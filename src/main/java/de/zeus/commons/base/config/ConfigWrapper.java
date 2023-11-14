package de.zeus.commons.base.config;

import com.google.gson.JsonObject;

/**
 * The ConfigWrapper class encapsulates the configuration settings for the application.
 * It holds the paths to various configuration files and other settings necessary for
 * initializing and running different components of the application.
 */
public class ConfigWrapper {

    private String jdbcConfig;
    private String sparkConfig;
    private String jsonClientConfig;
    private String mode;
    private JsonObject jsonRequest;

    /**
     * Gets the JDBC configuration file path.
     *
     * @return The JDBC configuration file path.
     */
    public String getJdbcConfig() {
        return jdbcConfig;
    }

    /**
     * Sets the JDBC configuration file path.
     *
     * @param jdbcConfig The JDBC configuration file path to set.
     */
    public void setJdbcConfig(String jdbcConfig) {
        this.jdbcConfig = jdbcConfig;
    }


    /**
     * Gets the operation mode of the application.
     *
     * @return The operation mode as a String.
     */
    public String getMode() {
        return mode;
    }

    /**
     * Sets the operation mode of the application.
     *
     * @param mode The operation mode to set.
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Gets the JSON request object.
     *
     * @return The JSON request object.
     */
    public JsonObject getJsonRequest() {
        return jsonRequest;
    }

    /**
     * Sets the JSON request object.
     *
     * @param jsonRequest The JSON request object to set.
     */
    public void setJsonRequest(JsonObject jsonRequest) {
        this.jsonRequest = jsonRequest;
    }

    /**
     * Gets the Spark configuration file path.
     *
     * @return The Spark configuration file path.
     */
    public String getSparkConfig() {
        return sparkConfig;
    }

    /**
     * Sets the Spark configuration file path.
     *
     * @param sparkConfig The Spark configuration file path to set.
     */
    public void setSparkConfig(String sparkConfig) {
        this.sparkConfig = sparkConfig;
    }

    /**
     * Gets the JsonClient configuration file path.
     *
     * @return The JsonClient configuration file path.
     */
    public String getJsonClientConfig() {
        return jsonClientConfig;
    }

    /**
     * Sets the JsonClient configuration file path.
     *
     * @param jsonClientConfig The JsonClient configuration file path to set.
     */
    public void setJsonClientConfig(String jsonClientConfig) {
        this.jsonClientConfig = jsonClientConfig;
    }
}
