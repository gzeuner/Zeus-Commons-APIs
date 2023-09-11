package de.zeus.commons.connector.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.zeus.commons.base.interfaces.IJdbcOperations;
import de.zeus.commons.connector.jdbc.config.JdbcConfig;
import de.zeus.commons.connector.jdbc.config.DataSource;

/**
 * Implementation of the IJdbcOperations interface for JDBC database operations.
 */
public class JdbcOperations implements IJdbcOperations {

	private static final Log LOG = LogFactory.getLog(JdbcOperations.class);
	private DataSource dataSource;
	private String appConfig;
	private String logConfig;

	/**
	 * Constructs a JdbcOperations instance.
	 *
	 * @param appConfig The application configuration file path.
	 * @param logConfig The log4j configuration file path.
	 */
	public JdbcOperations(String appConfig, String logConfig) {
		super();
		setAppConfig(appConfig);
		setLogConfig(logConfig);
		initConfig();
		initDataSource();
	}

	@Override
	public Connection getDatabaseConnection() {
		Connection con = null;
		try {
			con = dataSource.getBasicDataSource().getConnection();
			LOG.debug("Fetching connection from the pool");
		} catch (SQLException e) {
			LOG.error("Error while fetching connection from the pool", e);
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
	public Statement getStmt(Connection con) {
		Statement stmt = null;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			return stmt;
		}
		return stmt;
	}

	public BasicDataSource getBasicDataSource() {
		return dataSource.getBasicDataSource();
	}


	public PreparedStatement prepareStatementFromJson(JsonObject queryJson, Connection connection, Map<String, Object> replaceValues) throws SQLException {
		String statement = queryJson.get("statement").getAsString();
		List<Object> params = new ArrayList<>();

		for(Map.Entry<String, Object> entry : replaceValues.entrySet()) {
			String placeholder = "[$" + entry.getKey() + "]";
			if(statement.contains(placeholder)) {
				statement = statement.replace(placeholder, "?");
				params.add(entry.getValue());
			}
		}

		PreparedStatement preparedStatement = connection.prepareStatement(statement);

		for (int i = 0; i < params.size(); i++) {
			Object param = params.get(i);
			if(param instanceof String) {
				preparedStatement.setString(i + 1, (String) param);
			} else if(param instanceof Integer) {
				preparedStatement.setInt(i + 1, (Integer) param);
			}
		}

		return preparedStatement;
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

	public String getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(String appConfig) {
		this.appConfig = appConfig;
	}

	public String getLogConfig() {
		return logConfig;
	}

	public void setLogConfig(String logConfig) {
		this.logConfig = logConfig;
	}

	@Override
	public void initConfig() {
		JdbcConfig config = JdbcConfig.getInstance();
		config.setPropertiesFile(appConfig);
		config.setLog4jConfPath(logConfig);
		config.init();
	}

	// Initialize dataSource
	public void initDataSource() {
		dataSource = DataSource.getInstance();
	}
}
