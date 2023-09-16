package de.zeus.commons.provider.convert;

import de.zeus.commons.provider.constants.IProviderConstants;
import de.zeus.commons.provider.model.ContentFieldData;
import de.zeus.commons.provider.model.ContentRecordData;
import de.zeus.commons.provider.model.DataWrapper;
import de.zeus.commons.provider.model.MetaData;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

import java.util.List;

/**
 * A class to convert a list of {@code DataWrapper} objects into an XML representation.
 */
public class DataToXML {

    private final List<DataWrapper> dataWrapperList;
    private final Element rootElement;
    private final Element contentElement;
    private final boolean includeMetaData;

    /**
     * Constructs an instance of {@code DataToXML}.
     *
     * @param dataWrappers    The list of DataWrapper objects to convert.
     * @param includeMetaData Whether to include metadata in the XML.
     */
    public DataToXML(List<DataWrapper> dataWrappers, boolean includeMetaData) {
        this.dataWrapperList = dataWrappers;
        this.includeMetaData = includeMetaData;

        this.rootElement = new Element("result");
        this.contentElement = new Element("contentData");

        this.rootElement.addContent(contentElement);
        addContentDataToRoot();
    }

    /**
     * Adds content data to the root element.
     */
    private void addContentDataToRoot() {
        for (DataWrapper dataWrapper : dataWrapperList) {
            addContentDataForWrapper(dataWrapper);
        }
    }

    /**
     * Adds content data for a single DataWrapper.
     *
     * @param dataWrapper The DataWrapper to process.
     */
    private void addContentDataForWrapper(DataWrapper dataWrapper) {
        Element wrapperElement = new Element(dataWrapper.getName());
        contentElement.addContent(wrapperElement);
        populateWrapperElement(dataWrapper, wrapperElement);
    }

    /**
     * Populates the wrapper element with content records.
     *
     * @param dataWrapper    The DataWrapper to process.
     * @param wrapperElement The wrapper element to populate.
     */
    private void populateWrapperElement(DataWrapper dataWrapper, Element wrapperElement) {
        for (ContentRecordData contentRecord : dataWrapper.getContentData()) {
            Element recordElement = new Element("entity");
            wrapperElement.addContent(recordElement);
            populateRecordElement(contentRecord, dataWrapper, recordElement);
        }
    }

    /**
     * Populates the record element with field data.
     *
     * @param contentRecord The ContentRecordData to process.
     * @param dataWrapper   The parent DataWrapper.
     * @param recordElement The record element to populate.
     */
    private void populateRecordElement(ContentRecordData contentRecord, DataWrapper dataWrapper, Element recordElement) {
        for (ContentFieldData contentField : contentRecord.getFieldDataObjs()) {
            MetaData metaData = dataWrapper.getMetaData().get(contentField.getColumnNumber());
            Element fieldElement = new Element(metaData.getColumnName());
            fieldElement.setText(contentField.getValue());
            recordElement.addContent(fieldElement);

            if (includeMetaData) {
                Element metaDataElement = createMetaDataElement(metaData);
                fieldElement.addContent(metaDataElement);
            }
        }
        addContentDataToChildElement(contentRecord, recordElement);
    }

    /**
     * Creates an XML element to represent the metadata.
     *
     * @param metaData The MetaData to convert.
     * @return An XML element representing the metadata.
     */
    private Element createMetaDataElement(MetaData metaData) {
        Element metaDataElement = new Element(IProviderConstants.META_DATA_SUFFIX);

        addSimpleElement(metaDataElement, IProviderConstants.COLUMN_NUMBER, String.valueOf(metaData.getColumnNumber()));
        addSimpleElement(metaDataElement, IProviderConstants.IS_PRIMARY_KEY, String.valueOf(metaData.isPrimaryKey()));
        addSimpleElement(metaDataElement, IProviderConstants.DATA_TYPE_NAME, metaData.getColumnSqlDataTypeName());
        addSimpleElement(metaDataElement, IProviderConstants.DATA_TYPE_ID, String.valueOf(metaData.getColumnSqlDataType()));
        addSimpleElement(metaDataElement, IProviderConstants.CLASS_NAME, metaData.getColumnClassName());

        return metaDataElement;
    }

    /**
     * Adds a simple XML element with text content to a parent element.
     *
     * @param parentElement The parent element.
     * @param elementName   The name of the element to add.
     * @param textContent   The text content of the element.
     */
    private void addSimpleElement(Element parentElement, String elementName, String textContent) {
        Element childElement = new Element(elementName);
        childElement.setText(textContent);
        parentElement.addContent(childElement);
    }

    /**
     * Adds content data to a child element.
     *
     * @param contentRecord The ContentRecordData to process.
     * @param parentElement The parent XML element.
     */
    private void addContentDataToChildElement(ContentRecordData contentRecord, Element parentElement) {
        for (DataWrapper childWrapper : contentRecord.getDataWrappers()) {
            Element childWrapperElement = new Element(childWrapper.getName());
            parentElement.addContent(childWrapperElement);

            populateWrapperElement(childWrapper, childWrapperElement);
        }
    }

    /**
     * Gets the XML representation as a String.
     *
     * @return The XML as a String.
     */
    public String getResult() {
        return new XMLOutputter().outputString(new Document(this.rootElement));
    }
}
