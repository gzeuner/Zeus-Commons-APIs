package de.zeus.commons.base.config;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Singleton class responsible for managing the JDBC data source configuration.
 */
public class DataSource {

	private final BasicDataSource basicDataSource = new BasicDataSource();
	private final JdbcConfig conf;

	// Singleton instance of DataSource
	public static DataSource dataSource = new DataSource();

	/**
	 * Private constructor for initializing the DataSource.
	 */
	private DataSource() {
		this.conf = JdbcConfig.getInstance();
		initBasicDataSource();
	}

	/**
	 * Initializes the basic data source with configuration settings.
	 */
	private void initBasicDataSource() {
		basicDataSource.setDriverClassName(conf.getDbDriver());
		basicDataSource.setUrl(conf.getDbUrl());
		basicDataSource.setUsername(conf.getDbUser());
		basicDataSource.setPassword(conf.getDbPass());
		basicDataSource.setInitialSize(conf.getDbPoolInitSize());
		basicDataSource.setMaxTotal(conf.getDbPoolMaxSize());
		basicDataSource.setMaxIdle(conf.getDbPoolMaxIdle());
		basicDataSource.setRemoveAbandonedOnMaintenance(conf.isDbRemoveAbandonedConnections());
		basicDataSource.setRemoveAbandonedTimeout(conf.getDbRemoveAbandonedConnectionsTimout());
	}

	/**
	 * Get the instance of the DataSource.
	 *
	 * @return The DataSource instance.
	 */
	public static DataSource getInstance() {
		return dataSource;
	}

	/**
	 * Get the underlying BasicDataSource.
	 *
	 * @return The BasicDataSource instance.
	 */
	public BasicDataSource getBasicDataSource() {
		return basicDataSource;
	}
}
