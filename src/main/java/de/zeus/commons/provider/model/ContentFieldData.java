package de.zeus.commons.provider.model;

import de.zeus.commons.provider.format.Format;

/**
 * Represents a single field of data within a database query result record.
 */
public class ContentFieldData {

	private int columnNumber; // The column number in the result set
	private String value; // The value of the field
	private final Format format = new Format(); // Format utility for data values

	/**
	 * Gets the column number of this field in the result set.
	 *
	 * @return The column number.
	 */
	public int getColumnNumber() {
		return columnNumber;
	}

	/**
	 * Sets the column number of this field in the result set.
	 *
	 * @param columnNumber The column number to set.
	 */
	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}

	/**
	 * Gets the value of this field.
	 *
	 * @return The field value.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of this field and trims leading and trailing whitespace.
	 *
	 * @param value The field value to set.
	 */
	public void setValue(String value) {
		this.value = format.trim(value);
	}

	/**
	 * Returns a string representation of this ContentFieldData object.
	 *
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		return "ContentFieldData{" +
				"columnNumber=" + columnNumber +
				", value='" + value + '\'' +
				'}';
	}
}
