package de.zeus.commons.consumer;

import de.zeus.commons.base.config.ConfigWrapper;
import de.zeus.commons.base.config.JdbcConfig;
import de.zeus.commons.base.config.JsonClientConfig;
import de.zeus.commons.connector.jdbc.JdbcOperations;
import de.zeus.commons.consumer.service.JsonClient;
import de.zeus.commons.file.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The consumer class serves as the entry point for client operation of the system.
 * It processes command line arguments and starts the client process.
 */
public class Consumer {

    private static final Log LOG = LogFactory.getLog(Consumer.class);

    public static void main(String[] args) {
        ConfigWrapper config = parseArgs(args);
        if (config == null) {
            return;
        }

        initConfig(config);

        // Load the JSON template from the properties and send the request
        JsonClientConfig jsonClientConfig = JsonClientConfig.getInstance();
        String jsonRequestFilePath = jsonClientConfig.getServiceRequestJson();
        sendJsonRequest(jsonClientConfig, jsonRequestFilePath);
    }

    private static ConfigWrapper parseArgs(String[] args) {
        if (args.length != 3 || !"JSON_EAV".equals(args[0])) {
            LOG.error("Invalid arguments. Usage: JSON_EAV <jdbc-properties-file> <json-client-config-file>");
            return null;
        }
        ConfigWrapper config = new ConfigWrapper();
        config.setJdbcConfig(args[1]);
        config.setJsonClientConfig(args[2]);
        return config;
    }

    private static void initConfig(ConfigWrapper config) {
        if (config.getJdbcConfig() != null) {
            JdbcConfig jdbcConfig = JdbcConfig.getInstance();
            jdbcConfig.setPropertiesFile(config.getJdbcConfig());
            jdbcConfig.loadProperties();
            LOG.info("JDBC Configuration loaded.");
        }

        if (config.getJsonClientConfig() != null) {
            JsonClientConfig jsonClientConfig = JsonClientConfig.getInstance();
            jsonClientConfig.setPropertiesFile(config.getJsonClientConfig());
            jsonClientConfig.loadProperties();
            LOG.info("JsonClient Configuration loaded.");
        }
    }

    private static void sendJsonRequest(JsonClientConfig jsonClientConfig, String jsonRequestFilePath) {
        try {
            JsonClient jsonClient = new JsonClient(new JdbcOperations());
            FileUtils fileUtils = new FileUtils();
            String jsonRequest = fileUtils.readFileToString(jsonRequestFilePath);
            jsonClient.sendRequestToService(new URL(jsonClientConfig.getServiceUrl()), jsonRequest);
            LOG.info("Request sent with JSON from " + jsonRequestFilePath);
        } catch (IOException e) {
            LOG.error("Error processing JSON request from file: " + jsonRequestFilePath, e);
        }
    }

}
