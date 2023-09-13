package de.zeus.commons.provider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.zeus.commons.base.interfaces.IConnectionController;
import de.zeus.commons.connector.jdbc.config.ConfigBase;
import de.zeus.commons.connector.jdbc.config.JdbcConfig;
import de.zeus.commons.connector.jdbc.config.SparkConfig;
import de.zeus.commons.provider.logic.sql.ConnectionControllerFactory;
import de.zeus.commons.provider.service.HttpServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Provider class serves as the entry point for running the application as a REST service or from the console.
 * It handles command-line arguments, initializes the necessary components, and starts the service.
 */
public class Provider {

	public static void main(String[] args) {

		if (args.length == 3 || args.length == 4) {
			ProviderConfig config = parseArgs(args);
			Provider.initConfig(config);
			ConnectionControllerFactory controllerFactory = new ConnectionControllerFactory();

			if (config.getMode() != null) {
				initServiceFromConsole(controllerFactory, config.getJsonRequest(), config.getMode());
			} else {
				initService(controllerFactory);
			}

		} else {
			displayUsage();
			System.exit(0);
		}
	}
	public static ProviderConfig parseArgs(String[] args) {
		ProviderConfig config = new ProviderConfig();

		String runMode = args[0];

		if(runMode.equals("REST")) {
			config.setJdbcConfig(args[1]);
			config.setSparkConfig(args[2]);
		}
		else {
			config.setJdbcConfig(args[1]);
			config.setJsonRequest(new Gson().fromJson(args[3], JsonObject.class));
		}

		return config;
	}

	/**
	 * Displays the usage information for running the application.
	 */
	public static void displayUsage() {
		System.out.println("Usage:");
		System.out.println("============================================");
		System.out.println("Run Service as REST-Service");
		System.out.println("============================================");
		System.out.println("Parm 1: jdbc-properties-file");
		System.out.println("Parm 2: spark-properties-file");
		System.out.println("Parm 3: log4j-config-file");
		System.out.println("============================================");
		System.out.println("Run Service from console");
		System.out.println("============================================");
		System.out.println("Parm 1: application-properties-file");
		System.out.println("Parm 2: log4j-config-file");
		System.out.println("Parm 3: application/xml or application/json");
		System.out.println("Parm 4: JSON Query-Objects {}");
	}

	/**
	 * Initializes and starts the service as a REST service.
	 *
	 * @param controllerFactory The factory for creating connection controllers.
	 */
	public static void initService(ConnectionControllerFactory controllerFactory) {
		new HttpServer(controllerFactory);
	}

	/**
	 * Initializes and runs the service from the console with the specified JSON request and processing mode.
	 *
	 * @param controllerFactory The factory for creating connection controllers.
	 * @param jsonRequest       The JSON request to process.
	 * @param mode              The processing mode (JSON or XML).
	 */
	public static void initServiceFromConsole(ConnectionControllerFactory controllerFactory, JsonObject jsonRequest, String mode) {
		IConnectionController connectionController = controllerFactory.getController();
		System.out.println(connectionController.process(jsonRequest, mode));
	}

	private static void initConfig(ProviderConfig config) {

		JdbcConfig jdbcConfig = JdbcConfig.getInstance();
		jdbcConfig.setPropertiesFile(config.getJdbcConfig());;
		jdbcConfig.loadProperties();

		SparkConfig sparkConfig = SparkConfig.getInstance();
		sparkConfig.setPropertiesFile(config.getSparkConfig());
		sparkConfig.loadProperties();
	}
}
