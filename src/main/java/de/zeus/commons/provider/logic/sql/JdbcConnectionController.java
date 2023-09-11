package de.zeus.commons.provider.logic.sql;

import com.google.gson.JsonObject;
import de.zeus.commons.base.interfaces.IConnectionController;
import de.zeus.commons.base.interfaces.IJdbcOperations;
import de.zeus.commons.provider.service.JsonRequestProcessor;
import de.zeus.commons.provider.config.IProviderConstants;
import de.zeus.commons.provider.convert.DataToJSON;
import de.zeus.commons.provider.convert.DataToXML;
import de.zeus.commons.provider.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.Select;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Controller for JDBC database connections.
 */
public class JdbcConnectionController implements IConnectionController, IProviderConstants {

	private static final Log LOG = LogFactory.getLog(JdbcConnectionController.class);

	private Connection databaseConnection;
	private final ReentrantLock lock = new ReentrantLock();
	private final IJdbcOperations jdbcOperations;

	/**
	 * Constructor for JdbcConnectionController.
	 *
	 * @param jdbcOperations The JDBC operations implementation.
	 */
	public JdbcConnectionController(IJdbcOperations jdbcOperations) {
		super();
		this.jdbcOperations = jdbcOperations;
	}

	/** Initialize the configuration. */
	@Override
	public void init() {
		this.jdbcOperations.initConfig();
	}

	@Override
	public Object process(JsonObject jsonRequest, String mode) {
		// Check if the "includeMetadata" flag is provided in the request and set the flag accordingly.
		boolean includeMetadata = jsonRequest.has("includeMetadata")
				&& jsonRequest.get("includeMetadata").getAsBoolean();

		if (MODE_JSON.equals(mode)) {
			return new DataToJSON(getData(jsonRequest), includeMetadata).getResult();
		}
		if (MODE_XML.equals(mode)) {
			return new DataToXML(getData(jsonRequest), includeMetadata).getResult();
		}
		return null;
	}

	/**
	 * Retrieves data based on the provided JSON request.
	 *
	 * @param jsonRequest The JSON request.
	 * @return A list of DataWrapper objects.
	 */
	public List<DataWrapper> getData(JsonObject jsonRequest) {
		connectDataService();
		JsonRequestProcessor requestProcessor = new JsonRequestProcessor(this);
		requestProcessor.processJSONRequest(jsonRequest);
		List<DataWrapper> dataWrapperList = requestProcessor.getResult();
		disconnectDataService();
		return dataWrapperList;
	}

	@Override
	public void connectDataService() {
		lock.lock(); // Lock the resource to ensure thread safety.
		try {
			this.databaseConnection = this.jdbcOperations.getDatabaseConnection();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void disconnectDataService() {
		lock.lock(); // Lock the resource to ensure thread safety.
		try {
			this.jdbcOperations.closeDatabaseConnection(this.databaseConnection);
		} finally {
			lock.unlock();
		}
	}

	public net.sf.jsqlparser.statement.Statement parseAndValidateSql(String sql) throws JSQLParserException {
		// First, check for any unwanted characters in the SQL string
		if (sql.matches(".*[^a-zA-Z0-9_ ,.*=<>!()'\"-].*")) {
			throw new UnsupportedOperationException("Invalid characters in SQL query.");
		}

		// Then, parse the statement using JSQLParser
		net.sf.jsqlparser.statement.Statement statement = CCJSqlParserUtil.parse(sql);

		// Finally, check if it's a SELECT statement
		if (statement instanceof Select) {
			// Additional validations or modifications can go here
			return statement;
		} else {
			throw new UnsupportedOperationException("Only SELECT queries are allowed.");
		}
	}


	/**
	 * Retrieves primary key fields for a given table.
	 *
	 * @param catalog The catalog name.
	 * @param schema  The schema name.
	 * @param table   The table name.
	 * @return A list of primary key fields.
	 */
	public List<String> getPrimaryKeys(String catalog, String schema, String table) {

		DatabaseMetaData meta = null;
		List<String> primaryKeyFields = new ArrayList<>();
		try {
			meta = this.databaseConnection.getMetaData();
		} catch (SQLException e) {
			LOG.error("Error while fetching metaData", e);
		}
		try (ResultSet tables = Objects.requireNonNull(meta).getTables(catalog, schema, table, new String[]{"TABLE"})) {
			while (tables.next()) {
				try (ResultSet pKeys = meta.getPrimaryKeys(catalog, schema, table)) {
					while (pKeys.next()) {
						primaryKeyFields.add(pKeys.getString("COLUMN_NAME"));
					}
				}
			}
		} catch (SQLException e) {
			LOG.error("Error while executing getPrimaryKeys", e);
		}

		return primaryKeyFields;
	}

	@Override
	public DataWrapper readData(String sqlQuery) {
		DataWrapper dataWrapper = new DataWrapper();

		try {
			// Parse and validate the SQL query
			net.sf.jsqlparser.statement.Statement parsedSql = parseAndValidateSql(sqlQuery);

			if (parsedSql instanceof Select) {
				java.sql.Statement stmt = jdbcOperations.getStmt(this.databaseConnection);
				ResultSet rs = stmt.executeQuery(sqlQuery);

				ResultSetMetaData meta = rs.getMetaData();
				for (int column = 1; column <= meta.getColumnCount(); column++) {
					MetaData metaData = new MetaData();
					metaData.setColumnNumber(column);
					metaData.setColumnLabel(meta.getColumnLabel(column));
					metaData.setColumnName(meta.getColumnName(column));
					metaData.setColumnSqlDataType(meta.getColumnType(column));
					metaData.setColumnSqlDataTypeName(meta.getColumnTypeName(column));
					metaData.setColumnClassName(meta.getColumnClassName(column));
					metaData.addInfoToTableList(new TableInfo(meta.getCatalogName(column), meta.getSchemaName(column), meta.getTableName(column)));
					dataWrapper.addMetaData(metaData);
				}

				// Add Primary Keys
				for (MetaData metaData : dataWrapper.getMetaData().values()) {
					for (TableInfo tableInfo : metaData.getTableInfoList()) {
						tableInfo.addAllFieldsToPrimaryKey(getPrimaryKeys(tableInfo.getCatalog(), tableInfo.getSchema(), tableInfo.getTable()));
					}
					metaData.checkPrimaryKey();
				}

				int recNo = 0;
				while (rs.next()) {
					recNo++;
					ContentRecordData contentRecordData = new ContentRecordData();
					dataWrapper.addContentData(contentRecordData);
					for (int column = 1; column <= meta.getColumnCount(); column++) {
						ContentFieldData contentFieldData = new ContentFieldData();
						contentRecordData.setRecordNumber(recNo);
						contentFieldData.setColumnNumber(column);
						contentFieldData.setValue(rs.getString(column));
						contentRecordData.addContentFieldData(contentFieldData);
					}
				}

				jdbcOperations.close(rs, stmt);
			}
		} catch (JSQLParserException e) {
			LOG.error("Error parsing SQL query", e);
		} catch (SQLException e) {
			LOG.error("Error while executing SQLQuery", e);
		}

		return dataWrapper;
	}


}
