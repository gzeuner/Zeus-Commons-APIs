package de.zeus.commons.base.config;

/**
 * The `JsonClientConfig` class is responsible for configuring settings related to the JsonClient
 * and managing its properties. It extends the `ConfigBase` class to leverage its configuration loading
 * and property management capabilities.
 */
public class JsonClientConfig extends ConfigBase {

    /**
     * The single instance of the `JsonClientConfig` class.
     */
    private static final JsonClientConfig config = new JsonClientConfig();

    /**
     * Path to the properties file associated with this configuration.
     */
    private String propertiesFile;

    /**
     * The service URL for the JSON Client.
     */
    private String serviceUrl;

    /**
     * The service Requet JSON.
     */
    private String serviceRequestJson;

    /**
     * The path to the KeyStore.
     */
    private String insertEavData;

    /**
     * The password for the KeyStore.
     */
    private String insertEavMetaData;

    /**
     * Private constructor to enforce a singleton pattern and specify the properties file path.
     */
    private JsonClientConfig() {
        super("config/jsonclient.properties");
    }

    /**
     * Returns the singleton instance of the `JsonClientConfig` class.
     *
     * @return The instance of `JsonClientConfig`.
     */
    public static JsonClientConfig getInstance() {
        return config;
    }

    /**
     * Performs initial tasks such as reading and setting configuration properties for JSON Client.
     */
    @Override
    public void loadProperties() {
        if (readPropertiesFile()) {
            setServiceUrl(getProperty("service.url"));
            setServiceRequestJson(getProperty("service.request.json"));
            setInsertEavData(getProperty("client.insert.data"));
            setInsertEavMetaData(getProperty("client.insert.metadata"));
        }
    }

    /**
     * Retrieves the path to the properties file associated with this configuration.
     *
     * @return The path to the properties file.
     */
    public String getPropertiesFile() {
        return propertiesFile;
    }

    /**
     * Sets the service URL for the JSON Client.
     *
     * @param serviceUrl The service URL to set.
     */
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    /**
     * Retrieves the service URL for the JSON Client.
     *
     * @return The service URL for the JSON Client.
     */
    public String getServiceUrl() {
        return serviceUrl;
    }

    /**
     * Sets the path to the properties file associated with this configuration.
     *
     * @param propertiesFile The path to the properties file.
     */
    public void setPropertiesFile(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }

    /**
     * Retrieves the location of the KeyStore file.
     *
     * @return The location of the KeyStore file.
     */
    public String getInsertEavData() {
        return insertEavData;
    }

    /**
     * Sets the location of the KeyStore file.
     *
     * @param keyStoreLocation The location of the KeyStore file.
     */
    public void setInsertEavData(String keyStoreLocation) {
        this.insertEavData = keyStoreLocation;
    }

    /**
     * Retrieves the password for the KeyStore.
     *
     * @return The password for the KeyStore.
     */
    public String getInsertEavMetaData() {
        return insertEavMetaData;
    }

    /**
     * Sets the password for the KeyStore.
     *
     * @param insertEavMetaData The password for the KeyStore.
     */
    public void setInsertEavMetaData(String insertEavMetaData) {
        this.insertEavMetaData = insertEavMetaData;
    }

    /**
     * Retrieves JSON-String containing sql queries.
     *
     * @return The Json containing request data
     */
    public String getServiceRequestJson() {
        return serviceRequestJson;
    }

    /**
     * Sets the JSON-String containing sql queries.
     *
     * @param serviceRequestJson The Json containing request data
     */
    public void setServiceRequestJson(String serviceRequestJson) {
        this.serviceRequestJson = serviceRequestJson;
    }
}
