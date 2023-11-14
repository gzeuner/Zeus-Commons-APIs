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

        String jsonRequestFilePath = args[3]; // The path of the JSON request file

        // Get service URL from JsonClientConfig
        String serviceUrl = JsonClientConfig.getInstance().getServiceUrl();
        URL url;
        try {
            url = new URL(serviceUrl);
        } catch (MalformedURLException e) {
            LOG.error("Malformed URL: " + serviceUrl, e);
            return;
        }

        JsonClient jsonClient = new JsonClient(new JdbcOperations());
        FileUtils fileUtils = new FileUtils();
        String jsonRequest;
        try {
            jsonRequest = fileUtils.readFileToString(jsonRequestFilePath);
        } catch (IOException e) {
            LOG.error("Error reading File: " + jsonRequestFilePath, e);
            return;
        }
        jsonClient.sendRequestToService(url, jsonRequest);
    }

    private static ConfigWrapper parseArgs(String[] args) {
        if (args.length != 4 || !"JSON_EAV".equals(args[0])) {
            LOG.error("Invalid arguments. Usage: JSON_EAV <jdbc-properties-file> <json-client-config-file> <json-request-file>");
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
}
