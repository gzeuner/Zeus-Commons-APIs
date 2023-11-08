package de.zeus.commons.consumer.convert;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.zeus.commons.provider.model.DynamicJsonObject;
import de.zeus.commons.provider.model.MetaData;

public class JSONProcessor {

    public DynamicJsonObject processJson(String json) {
        JsonElement element = JsonParser.parseString(json);
        JsonObject rootObject = element.getAsJsonObject();
        JsonObject contentDataObject = rootObject.getAsJsonObject("contentData");

        return processNode(contentDataObject);
    }

    private DynamicJsonObject processNode(JsonObject jsonObject) {
        DynamicJsonObject obj = new DynamicJsonObject();

        for (String key : jsonObject.keySet()) {
            JsonElement element = jsonObject.get(key);

            if (key.endsWith("_metadata")) {
                MetaData metaData = processMetaData(jsonObject, key);
                obj.addMetaData(key, metaData);
            } else if (element.isJsonObject()) {
                DynamicJsonObject childObj = processNode(element.getAsJsonObject());
                obj.addAttribute(key, childObj);
            } else {
                obj.addAttribute(key, element.getAsString());
            }
        }

        return obj;
    }

    private MetaData processMetaData(JsonObject jsonObject, String key) {
        JsonObject metadataJson = jsonObject.getAsJsonObject(key);
        MetaData metaData = new MetaData();

        metaData.setColumnNumber(metadataJson.get("columnNumber").getAsInt());
        metaData.setColumnName(key.replace("_metadata", ""));
        metaData.setColumnSqlDataType(metadataJson.get("dataTypeId").getAsInt());
        metaData.setColumnSqlDataTypeName(metadataJson.get("dataTypeName").getAsString());
        metaData.setColumnClassName(metadataJson.get("className").getAsString());
        metaData.setPrimaryKey(metadataJson.get("isPrimaryKey").getAsBoolean());

        return metaData;
    }
}
