package de.zeus.commons.provider.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.zeus.commons.base.interfaces.IConnectionController;
import de.zeus.commons.connector.jdbc.DatabaseConnectionException;
import de.zeus.commons.provider.model.ContentFieldData;
import de.zeus.commons.provider.model.ContentRecordData;
import de.zeus.commons.provider.model.DataWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class responsible for processing JSON requests containing sql queries.
 */
public class JsonRequestProcessor {

    private static final Log LOG = LogFactory.getLog(JsonRequestProcessor.class);
    /** SQL connection controller interface. */
    private final IConnectionController sqlController;
    /** Holds Level One data wrappers. */
    private final List<DataWrapper> levelOneWrapper = new ArrayList<>();
    /** Holds SQL statements. */
    private final Map<String, String> sqlStatements = new LinkedHashMap<>();

    private DataWrapper currentLevelOneWrapper;
    private List<DataWrapper> currentLevelTwoWrappers = new ArrayList<>();
    private List<List<DataWrapper>> currentLevelThreeWrappers = new ArrayList<>();
    private final Map<Integer, Consumer<Map.Entry<String, String>>> levelActions = new HashMap<>();

    private String elementName = null;
    private String statement = null;

    /**
     * Constructor for JsonRequestProcessor.
     *
     * @param sqlController SQL connection controller interface.
     */
    public JsonRequestProcessor(IConnectionController sqlController) {
        this.sqlController = sqlController;
        initLevelActions();
    }

    /**
     * Initialize actions for different nesting levels.
     */
    public void initLevelActions() {
        levelActions.put(1, this::processLevelOne);
        levelActions.put(2, this::processLevelTwo);
        levelActions.put(3, this::processLevelThree);
        levelActions.put(4, this::processLevelFour);
    }

    /**
     * Process the JSON request.
     *
     * @param jsonRequest The JSON object representing the request.
     */
    public void processJSONRequest(JsonObject jsonRequest) {
        analyseJSONRequest(jsonRequest);
        executeSQLStatements();
    }

    /**
     * Execute SQL statements based on the JSON request.
     */
    private void executeSQLStatements() {
        int nestingLevel = 1;
        for (Map.Entry<String, String> entry : sqlStatements.entrySet()) {
            executeByNestingLevel(nestingLevel, entry);
            nestingLevel++;
        }
    }

    private void executeByNestingLevel(int nestingLevel, Map.Entry<String, String> entry) {
        Consumer<Map.Entry<String, String>> action = levelActions.get(nestingLevel);
        if (action != null) {
            action.accept(entry);
        } else {
            LOG.error("Unknown level: " + nestingLevel);
        }
    }
    /**
     * Process Level One data.
     *
     * <p>This method utilizes {@code addLevelOneData} to add a new Level One data wrapper.
     * If adding is successful, {@code currentLevelOneWrapper} is updated.</p>
     *
     * @param entry a key-value pair where the key is the element name and the value is the SQL statement for data retrieval.
     */
    private void processLevelOne(Map.Entry<String, String> entry) {
        Optional<DataWrapper> rootLevelDataWrapperOpt = addLevelOneData(entry.getKey(), entry.getValue());
        // Handle the case when no data could be added, if necessary
        // or some default value
        currentLevelOneWrapper = rootLevelDataWrapperOpt.orElse(null);
    }

    /**
     * Process Level Two data.
     */
    private void processLevelTwo(Map.Entry<String, String> entry) {
        currentLevelTwoWrappers = addLevelTwoData(entry.getKey(),
                entry.getValue(), currentLevelOneWrapper);
    }

    /**
     * Process Level Three data.
     */
    private void processLevelThree(Map.Entry<String, String> entry) {
        currentLevelThreeWrappers = addLevelThreeData(entry.getKey(),
                entry.getValue(), currentLevelTwoWrappers);
    }

    /**
     * Process Level Four data.
     */
    private void processLevelFour(Map.Entry<String, String> entry) {
        addLevelFourData(entry.getKey(),
                entry.getValue(), currentLevelThreeWrappers);
    }

    /**
     * Returns the processed result.
     *
     * @return List of DataWrapper objects.
     */
    public List<DataWrapper> getResult() {
        return levelOneWrapper;
    }

    private void analyseJSONRequest(JsonObject jsonRequest) {
        for (Map.Entry<String, JsonElement> entry : jsonRequest.entrySet()) {
            JsonElement value = entry.getValue();
            if (value.isJsonObject()) {
                handleJsonObject(value.getAsJsonObject());
            } else if (value.isJsonPrimitive()) {
                handleJsonPrimitive(entry);
            }
        }
    }

    private void handleJsonObject(JsonObject queryObject) {
        sqlStatements.put(queryObject.get("name").getAsString(), queryObject.get("statement").getAsString());

        // Nested Subquery, recursive call to analyseJSONRequest-method
        if (queryObject.has("subquery")) {
            analyseJSONRequest(queryObject.get("subquery").getAsJsonObject());
        }
    }


    private void handleJsonPrimitive(Map.Entry<String, JsonElement> entry) {
        if ("name".equals(entry.getKey())) {
            elementName = entry.getValue().getAsString();
        }
        if ("statement".equals(entry.getKey())) {
            statement = entry.getValue().getAsString();
        }
        if (null != elementName && null != statement) {
            sqlStatements.put(elementName, statement);
            elementName = null;
            statement = null;
        }
    }

    /**
     * Attempts to read data safely using the provided SQL statement.
     *
     * <p>This method wraps the {@code readData} method and handles {@code DatabaseConnectionException}
     * internally by logging an error message. It returns an empty {@code Optional} if the data reading fails.</p>
     *
     * @param statement the SQL statement to be executed for data retrieval.
     * @return an {@code Optional<DataWrapper>} that will contain the data if the reading is successful, or be empty if it fails.
     */
    private Optional<DataWrapper> safelyReadData(String statement) {
        try {
            return Optional.ofNullable(readData(statement));
        } catch (DatabaseConnectionException e) {
            LOG.error("Error reading data with the statement: " + statement, e);
            return Optional.empty();
        }
    }

    /**
     * Adds Level One data to the existing data structure.
     *
     * <p>This method uses {@code safelyReadData} to read data based on the provided SQL statement.
     * If the data reading is successful, it sets the name of the {@code DataWrapper} object,
     * adds it to the {@code levelOneWrapper}, and returns it wrapped in an {@code Optional}.</p>
     *
     * @param elementName the name to set for the {@code DataWrapper} object.
     * @param statement the SQL statement to be executed for data retrieval.
     * @return an {@code Optional<DataWrapper>} containing the data read, or empty if reading fails.
     */
    private Optional<DataWrapper> addLevelOneData(String elementName, String statement) {
        Optional<DataWrapper> rootLevelDataWrapperOpt = safelyReadData(statement);
        if (rootLevelDataWrapperOpt.isPresent()) {
            DataWrapper rootLevelDataWrapper = rootLevelDataWrapperOpt.get();
            rootLevelDataWrapper.setName(elementName);
            levelOneWrapper.add(rootLevelDataWrapper);
        }
        return rootLevelDataWrapperOpt;
    }



    /**
     * Adds data for Level Two.
     * This method processes a single element for Level Two data and associates it with its parent Level One element.
     *
     * @param elementName    The name of the element to be processed.
     * @param statement      The SQL statement to be executed.
     * @param contentWrapper The parent data wrapper containing Level One data.
     * @return A list of data wrappers containing processed Level Two data. The list may be empty if no data could be processed.
     */
    private List<DataWrapper> addLevelTwoData(String elementName, String statement, DataWrapper contentWrapper) {
        List<DataWrapper> currentDataWrappers = new ArrayList<>();

        for (ContentRecordData contentRecord : contentWrapper.getContentData()) {
            HashMap<String, String> data = collectDataFromContentRecord(contentWrapper, contentRecord);
            String dynStatement = replaceDynamicValuesInSql(statement, data);
            Optional<DataWrapper> childWrapperOpt = readAndWrapData(dynStatement, elementName, contentRecord);
            childWrapperOpt.ifPresent(currentDataWrappers::add);
        }

        return currentDataWrappers;
    }


    private List<List<DataWrapper>> addLevelThreeData(String elementName, String statement, List<DataWrapper> currentDataWrappers) {

        List<List<DataWrapper>> resultDataWrappers = new ArrayList<>();
        for (DataWrapper dataWrapper : currentDataWrappers) {
            resultDataWrappers.add(addLevelTwoData(elementName, statement, dataWrapper));
        }
        return resultDataWrappers;
    }

    private void addLevelFourData(String elementName, String statement, List<List<DataWrapper>> currentDataWrappers) {
        for(List<DataWrapper> dataWrappers : currentDataWrappers) {
            addLevelThreeData(elementName, statement, dataWrappers);
        }
    }

    /**
     * Collects data from a single ContentRecordData object.
     *
     * @param contentWrapper The parent data wrapper containing metadata.
     * @param contentRecord  The ContentRecordData object from which data is to be collected.
     * @return A HashMap containing the collected data.
     */
    private HashMap<String, String> collectDataFromContentRecord(DataWrapper contentWrapper, ContentRecordData contentRecord) {
        HashMap<String, String> data = new HashMap<>();
        for (ContentFieldData contentField : contentRecord.getFieldDataObjs()) {
            data.put(contentWrapper.getMetaData().get(contentField.getColumnNumber()).getColumnName(), contentField.getValue());
            LOG.debug(contentWrapper.getName() + " " + contentWrapper.getMetaData().get(contentField.getColumnNumber()).getColumnName() + " " + contentField.getValue());
        }
        return data;
    }

    /**
     * Replaces dynamic values in an SQL statement using a HashMap.
     *
     * @param statement The SQL statement template.
     * @param data      The HashMap containing keys and values to replace in the SQL statement.
     * @return The SQL statement with dynamic values replaced.
     */
    private String replaceDynamicValuesInSql(String statement, HashMap<String, String> data) {
        List<String> keys = getKeysFromSqlQuery(statement);
        String dynStatement = statement;
        for (String key : keys) {
            String value = data.get(key);
            if (null != value) {
                dynStatement = dynStatement.replace(getKeyByJSONkey(key), value);
            }
        }
        return dynStatement;
    }

    /**
     * Reads data from a database, wraps it in a DataWrapper object, and associates it with a parent ContentRecordData object.
     *
     * <p>If the data reading is successful, sets the name for the DataWrapper object and adds it to the parent ContentRecordData.</p>
     *
     * @param dynStatement  The dynamic SQL statement to be executed.
     * @param elementName   The name of the element.
     * @param contentRecord The parent ContentRecordData object.
     * @return An Optional<DataWrapper> containing the data, or empty if reading fails.
     */
    private Optional<DataWrapper> readAndWrapData(String dynStatement, String elementName, ContentRecordData contentRecord) {
        Optional<DataWrapper> childWrapperOpt = safelyReadData(dynStatement);
        LOG.debug("dynStatement : " + elementName + " : " + dynStatement);

        if (childWrapperOpt.isPresent()) {
            DataWrapper childWrapper = childWrapperOpt.get();
            childWrapper.setName(elementName);
            contentRecord.addDataWrapper(childWrapper);
            return childWrapperOpt;
        } else {
            // Handle the case when no data could be read and wrapped, if necessary.
            return Optional.empty();
        }
    }


    private DataWrapper readData(String sqlStatement) throws DatabaseConnectionException {

        return (DataWrapper) sqlController.readData(sqlStatement);
    }


    // get all [§KEY] entries from sqlStatement
    private List<String> getKeysFromSqlQuery (String sqlQuery){

        List<String> matchList = new ArrayList<String>();
        Pattern regex = Pattern.compile("\\[(.*?)\\]");
        Matcher regexMatcher = regex.matcher(sqlQuery);

        while (regexMatcher.find()) {
            matchList.add(regexMatcher.group(1).replace("$", ""));
        }
        return matchList;
    }

    private String getKeyByJSONkey(String key) {
        return "[$" + key + "]".toUpperCase();
    }
}
