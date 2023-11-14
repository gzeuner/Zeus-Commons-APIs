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
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.zeus.commons.base.constants.IConstants.MODE_JSON;

public class JsonClient {

    private static final Logger LOG = Logger.getLogger(JsonClient.class.getName());
    private final JdbcOperations jdbcOperations;

    public JsonClient(JdbcOperations jdbcOperation) {
        this.jdbcOperations = jdbcOperation;
    }

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
            } catch (SQLException | ProcessingException throwables) {
                LOG.log(Level.SEVERE,"Error inserting DynJsonData.");
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE,"Error sending the request to " + url, e);
        } finally {
            if (conn != null) {
                conn.disconnect();
                LOG.info("Connection disconnected from " + url);
            }
        }
    }
}
