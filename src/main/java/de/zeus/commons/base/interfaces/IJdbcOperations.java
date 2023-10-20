package de.zeus.commons.base.interfaces;

import de.zeus.commons.connector.jdbc.ProcessingException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * An interface defining common JDBC database operations.
 */
public interface IJdbcOperations {

	/**
	 * Gets a database connection.
	 *
	 * @return A database connection.
	 */
	Connection getDatabaseConnection() throws ProcessingException;

	/**
	 * Closes a database connection.
	 *
	 * @param con The database connection to close.
	 */
	void closeDatabaseConnection(Connection con);

	/**
	 * Gets a prepared statement for executing a query with parameters.
	 *
	 * @param con     The database connection.
	 * @param query   The SQL query.
	 * @param params  The parameters to be set in the prepared statement.
	 * @return A prepared statement.
	 */
	PreparedStatement getPreparedStmt(Connection con, String query, Object... params);

	/**
	 * Gets a statement for executing SQL queries.
	 *
	 * @param con The database connection.
	 * @return A statement.
	 */
	Statement getStmt(Connection con) throws ProcessingException;

	/**
	 * Closes a ResultSet and a Statement.
	 *
	 * @param rSet The ResultSet to close.
	 * @param stmt The Statement to close.
	 * @throws SQLException If an SQL exception occurs.
	 */
	void close(ResultSet rSet, Statement stmt) throws SQLException;

}
