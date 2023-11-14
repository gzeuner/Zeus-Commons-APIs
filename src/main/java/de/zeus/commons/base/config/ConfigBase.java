package de.zeus.commons.base.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This abstract class serves as the base for configuration classes. It provides
 * methods for reading properties from a file, initializing properties, and
 * creating default folders. Subclasses should implement the {@code loadProperties}
 * method to define how properties are loaded and set.
 */
public abstract class ConfigBase {

    public static final Logger LOG = LogManager.getLogger(ConfigBase.class);

    /**
     * The properties container for storing configuration properties.
     */
    protected Properties properties = new Properties();

    /**
     * The path to the properties file.
     */
    private String propertiesFile;

    /**
     * Constructs a new ConfigBase instance with the specified properties file path.
     *
     * @param propertiesFile The path to the properties file.
     */
    public ConfigBase(String propertiesFile) {
        this.propertiesFile = propertiesFile;
        init();
    }

    /**
     * Initializes the configuration by creating default folders.
     */
    protected void init() {
        createDefaultFolder();
    }

    /**
     * Reads properties from the specified file or classpath resource and populates
     * the properties container.
     *
     * @return {@code true} if the properties were successfully loaded, {@code false} otherwise.
     */
    public boolean readPropertiesFile() {
        boolean fileOk = true;
        InputStream inputStream = null;
        File propFile = new File(getPropertiesFile());
        try {
            if (propFile.exists()) {
                inputStream = new FileInputStream(propFile);
            } else {
                inputStream = getClass().getClassLoader().getResourceAsStream(getPropertiesFile());
                if (inputStream == null) {
                    LOG.error("Property file '" + getPropertiesFile() + "' not found in the classpath");
                    fileOk = false;
                    return fileOk;
                }
            }

            try (InputStream is = inputStream) {
                properties.load(is);
            }
        } catch (IOException e) {
            LOG.error("Failed to load properties", e);
            fileOk = false;
        }
        return fileOk;
    }

    /**
     * Subclasses should implement this method to load properties from the
     * configuration source and set them.
     */
    public abstract void loadProperties();

    /**
     * Creates a default folder if it does not exist.
     */
    private void createDefaultFolder() {
        File f = new File("./logs");
        if (!f.exists()) {
            f.mkdir();
        }
    }

    /**
     * Retrieves the path to the properties file.
     *
     * @return The properties file path.
     */
    public String getPropertiesFile() {
        return propertiesFile;
    }

    /**
     * Retrieves a property value for the given key.
     *
     * @param key The key of the property.
     * @return The property value, or "NOT SET" if the property is not found.
     */
    public String getProperty(String key) {
        return properties.getProperty(key, "NOT SET");
    }

    /**
     * Sets the properties file path.
     *
     * @param propertiesFile The new properties file path.
     */
    public void setPropertiesFile(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }
}
