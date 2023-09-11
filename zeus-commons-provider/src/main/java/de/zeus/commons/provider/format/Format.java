package de.zeus.commons.provider.format;

public class Format {

	/**
	 * Trims leading and trailing whitespaces from a given string.
	 *
	 * @param value The string to trim.
	 * @return The trimmed string, or an empty string if the input is null.
	 */
	public String trim(String value) {
		if (value == null) {
			return "";
		} else {
			return value.trim();
		}
	}

	/**
	 * Converts a given string to lowercase.
	 *
	 * @param value The string to convert.
	 * @return The lowercase version of the input string, or the input string if it's null.
	 */
	public String toLowerCase(String value) {
		if (null != value) {
			value = value.toLowerCase();
		}
		return value;
	}
}
