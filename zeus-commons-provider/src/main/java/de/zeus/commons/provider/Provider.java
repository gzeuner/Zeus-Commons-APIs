package de.zeus.commons.provider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.zeus.commons.base.interfaces.IConnectionController;
import de.zeus.commons.provider.logic.sql.ConnectionControllerFactory;
import de.zeus.commons.provider.service.HttpServer;

/**
 * The Provider class serves as the entry point for running the application as a REST service or from the console.
 * It handles command-line arguments, initializes the necessary components, and starts the service.
 */
public class Provider {

	public static void main(String[] args) {

		if (args.length == 2 || args.length == 4) {
			String appConfig = args[0];
			String logConfig = args[1];
			ConnectionControllerFactory controllerFactory = new ConnectionControllerFactory(appConfig, logConfig);

			if (args.length == 4) {
				String mode = args[2];
				JsonObject jsonRequest = new Gson().fromJson(args[3], JsonObject.class);
				initServiceFromConsole(controllerFactory, jsonRequest, mode);
			} else {
				initService(controllerFactory);
			}

		} else {
			displayUsage();
			System.exit(0);
		}
	}

	/**
	 * Displays the usage information for running the application.
	 */
	public static void displayUsage() {
		System.out.println("Usage:");
		System.out.println("============================================");
		System.out.println("Run Service as REST-Service");
		System.out.println("============================================");
		System.out.println("Parm 0: application-properties-file");
		System.out.println("Parm 1: log4j-config-file");
		System.out.println("============================================");
		System.out.println("Run Service from console");
		System.out.println("============================================");
		System.out.println("Parm 0: application-properties-file");
		System.out.println("Parm 1: log4j-config-file");
		System.out.println("Parm 2: application/xml or application/json");
		System.out.println("Parm 3: JSON Query-Objects {}");
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
}
