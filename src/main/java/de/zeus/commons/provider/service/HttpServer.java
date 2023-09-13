package de.zeus.commons.provider.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.zeus.commons.connector.jdbc.config.JdbcConfig;
import de.zeus.commons.provider.config.IProviderConstants;
import de.zeus.commons.provider.logic.sql.ConnectionControllerFactory;

/**
 * The HttpServer class provides a simple HTTP server for processing JSON and XML requests.
 * It registers routes for handling different modes (JSON and XML) and delegates request processing
 * to the appropriate controller.
 */
public class HttpServer implements IProviderConstants {

	private final ConnectionControllerFactory controllerFactory;
	private final Gson gson;
	private final JdbcConfig config = JdbcConfig.getInstance();

	/**
	 * Initializes a new instance of the HttpServer class with the provided controller factory.
	 *
	 * @param controllerFactory The factory for creating connection controllers.
	 */
	public HttpServer(ConnectionControllerFactory controllerFactory) {
		this.controllerFactory = controllerFactory;
		this.gson = new Gson();
		initSpark();
		registerRoutes();
	}

	private void initSpark() {
		spark.Spark.ipAddress(config.getSparkJavaAllowedHosts());
		spark.Spark.port(config.getSparkJavaPort());
	}

	/**
	 * Registers the default routes for JSON and XML processing.
	 */
	public void registerRoutes() {
		registerRoute("/get_json", MODE_JSON);
		registerRoute("/get_xml", MODE_XML);
	}

	/**
	 * Registers a route for the specified mode (JSON or XML) that handles incoming requests.
	 *
	 * @param route The route path to register.
	 * @param mode  The processing mode (JSON or XML) for this route.
	 */
	public void registerRoute(String route, String mode) {
		spark.Spark.post(route, (req, res) -> {
			res.type(mode);
			return controllerFactory.getController().process(gson.fromJson(req.body(), JsonObject.class), mode);
		});
	}
}
