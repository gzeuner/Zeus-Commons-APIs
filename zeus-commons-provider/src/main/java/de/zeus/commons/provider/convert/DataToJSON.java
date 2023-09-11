package de.zeus.commons.provider.convert;

import com.google.gson.JsonObject;
import de.zeus.commons.provider.config.IProviderConstants;
import de.zeus.commons.provider.model.ContentFieldData;
import de.zeus.commons.provider.model.ContentRecordData;
import de.zeus.commons.provider.model.DataWrapper;
import de.zeus.commons.provider.model.MetaData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * This class is responsible for converting a list
 * of {@code DataWrapper} objects into a JSON representation.
 * It allows for the optional inclusion of metadata.
 */
public class DataToJSON {
    private static final String CONTENT_DATA = "contentData";
    private final List<DataWrapper> dataWrapperList;
    private final JsonObject resultJson = new JsonObject();
    private final JsonObject contentJson = new JsonObject();

    private static final Log LOG = LogFactory.getLog(DataToJSON.class);

    private final boolean includeMetaData; // Option to include metadata

    /**
     * Initializes a new instance of the DataToJSON class.
     *
     * @param dataWrappers    A list of DataWrapper objects containing the data to be converted to JSON.
     * @param includeMetaData A flag indicating whether metadata should be included in the JSON output.
     */
    public DataToJSON(List<DataWrapper> dataWrappers, boolean includeMetaData) {
        this.dataWrapperList = dataWrappers;
        this.includeMetaData = includeMetaData; // Set the option here
        resultJson.add(CONTENT_DATA, contentJson);
        addContentDataToRoot();
    }

    /**
     * Adds the content data to the root JSON object.
     */
    private void addContentDataToRoot() {
        dataWrapperList.forEach(this::addContentDataForWrapper);
    }

    /**
     * Adds the content data for a specific DataWrapper to the JSON object.
     *
     * @param dataWrapper The DataWrapper object for which to add content data.
     */
    private void addContentDataForWrapper(DataWrapper dataWrapper) {
        JsonObject contentData = new JsonObject();
        contentJson.add(dataWrapper.getName(), contentData);
        dataWrapper.getContentData().forEach(contentRecord ->
                addContentDataForRecord(contentData, contentRecord, dataWrapper));
    }

    /**
     * Adds content data for a specific record to the JSON object.
     *
     * @param contentData    The JSON object to which content data should be added.
     * @param contentRecord  The ContentRecordData object containing the data for the record.
     * @param dataWrapper    The DataWrapper object containing the metadata for the field.
     */
    private void addContentDataForRecord(JsonObject contentData,
                                         ContentRecordData contentRecord,
                                         DataWrapper dataWrapper) {
        JsonObject contentDetail = new JsonObject();
        String recordKey = getRecordKey(contentRecord, dataWrapper);
        contentData.add(recordKey, contentDetail);
        populateContentDetail(contentDetail, contentRecord, dataWrapper);
        addContentDataToChildJson(contentRecord, contentDetail);
    }

    // Private method to get the record key
    private String getRecordKey(ContentRecordData contentRecord, DataWrapper dataWrapper) {
        return contentRecord.getFieldDataObjs()
                .stream()
                .filter(contentField -> dataWrapper
                        .getMetaData().get(contentField.getColumnNumber()).isPrimaryKey())
                .map(ContentFieldData::getValue)
                .findFirst()
                .orElse(String.valueOf(contentRecord.getRecordNumber()));
    }

    // Private method to populate content detail including metadata
    private void populateContentDetail(JsonObject contentDetail, ContentRecordData contentRecord, DataWrapper dataWrapper) {
        contentRecord.getFieldDataObjs().forEach(contentField -> {
            String columnName = dataWrapper.getMetaData()
                    .get(contentField.getColumnNumber())
                    .getColumnName();
            String columnValue = contentField.getValue();
            contentDetail.addProperty(columnName, columnValue);

            if (includeMetaData) {
                JsonObject metaDataJson = createMetaDataJson(dataWrapper, contentField);
                if (metaDataJson != null) {
                    contentDetail.add(columnName +
                            IProviderConstants.META_DATA_SUFFIX, metaDataJson);

                    // Logging the inclusion of metadata
                    LOG.debug("Included metadata for column: " + columnName);
                }
            }
        });
    }

    // Private method to create metadata JSON
    private JsonObject createMetaDataJson(DataWrapper dataWrapper,
                                          ContentFieldData contentField) {
        int columnNumber = contentField.getColumnNumber();
        MetaData metaData = dataWrapper.getMetaDataForField(columnNumber);

        if (metaData != null) {
            JsonObject metaDataJson = new JsonObject();
            metaDataJson.addProperty(IProviderConstants.COLUMN_NUMBER,
                    metaData.getColumnNumber());
            metaDataJson.addProperty(IProviderConstants.IS_PRIMARY_KEY,
                    metaData.isPrimaryKey());
            metaDataJson.addProperty(IProviderConstants.DATA_TYPE_NAME,
                    metaData.getColumnSqlDataTypeName());
            metaDataJson.addProperty(IProviderConstants.DATA_TYPE_ID,
                    metaData.getColumnSqlDataType());
            metaDataJson.addProperty(IProviderConstants.CLASS_NAME,
                    metaData.getColumnClassName());
            return metaDataJson;
        }

        return null;
    }

    // Private method to add content data for child records to JSON
    private void addContentDataToChildJson(ContentRecordData contentRecord,
                                           JsonObject contentDetail) {
        contentRecord.getDataWrappers().forEach(dataChildWrapper ->
                addContentDataForChildWrapper(contentDetail, dataChildWrapper));
    }

    // Private method to add content data for child records' content to JSON
    private void addContentDataForChildWrapper(JsonObject contentDetail,
                                               DataWrapper dataChildWrapper) {
        JsonObject contentChild = new JsonObject();
        contentDetail.add(dataChildWrapper.getName(), contentChild);
        dataChildWrapper.getContentData().forEach(contentRec ->
                addContentDataForChildRecord(contentChild, contentRec, dataChildWrapper));
    }

    // Private method to add content data for child records' content records to JSON
    private void addContentDataForChildRecord(JsonObject contentChild,
                                              ContentRecordData contentRec,
                                              DataWrapper dataChildWrapper) {
        JsonObject childDetail = new JsonObject();
        String childRecordKey = getRecordKey(contentRec, dataChildWrapper);
        contentChild.add(childRecordKey, childDetail);
        populateContentDetail(childDetail, contentRec, dataChildWrapper);
        addContentDataToChildJson(contentRec, childDetail);
    }

    /**
     * Gets the resulting JSON object that contains all the content data and optionally the metadata.
     *
     * @return A JsonObject representing the JSON data.
     */
    public JsonObject getResult() {
        return resultJson;
    }
}
