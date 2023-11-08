package de.zeus.commons.consumer.service;

import de.zeus.commons.consumer.convert.JSONProcessor;
import de.zeus.commons.provider.model.DynamicJsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class JsonClient {

    public static void main(String[] args) {
        JsonClient client = new JsonClient();
        client.sendRequest();
    }

    public void sendRequest() {

        try {
            URL url = new URL("http://localhost:4567/json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{"
                    + "\"query\": {"
                    + "    \"name\": \"agents\","
                    + "    \"statement\": \"select * from agents\","
                    + "    \"subquery\": {"
                    + "        \"name\": \"orders\","
                    + "        \"statement\": \"select * from orders where agent_code = '[$agent_code]'\","
                    + "        \"subquery\": {"
                    + "            \"name\": \"customers\","
                    + "            \"statement\": \"select * from customers where cust_code = '[$cust_code]'\","
                    + "            \"subquery\": {"
                    + "                \"name\": \"revenue\","
                    + "                \"statement\": \"select * from agent_revenue where agent_code = '[$agent_code]'\""
                    + "            }"
                    + "        }"
                    + "    }"
                    + "},"
                    + "\"includeMetadata\": false"
                    + "}";



            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                String json = response.toString();
                JSONProcessor processor = new JSONProcessor();
                DynamicJsonObject result = processor.processJson(json);

                System.out.println("Verarbeitetes JSON:");
                System.out.println(result.toPrettyString());



            }

            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
