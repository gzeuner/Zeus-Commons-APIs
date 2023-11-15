package de.zeus.commons.consumer.service;

import de.zeus.commons.connector.jdbc.JdbcOperations;
import de.zeus.commons.connector.jdbc.ProcessingException;
import de.zeus.commons.consumer.convert.JSONProcessor;
import de.zeus.commons.consumer.model.DynamicJsonObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static de.zeus.commons.base.constants.IConstants.MODE_JSON;

/**
 * The JsonClient class is responsible for sending JSON requests to a specified service URL
 * and processing the JSON response. It uses JdbcOperations to interact with the database.
 */
public class JsonClient {

    private static final Log LOG = LogFactory.getLog(JsonClient.class) ;

    private final JdbcOperations jdbcOperations;

    /**
     * Constructs a JsonClient with specified JDBC operations.
     *
     * @param jdbcOperation The JDBC operations to be used for database interactions.
     */
    public JsonClient(JdbcOperations jdbcOperation) {
        this.jdbcOperations = jdbcOperation;
    }

    /**
     * Sends a JSON request to a specified URL and processes the response.
     * The processed JSON data is then saved in the database.
     *
     * @param url The URL to which the request is sent.
     * @param jsonInputString The JSON string to be sent as a request.
     */
    public void sendRequestToService(URL url, String jsonInputString) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", MODE_JSON);
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
                LOG.info("Request sent to: " + url);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String jsonResponse = br.lines().map(String::trim).collect(Collectors.joining());
                JSONProcessor processor = new JSONProcessor();
                DynamicJsonObject result = processor.processJson(jsonResponse);

                LOG.info("Processed JSON:");
                LOG.info(result.toPrettyString());

                // Save the processed JSON data in the database
                jdbcOperations.insertDynamicJsonObject(result);
                LOG.info("JSON data inserted successfully into the database.");
            } catch (SQLException | ProcessingException e) {
                LOG.error( "Error processing or inserting JSON data into the database.", e);
            }
        } catch (IOException e) {
            LOG.error("Error sending the request to " + url, e);
        } finally {
            if (conn != null) {
                conn.disconnect();
                LOG.info("Disconnected from URL: " + url);
            }
        }
    }
}
