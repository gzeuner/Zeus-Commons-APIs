package de.zeus.commons.connector.jdbc.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The `JdbcConfig` class is responsible for configuring JDBC (Java Database Connectivity)
 * settings and managing database connection properties. It extends the `ConfigBase` class
 * to leverage its configuration loading and property management capabilities.
 */
public class JdbcConfig extends ConfigBase {

	/**
	 * The logger instance for this class.
	 */
	private static final Log LOG = LogFactory.getLog(JdbcConfig.class);

	/**
	 * JDBC Driver class name.
	 */
	private String dbDriver;

	/**
	 * Database URL.
	 */
	private String dbUrl;

	/**
	 * Database username.
	 */
	private String dbUser;

	/**
	 * Database password.
	 */
	private String dbPass;

	/**
	 * Initial size of the database connection pool.
	 */
	private int dbPoolInitSize;

	/**
	 * Maximum size of the database connection pool.
	 */
	private int dbPoolMaxSize;

	/**
	 * Maximum idle connections in the database connection pool.
	 */
	private int dbPoolMaxIdle;

	/**
	 * Maximum time to wait for a database connection (in milliseconds).
	 */
	private int dbPoolMaxWait;

	/**
	 * Timeout for removing abandoned database connections (in seconds).
	 */
	private int dbRemoveAbandonedConnectionsTimout;

	/**
	 * Flag indicating whether abandoned database connections should be removed.
	 */
	private boolean dbRemoveAbandonedConnections;

	/**
	 * A single instance of the `JdbcConfig` class.
	 */
	private static JdbcConfig config = new JdbcConfig();

	/**
	 * Private constructor to enforce a singleton pattern and specify the properties file path.
	 */
	private JdbcConfig() {
		super("config/jdbc.properties");
	}

	/**
	 * Performs initial tasks such as reading and setting database configuration properties.
	 */
	public void loadProperties() {

		if (readPropertiesFile()) {
			setDbDriver(getProperty("db.drv"));
			setDbUrl(getProperty("db.url"));
			setDbUser(getProperty("db.usr"));
			setDbPass(getProperty("db.pwd"));
			setDbPoolInitSize(getProperty("db.pool.size.init"));
			setDbPoolMaxSize(getProperty("db.pool.max.size"));
			setDbPoolMaxIdle(getProperty("db.pool.max.idle"));
			setDbPoolMaxWait(getProperty("db.pool.max.wait"));
			setDbRemoveAbandonedConnectionsTimout(getProperty("db.connection.removeAbandonedTimout"));
			setDbRemoveAbandonedConnections(getProperty("db.connection.removeAbandoned"));
		}
	}

	/**
	 * Returns the instance of Config.java class
	 *
	 * @return Config.class - SingleTone Object
	 */
	public static JdbcConfig getInstance() {
		return config;
	}

	public String getDbDriver() {
		return dbDriver;
	}

	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPass() {
		return dbPass;
	}

	public void setDbPass(String dbPass) {
		this.dbPass = dbPass;
	}

	public void setDbPoolInitSize(String dbPoolInitSize) {
		try {
			setDbPoolInitSize(Integer.parseInt(dbPoolInitSize));
		}catch (Exception e) {
			LOG.info("The initial connection poolsize must be a numeric value. Using default size: [1] ");
			setDbPoolInitSize(1);
		}
	}

	public void setDbPoolInitSize(int dbPoolInitSize) {
		this.dbPoolInitSize = dbPoolInitSize;
	}

	public int getDbPoolInitSize() {
		return dbPoolInitSize;
	}

	public void setDbPoolMaxSize(String dbPoolMaxSize) {
		try {
			setDbPoolMaxSize(Integer.parseInt(dbPoolMaxSize));
		}catch (Exception e) {
			LOG.info("The maximum connection poolsize must be a numeric value. Using default size: [10] ");
			setDbPoolMaxSize(10);
		}
	}

	public void setDbPoolMaxSize(int dbPoolMaxSize) {
		this.dbPoolMaxSize = dbPoolMaxSize;
	}

	public int getDbPoolMaxSize() {
		return dbPoolMaxSize;
	}

	public void setDbPoolMaxIdle(String dbPoolMaxIdle) {
		try {
			setDbPoolMaxIdle(Integer.parseInt(dbPoolMaxIdle));
		}catch (Exception e) {
			LOG.info("The maximum connection idle value must be numeric. Using default value: [10] ");
			setDbPoolMaxIdle(10);
		}
	}

	public void setDbPoolMaxIdle(int dbPoolMaxIdle) {
		this.dbPoolMaxIdle = dbPoolMaxIdle;
	}

	public int getDbPoolMaxIdle() {
		return dbPoolMaxIdle;
	}

	public void setDbPoolMaxWait(String dbPoolMaxWait) {
		try {
			setDbPoolMaxWait(Integer.parseInt(dbPoolMaxWait));
		}catch (Exception e) {
			LOG.info("The wait for connection time value must be numeric. Using default value: [-1] ");
			setDbPoolMaxWait(-1);
		}
	}

	public void setDbPoolMaxWait(int dbPoolMaxWait) {
		this.dbPoolMaxWait = dbPoolMaxWait;
	}

	public int getDbPoolMaxWait() {
		return dbPoolMaxWait;
	}

	public void setDbRemoveAbandonedConnectionsTimout(String dbRemoveAbandonedConnectionsTimout) {
		try {
			setDbRemoveAbandonedConnectionsTimout(Integer.parseInt(dbRemoveAbandonedConnectionsTimout));
		}catch (Exception e) {
			LOG.info("The remove abandoned connection time value must be numeric. Using default value: [60] ");
			setDbRemoveAbandonedConnectionsTimout(60);
		}
	}

	public void setDbRemoveAbandonedConnectionsTimout(int dbRemoveAbandonedConnectionsTimout) {
		this.dbRemoveAbandonedConnectionsTimout = dbRemoveAbandonedConnectionsTimout;
	}

	public int getDbRemoveAbandonedConnectionsTimout() {
		return dbRemoveAbandonedConnectionsTimout;
	}

	public void setDbRemoveAbandonedConnections(String dbRemoveAbandonedConnections) {
		try {
			setDbRemoveAbandonedConnections(Boolean.parseBoolean(dbRemoveAbandonedConnections));
		}catch (Exception e) {
			LOG.info("The remove abandoned connection value must be a boolean (true|false). Using default value: [true]");
			setDbRemoveAbandonedConnections(true);
		}
	}

	public void setDbRemoveAbandonedConnections(boolean dbRemoveAbandonedConnections) {
		this.dbRemoveAbandonedConnections = dbRemoveAbandonedConnections;
	}

	public boolean isDbRemoveAbandonedConnections() {
		return dbRemoveAbandonedConnections;
	}

}
