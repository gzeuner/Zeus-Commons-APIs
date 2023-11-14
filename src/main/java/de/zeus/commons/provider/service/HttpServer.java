package de.zeus.commons.provider.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.zeus.commons.base.config.ConfigBase;
import de.zeus.commons.base.config.SparkConfig;
import de.zeus.commons.base.constants.IConstants;
import de.zeus.commons.provider.logic.sql.ConnectionControllerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Provides a simple HTTP server to handle JSON and XML requests.
 * Configures the HTTP server based on provided SparkConfig settings,
 * including optional SSL configurations.
 */
public class HttpServer implements IConstants {

	private static final Log LOG = LogFactory.getLog(HttpServer.class);
	private final ConnectionControllerFactory controllerFactory;
	private final Gson gson;
	private final SparkConfig config = SparkConfig.getInstance();

	/**
	 * Constructs a new HttpServer instance.
	 *
	 * @param controllerFactory The factory for generating connection controllers.
	 */
	public HttpServer(ConnectionControllerFactory controllerFactory) {
		this.controllerFactory = controllerFactory;
		this.gson = new Gson();
		initSpark();
		registerRoutes();
		ConfigBase.LOG.info("Service started. Waiting for Requests > ");
	}

	/**
	 * Initializes the Spark HTTP server with settings from SparkConfig.
	 */
	private void initSpark() {
		spark.Spark.ipAddress(config.getSparkJavaAllowedHosts());
		spark.Spark.port(config.getSparkJavaPort());
		configureSSL();
	}

	/**
	 * Configures SSL settings if provided in the SparkConfig.
	 */
	private void configureSSL() {
		if (shouldUseSSL()) {
			String trustStoreLocation = shouldUseTrustStore() ? config.getTrustStoreLocation() : null;
			String trustStorePassword = shouldUseTrustStore() ? config.getTrustStorePassword() : null;

			spark.Spark.secure(
					config.getKeyStoreLocation(),
					config.getKeyStorePassword(),
					trustStoreLocation,
					trustStorePassword
			);

			LOG.info("SSL is enabled.");

			if (shouldUseTrustStore()) {
				LOG.info("TrustStore is configured.");
			}
		} else {
			LOG.info("SSL is not enabled.");
		}
	}


	/**
	 * Checks if SSL should be used based on the SparkConfig settings.
	 *
	 * @return True if SSL should be used, false otherwise.
	 */
	private boolean shouldUseSSL() {
		return config.getKeyStoreLocation() != null && !config.getKeyStoreLocation().isEmpty() &&
				config.getKeyStorePassword() != null && !config.getKeyStorePassword().isEmpty();
	}

	/**
	 * Checks if a TrustStore should be used based on the SparkConfig settings.
	 *
	 * @return True if a TrustStore should be used, false otherwise.
	 */
	private boolean shouldUseTrustStore() {
		return config.getTrustStoreLocation() != null && !config.getTrustStoreLocation().isEmpty() &&
				config.getTrustStorePassword() != null && !config.getTrustStorePassword().isEmpty();
	}

	/**
	 * Registers the default routes for handling JSON and XML requests.
	 */
	public void registerRoutes() {
		registerRoute("/json", MODE_JSON);
		registerRoute("/xml", MODE_XML);
	}

	/**
	 * Registers a specific route to handle requests for a particular data format (JSON/XML).
	 *
	 * @param route The URL path for the route.
	 * @param mode  The data format (either JSON or XML).
	 */
	public void registerRoute(String route, String mode) {
		spark.Spark.post(route, (req, res) -> {
			res.type(mode);
			return controllerFactory.getController().process(gson.fromJson(req.body(), JsonObject.class), mode);
		});
	}
}
