package de.zeus.commons.provider.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a single record of data retrieved from a database query result.
 */
public class ContentRecordData {

	private int recordNumber;
	private final ArrayList<ContentFieldData> fieldDataObjs = new ArrayList<>(); // List of field data objects
	private final ArrayList<DataWrapper> dataWrappers = new ArrayList<>(); // Data wrapper objects
	private final Map<Integer, MetaData> metaDataMap = new HashMap<>();


	/**
	 * Gets the list of field data objects for this record.
	 *
	 * @return The list of field data objects.
	 */
	public ArrayList<ContentFieldData> getFieldDataObjs() {
		return fieldDataObjs;
	}

	/**
	 * Adds a field data object to this record.
	 *
	 * @param fieldDataObj The field data object to add.
	 */
	public void addContentFieldData(ContentFieldData fieldDataObj) {
		this.fieldDataObjs.add(fieldDataObj);
	}

	/**
	 * Gets the record number in the JDBC result set.
	 *
	 * @return The record number.
	 */
	public int getRecordNumber() {
		return recordNumber;
	}

	/**
	 * Sets the record number in the JDBC result set.
	 *
	 * @param recordNumber The record number to set.
	 */
	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;
	}

	/**
	 * Adds a data wrapper to this record.
	 *
	 * @param dataWrapper The data wrapper to add.
	 */
	public void addDataWrapper(DataWrapper dataWrapper) {
		this.dataWrappers.add(dataWrapper);
	}

	/**
	 * Gets the list of data wrappers associated with this record.
	 *
	 * @return The list of data wrappers.
	 */
	public ArrayList<DataWrapper> getDataWrappers() {
		return dataWrappers;
	}
}
