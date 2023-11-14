package de.zeus.commons.connector.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import de.zeus.commons.base.config.JsonClientConfig;
import de.zeus.commons.consumer.model.DynamicJsonObject;
import de.zeus.commons.provider.model.MetaData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.zeus.commons.base.interfaces.IJdbcOperations;
import de.zeus.commons.base.config.DataSource;

/**
 * Implementation of the IJdbcOperations interface for JDBC database operations.
 */
public class JdbcOperations implements IJdbcOperations {

    private static final Log LOG = LogFactory.getLog(JdbcOperations.class);
    private DataSource dataSource;
    private String appConfig;
    private String logConfig;
    private final Set<String> existingMetadata = new HashSet<>();

    /**
     * Constructs a JdbcOperations instance.
     */
    public JdbcOperations() {
        initDataSource();
    }

    /**
     * Fetches a database connection from the connection pool.
     *
     * <p>This method attempts to fetch a database connection from the connection pool managed by the {@code dataSource}.</p>
     *
     * @return A {@code Connection} object representing the database connection.
     * @throws ProcessingException if fetching the connection from the pool fails.
     */
    @Override
    public Connection getDatabaseConnection() throws ProcessingException {
        Connection con = null;
        try {
            con = dataSource.getBasicDataSource().getConnection();
            LOG.debug("Fetching connection from the pool");
        } catch (SQLException e) {
            LOG.error("Error while fetching connection from the pool", e);
            throw new ProcessingException("Failed to get database connection", e);
        }
        return con;
    }


    @Override
    public void closeDatabaseConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
                LOG.debug("DB-Connection closed.");
            } catch (SQLException e) {
                LOG.error("Error while closing connection", e);
            }
        }
    }

    @Override
    public PreparedStatement getPreparedStmt(Connection con, String query, Object... params) {
        PreparedStatement pStmt = null;
        try {
            pStmt = con.prepareStatement(query);

            // Set parameters for the prepared statement
            for (int i = 0; i < params.length; i++) {
                pStmt.setObject(i + 1, params[i]);
            }

            return pStmt;
        } catch (SQLException e) {
            LOG.error("Error while preparing statement", e);
            return pStmt;
        }
    }

    @Override
    public Statement getStmt(Connection con) throws ProcessingException {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
        } catch (SQLException e) {
            LOG.error("Error while creating statement", e);
            throw new ProcessingException("Error while creating statement", e);
        }
        return stmt;
    }


    /**
     * Inserts data from a DynamicJsonObject into the eav_data table.
     *
     * @param dynamicJsonObject The DynamicJsonObject containing the data to insert.
     * @throws SQLException If there is an error executing the database operations.
     */
    public void insertDynamicJsonObject(DynamicJsonObject dynamicJsonObject) throws SQLException, ProcessingException {
        Connection con = null;
        try {
            con = getDatabaseConnection();
            String insertAttributeSql = JsonClientConfig.getInstance().getInsertEavData();
            try (PreparedStatement pStmtAttributes = con.prepareStatement(insertAttributeSql)) {
                insertAttributes(dynamicJsonObject, pStmtAttributes, "", con);
            }

        } catch (SQLException | ProcessingException e) {
            LOG.error("Error while inserting data into database", e);
            throw e;
        } finally {
            closeDatabaseConnection(con);
        }
    }

    private void insertAttributes(DynamicJsonObject jsonObject, PreparedStatement pStmtAttributes, String parentKey, Connection con) throws SQLException {
        // Process the regular attributes
        String entityId = jsonObject.getId();
        for (Map.Entry<String, Object> entry : jsonObject.getAttributes().entrySet()) {
            String key = concatenateKeys(parentKey, entry.getKey());
            Object value = entry.getValue();

            if (value instanceof DynamicJsonObject) {
                // Recursive call for nested objects
                insertAttributes((DynamicJsonObject) value, pStmtAttributes, key, con);
            } else {
                // Insert a single value
                insertSingleValue(entityId, key, value.toString(), pStmtAttributes);
            }
        }

        // Process and save the metadata
        insertMetadata(jsonObject, entityId, con);
    }

    private void insertMetadata(DynamicJsonObject jsonObject, String entityId, Connection con) throws SQLException {
        String insertMetadataSql = JsonClientConfig.getInstance().getInsertEavMetaData();
        try (PreparedStatement pStmtMetadata = con.prepareStatement(insertMetadataSql)) {
            for (Map.Entry<String, MetaData> entry : jsonObject.getMetadata().entrySet()) {
                String metadataKey = entry.getKey().replace("_metadata", "");
                MetaData metaData = entry.getValue();

                insertMetadataAttribute(entityId, metadataKey, "columnNumber", String.valueOf(metaData.getColumnNumber()), pStmtMetadata);
                insertMetadataAttribute(entityId, metadataKey, "columnName", metaData.getColumnName(), pStmtMetadata);
                insertMetadataAttribute(entityId, metadataKey, "columnSqlDataType", String.valueOf(metaData.getColumnSqlDataType()), pStmtMetadata);
                insertMetadataAttribute(entityId, metadataKey, "columnSqlDataTypeName", metaData.getColumnSqlDataTypeName(), pStmtMetadata);
                insertMetadataAttribute(entityId, metadataKey, "columnClassName", metaData.getColumnClassName(), pStmtMetadata);
                insertMetadataAttribute(entityId, metadataKey, "isPrimaryKey", String.valueOf(metaData.isPrimaryKey()), pStmtMetadata);
            }
        }
    }

    private void insertMetadataAttribute(String entityId, String metadataKey, String metadataType, String metadataValue, PreparedStatement pStmt) throws SQLException {
        pStmt.setString(1, entityId);
        pStmt.setString(2, metadataKey);
        pStmt.setString(3, metadataType);
        pStmt.setString(4, metadataValue);
        pStmt.executeUpdate();
    }



    private void insertSingleValue(String entityId, String key, Object value, PreparedStatement pStmt) throws SQLException {
        pStmt.setString(1, entityId);
        pStmt.setString(2, key);
        pStmt.setString(3, value.toString());
        pStmt.executeUpdate();
    }

    private String concatenateKeys(String parentKey, String childKey) {
        return parentKey.isEmpty() ? childKey : parentKey + "." + childKey;
    }

    @Override
    public void close(ResultSet rset, Statement stmt) throws SQLException {
        close(rset);
        close(stmt);
    }

    public void close(Statement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
    }

    public void close(ResultSet rset) throws SQLException {
        if (rset != null) {
            rset.close();
        }
    }

    // Initialize dataSource
    public void initDataSource() {
        dataSource = DataSource.getInstance();
    }
}
