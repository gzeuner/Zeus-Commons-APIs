package de.zeus.commons.connector.jdbc.config;

import de.zeus.commons.provider.Provider;

/**
 * The `SparkConfig` class is responsible for configuring settings related to the Spark Java web framework
 * and managing its properties. It extends the `ConfigBase` class to leverage its configuration loading
 * and property management capabilities.
 */
public class SparkConfig extends ConfigBase {

    /**
     * The single instance of the `SparkConfig` class.
     */
    private static SparkConfig config = new SparkConfig();

    /**
     * Path to the properties file associated with this configuration.
     */
    private String propertiesFile;

    /**
     * The port on which the Spark Java server should listen.
     */
    private int sparkJavaPort;

    /**
     * The allowed hosts for the Spark Java server.
     */
    private String sparkJavaAllowedHosts;

    /**
     * Private constructor to enforce a singleton pattern and specify the properties file path.
     */
    private SparkConfig() {
        super("config/spark.properties");
    }

    /**
     * Returns the singleton instance of the `SparkConfig` class.
     *
     * @return The `SparkConfig` instance.
     */
    public static SparkConfig getInstance() {
        return config;
    }

    /**
     * Performs initial tasks such as reading and setting Spark Java configuration properties.
     */
    @Override
    public void loadProperties() {
        if (readPropertiesFile()) {
            setSparkJavaPort(getProperty("spark.port"));
            setSparkJavaAllowedHosts(getProperty("spark.host"));
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
     * Sets the Spark Java server's port based on the provided value, falling back to the default port
     * if the provided value is not numeric.
     *
     * @param sparkJavaPort The Spark Java server port to set.
     */
    public void setSparkJavaPort(String sparkJavaPort) {
        try {
            this.sparkJavaPort = Integer.parseInt(sparkJavaPort);
        } catch (NumberFormatException e) {
            this.sparkJavaPort = 4567;
            LOG.info("The Spark-Java port must be a numeric value. Using default port: [4567]");
        }
    }

    /**
     * Retrieves the port on which the Spark Java server should listen.
     *
     * @return The Spark Java server port.
     */
    public int getSparkJavaPort() {
        return sparkJavaPort;
    }

    /**
     * Sets the allowed hosts for the Spark Java server.
     *
     * @param sparkJavaAllowedHosts The allowed hosts to set.
     */
    public void setSparkJavaAllowedHosts(String sparkJavaAllowedHosts) {
        this.sparkJavaAllowedHosts = sparkJavaAllowedHosts;
    }

    /**
     * Retrieves the allowed hosts for the Spark Java server.
     *
     * @return The allowed hosts for the Spark Java server.
     */
    public String getSparkJavaAllowedHosts() {
        return sparkJavaAllowedHosts;
    }

    /**
     * Sets the path to the properties file associated with this configuration.
     *
     * @param propertiesFile The path to the properties file.
     */
    public void setPropertiesFile(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }
}
