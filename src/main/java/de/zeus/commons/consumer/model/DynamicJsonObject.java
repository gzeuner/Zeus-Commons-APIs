package de.zeus.commons.consumer.model;

import de.zeus.commons.provider.model.MetaData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * The DynamicJsonObject class is designed to store and manage dynamic JSON data.
 * It allows for flexible manipulation of attributes and metadata in a structured format.
 */
public class DynamicJsonObject {

    private static final Log LOG = LogFactory.getLog(DynamicJsonObject.class);

    private final Map<String, Object> attributes = new HashMap<>();
    private final Map<String, MetaData> metadata = new HashMap<>();

    /**
     * Adds or updates an attribute in the dynamic JSON object.
     *
     * @param key   The attribute key.
     * @param value The attribute value.
     */
    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
        LOG.info("Attribute added/updated: " + key);
    }

    /**
     * Adds or updates metadata in the dynamic JSON object.
     *
     * @param key      The metadata key.
     * @param metaData The metadata value.
     */
    public void addMetaData(String key, MetaData metaData) {
        metadata.put(key, metaData);
        LOG.info("Metadata added/updated: " + key);
    }

    /**
     * Retrieves an attribute value by its key.
     *
     * @param key The attribute key.
     * @return The attribute value, or null if not found.
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * Retrieves metadata by its key.
     *
     * @param key The metadata key.
     * @return The metadata value, or null if not found.
     */
    public MetaData getMetaData(String key) {
        return metadata.get(key);
    }

    @Override
    public String toString() {
        return "DynamicJsonObject{" +
                "attributes=" + attributes +
                ", metadata=" + metadata +
                '}';
    }

    /**
     * Generates a pretty-printed string representation of the dynamic JSON object.
     *
     * @return The pretty-printed string representation.
     */
    public String toPrettyString() {
        return toPrettyString(0);
    }

    private String toPrettyString(int indentation) {
        StringBuilder sb = new StringBuilder();
        String indent = getIndentString(indentation * 2);

        sb.append(indent).append("{\n");

        attributes.forEach((key, value) -> {
            sb.append(indent).append("  ").append(key).append(": ");
            if (value instanceof DynamicJsonObject) {
                sb.append(((DynamicJsonObject) value).toPrettyString(indentation + 1));
            } else {
                sb.append(value);
            }
            sb.append(",\n");
        });

        metadata.forEach((key, value) -> {
            sb.append(indent).append("  ").append(key).append(": ").append(value).append(",\n");
        });

        sb.append(indent).append("}\n");
        return sb.toString();
    }

    /**
     * Helper method to create a string of spaces for indentation.
     *
     * @param count The number of spaces.
     * @return A string consisting of 'count' spaces.
     */
    private String getIndentString(int count) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < count; i++) {
            indent.append(" ");
        }
        return indent.toString();
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public Map<String, MetaData> getMetadata() {
        return metadata;
    }

    /**
     * Generates a unique identifier for this dynamic JSON object.
     *
     * @return A unique identifier as a String.
     */
    public String getId() {
        return UniqueIdGenerator.generateUniqueId(this);
    }
}
