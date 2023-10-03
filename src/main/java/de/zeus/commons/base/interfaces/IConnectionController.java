package de.zeus.commons.base.interfaces;

import com.google.gson.JsonObject;
import de.zeus.commons.connector.jdbc.DatabaseConnectionException;

/**
 * An interface defining operations for managing a data connection.
 */
public interface IConnectionController {

	/**
	 * Reads data using the provided SQL query.
	 *
	 * @param sqlQuery The SQL query to read data.
	 * @return The data read from the query.
	 */
	Object readData(String sqlQuery) throws DatabaseConnectionException;

	/**
	 * Connects to the data service.
	 */
	void connectDataService() throws DatabaseConnectionException;

	/**
	 * Disconnects from the data service.
	 */
	void disconnectDataService();

	/**
	 * Processes a JSON request in the specified mode.
	 *
	 * @param jsonRequest The JSON request to process.
	 * @param mode        The processing mode (e.g., JSON or XML).
	 * @return The result of processing the JSON request.
	 */
	Object process(JsonObject jsonRequest, String mode);
}
