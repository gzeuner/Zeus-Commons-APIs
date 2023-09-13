package de.zeus.commons.connector.jdbc.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JdbcConfig {

	/**
	 * Logger-Instance
	 */
	private static final Log LOG = LogFactory.getLog(JdbcConfig.class);
	/**
	 * Use to read properties for this configuration
	 */
	private String propertiesFile;

	/**
	 * Used to provide properties at runtime
	 */
	private Properties properties = new Properties();
	
	/**
	 * JDBC Driver
	 */
	private String dbDriver;

	/**
	 * DB URL
	 */
	private String dbUrl;

	/**
	 * DB User
	 */
	private String dbUser;

	/**
	 * DB Password
	 */
	private String dbPass;
	
	private int dbPoolInitSize;
	
	private int dbPoolMaxSize;
	
	private int dbPoolMaxIdle;
	
	private int dbPoolMaxWait;
	
	private int dbRemoveAbandonedConnectionsTimout;
	
	private boolean dbRemoveAbandonedConnections;
	
	private String log4jConfPath;

	private int sparkJavaPort;
	private String sparkJavaAllowedHosts;
	
	/**
	 * Config object
	 */
	private static JdbcConfig config = new JdbcConfig();

	/**
	 * No external instantiation, Config is provided as single tone
	 */
	private JdbcConfig() {
	}

	/**
	 * Initial Tasks
	 */
	public void init() {

		createDefaultFolder();

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
			setSparkJavaPort(getProperty("spark.port"));
			setSparkJavaAllowedHosts(getProperty("spark.host"));
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

	/**
	 * Returns a property value for a given key
	 */
	public String getProperty(String key) {

		String property = this.properties.getProperty(key);
		if (property == null) {
			property = "NOT SET";
		}
		return property;
	}

	public boolean readPropertiesFile() {
		boolean fileOk = true;
		InputStream inputStream = null;
		// Attempt to read the file from the file system
		File propFile = new File(getPropertiesFile());
		try {
			if (propFile.exists()) {
				inputStream = new FileInputStream(propFile);
			} else {
				// Attempt to read the file from the JAR
				inputStream = getClass().getClassLoader().getResourceAsStream(getPropertiesFile());
				if (inputStream == null) {
					LOG.error("Property file '" + getPropertiesFile() + "' not found in the classpath");
					fileOk = false;
					return fileOk;
				}
			}

			try (InputStream is = inputStream) {  // try-with-resources in Java 8 style
				properties.load(is);
			}
		} catch (IOException e) {
			LOG.error("Failed to load properties", e);
			fileOk = false;
		}
		return fileOk;
	}


	/**
	 * Creates the "must have" directories
	 */
	private void createDefaultFolder() {

		File f = new File("./logs");
		if (!f.exists()) {
			f.mkdir();
		}
	}

	public String getPropertiesFile() {
		return propertiesFile;
	}

	public void setPropertiesFile(String propertiesFile) {
		this.propertiesFile = propertiesFile;
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

	public String getLog4jConfPath() {
		return log4jConfPath;
	}

	public void setLog4jConfPath(String log4jConfPath) {
		this.log4jConfPath = log4jConfPath;
		System.setProperty("log4j.configurationFile", this.log4jConfPath);
	}

	public void setSparkJavaPort(String sparkJavaPort) {
		try {
			Integer.parseInt(sparkJavaPort);
			this.sparkJavaPort=Integer.parseInt(sparkJavaPort);
		}catch (Exception e) {
			this.sparkJavaPort=4567;
			LOG.info("The Spark-Java port must be a numeric value. Using default port: [4567] ");
		}
	}
	public int getSparkJavaPort() {
		return sparkJavaPort;
	}

	public void setSparkJavaAllowedHosts(String sparkJavaAllowedHosts) {
		this.sparkJavaAllowedHosts = sparkJavaAllowedHosts;
	}

	public String getSparkJavaAllowedHosts() {
		return sparkJavaAllowedHosts;
	}
}
