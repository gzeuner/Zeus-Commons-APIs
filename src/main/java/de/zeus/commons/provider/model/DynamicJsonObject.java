package de.zeus.commons.provider.model;

import java.util.HashMap;
import java.util.Map;

public class DynamicJsonObject {
    private final Map<String, Object> attributes = new HashMap<>();
    private final Map<String, MetaData> metadata = new HashMap<>();

    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public void addMetaData(String key, MetaData metaData) {
        metadata.put(key, metaData);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public MetaData getMetaData(String key) {
        return metadata.get(key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DynamicJsonObject{");
        sb.append("attributes=").append(attributes);
        sb.append(", metadata=").append(metadata);
        sb.append('}');
        return sb.toString();
    }

    public String toPrettyString() {
        return toPrettyString(0);
    }

    private String toPrettyString(int indentation) {
        StringBuilder sb = new StringBuilder();
        String indent = " ";
        for (int i = 0; i < indentation * 2; i++) {
            indent += " ";
        }

        sb.append(indent).append("{\n");

        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            sb.append(indent).append("  ").append(entry.getKey()).append(": ");
            if (entry.getValue() instanceof DynamicJsonObject) {
                sb.append(((DynamicJsonObject) entry.getValue()).toPrettyString(indentation + 1));
            } else {
                sb.append(entry.getValue());
            }
            sb.append(",\n");
        }

        for (Map.Entry<String, MetaData> entry : metadata.entrySet()) {
            sb.append(indent).append("  ").append(entry.getKey()).append(": ").append(entry.getValue()).append(",\n");
        }

        sb.append(indent).append("}\n");
        return sb.toString();
    }
}
