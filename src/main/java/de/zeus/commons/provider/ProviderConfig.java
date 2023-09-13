package de.zeus.commons.provider;

import com.google.gson.JsonObject;

public class ProviderConfig {

    private String jdbcConfig;
    private String sparkConfig;
    private String logConfig;
    private String mode;
    private JsonObject jsonRequest;

    public String getJdbcConfig() {
        return jdbcConfig;
    }

    public void setJdbcConfig(String jdbcConfig) {
        this.jdbcConfig = jdbcConfig;
    }

    public String getLogConfig() {
        return logConfig;
    }

    public void setLogConfig(String logConfig) {
        this.logConfig = logConfig;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public JsonObject getJsonRequest() {
        return jsonRequest;
    }

    public void setJsonRequest(JsonObject jsonRequest) {
        this.jsonRequest = jsonRequest;
    }

    public String getSparkConfig() {
        return sparkConfig;
    }

    public void setSparkConfig(String sparkConfig) {
        this.sparkConfig = sparkConfig;
    }
}
