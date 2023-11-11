package de.zeus.commons.consumer.service;

import de.zeus.commons.consumer.convert.JSONProcessor;
import de.zeus.commons.file.FileUtils;
import de.zeus.commons.provider.model.DynamicJsonObject;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static de.zeus.commons.base.constants.IConstants.MODE_JSON;

public class JsonClient {

    private static final Logger LOG = Logger.getLogger(JsonClient.class.getName());

    public static void main(String[] args) {
        if (args.length < 2) {
            LOG.log(Level.SEVERE,"Missing arguments. Please enter the file name and the service URL as arguments.");
            return;
        }

        String serviceUrl = args[0];
        String filePath = args[1];

        URL url;
        try {
            url = new URL(serviceUrl);
        } catch (MalformedURLException e) {
            LOG.log(Level.SEVERE,"Malformed URL: " + serviceUrl, e);
            return;
        }

        FileUtils fileUtils = new FileUtils();
        try {
            String jsonInputString = fileUtils.readFileToString(filePath);
            JsonClient client = new JsonClient();
            client.sendRequest(url, jsonInputString);
        } catch (IOException e) {
            LOG.log(Level.SEVERE,"Error reading the file:" + filePath, e);
        }
    }

    public void sendRequest(URL url, String jsonInputString) {
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
