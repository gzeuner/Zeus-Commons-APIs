package de.zeus.commons.base.constants;

/**
 * This interface defines constants used in configuration and data processing.
 */
public interface IConstants {

    /**
     * Mode for XML output format.
     */
    String MODE_XML = "application/xml";

    /**
     * Mode for JSON output format.
     */
    String MODE_JSON = "application/json";

    /**
     * Key for specifying column number in metadata.
     */
    String COLUMN_NUMBER = "columnNumber";

    /**
     * Key for indicating if a field is a primary key in metadata.
     */
    String IS_PRIMARY_KEY = "isPrimaryKey";

    /**
     * Key for specifying data type name in metadata.
     */
    String DATA_TYPE_NAME = "dataTypeName";

    /**
     * Key for specifying data type ID in metadata.
     */
    String DATA_TYPE_ID = "dataTypeId";

    /**
     * Key for specifying class name in metadata.
     */
    String CLASS_NAME = "className";

    /**
     * Suffix for metadata keys.
     */
    String META_DATA_SUFFIX = "_metadata";

    /**
     * Key for specifying contentData.
     */
    String CONTENT_DATA = "contentData";

}
