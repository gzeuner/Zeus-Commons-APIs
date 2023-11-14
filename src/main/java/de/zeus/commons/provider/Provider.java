package de.zeus.commons.provider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.zeus.commons.base.config.ConfigWrapper;
import de.zeus.commons.base.interfaces.IConnectionController;
import de.zeus.commons.base.config.JdbcConfig;
import de.zeus.commons.base.config.SparkConfig;
import de.zeus.commons.file.FileUtils;
import de.zeus.commons.provider.logic.sql.ConnectionControllerFactory;
import de.zeus.commons.provider.service.HttpServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Provider class serves as the entry point for running the application as a REST service or from the console.
 * It handles command-line arguments, initializes the necessary components, and starts the service.
 */
public class Provider {

    private static final Log LOG = LogFactory.getLog(Provider.class) ;

    public static void main(String[] args) {

        ConfigWrapper config = parseArgs(args);
        Provider.initConfig(config);
        ConnectionControllerFactory controllerFactory = new ConnectionControllerFactory();

        if (config.getMode() != null) {
            initServiceFromConsole(controllerFactory, config.getJsonRequest(), config.getMode());
        } else {
            initService(controllerFactory);
        }

    }

    public static ConfigWrapper parseArgs(String[] args) {

        ConfigWrapper config = new ConfigWrapper();
        try {
            String runMode = args[0];
            if (runMode.equals("REST")) {
                config.setJdbcConfig(args[1]);
                config.setSparkConfig(args[2]);
            } else if (runMode.equals("CONSOLE")) {
                config.setJdbcConfig(args[1]);
                config.setJsonRequest(new Gson().fromJson(args[2], JsonObject.class));
                config.setMode(args[3]);
            } else {
                displayUsage();
                System.exit(0);
            }
        }catch (ArrayIndexOutOfBoundsException e) {
            displayUsage();
            System.exit(0);
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
        System.out.println("Parm 1: REST");
        System.out.println("Parm 2: <jdbc-properties-file>");
        System.out.println("Parm 3: <spark-properties-file>");
        System.out.println("============================================");
        System.out.println("Run Service from console");
        System.out.println("============================================");
        System.out.println("Parm 1: CONSOLE");
        System.out.println("Parm 2: <jdbc-properties-file>");
        System.out.println("Parm 3: <JSON Query-Objects {}>");
        System.out.println("Parm 4: <application/xml or application/json>");
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
        FileUtils fileUtils = new FileUtils();
        try {
            fileUtils.writeContentToFile(String.valueOf(connectionController.process(jsonRequest, mode)), mode);
        } catch (Exception e) {
            LOG.error("IO-File or Format Error. ", e);
        }
    }

    private static void initConfig(ConfigWrapper config) {

        if(config.getJdbcConfig() != null) {
            LOG.debug("Loading JdbcConfig");
            JdbcConfig jdbcConfig = JdbcConfig.getInstance();
            jdbcConfig.setPropertiesFile(config.getJdbcConfig());
            jdbcConfig.loadProperties();
        }

        if(config.getSparkConfig() != null) {
            LOG.debug("Loading SparkConfig");
            SparkConfig sparkConfig = SparkConfig.getInstance();
            sparkConfig.setPropertiesFile(config.getSparkConfig());
            sparkConfig.loadProperties();
        }
    }
}
