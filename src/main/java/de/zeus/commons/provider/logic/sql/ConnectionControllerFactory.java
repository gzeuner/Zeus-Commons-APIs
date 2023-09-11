package de.zeus.commons.provider.logic.sql;

import de.zeus.commons.base.interfaces.IConnectionController;
import de.zeus.commons.base.interfaces.IJdbcOperations;
import de.zeus.commons.connector.jdbc.JdbcOperations;

/**
 * ConnectionControllerFactory is a factory class for creating
 * instances of the IConnectionController interface.
 *
 * It initializes the JDBC operations and the connection controller during
 * construction and provides a getter for the connection controller.
 */
public class ConnectionControllerFactory {

    /** Member variable for JDBC operations */
    private IJdbcOperations jdbcOperations;

    /** Member variable for the connection controller */
    private IConnectionController connectionController;

    /**
     * Constructor for ConnectionControllerFactory.
     * Initializes the JDBC operations and creates a new connection controller.
     *
     * @param appConfig Configuration parameters for the application.
     * @param logConfig Configuration parameters for logging.
     */
    public ConnectionControllerFactory(String appConfig, String logConfig) {
        // Initializes JdbcOperations with given appConfig and logConfig
        this.jdbcOperations = new JdbcOperations(appConfig, logConfig);

        // Creates and initializes a new connection controller
        this.connectionController = createNewController();
    }

    /**
     * Creates and initializes a new JdbcConnectionController.
     *
     * @return An initialized instance of IConnectionController.
     */
    private IConnectionController createNewController() {
        // Create a new JdbcConnectionController with jdbcOperations
        JdbcConnectionController newController = new JdbcConnectionController(jdbcOperations);

        // Initialize the newly created controller
        newController.init();

        return newController;
    }

    /**
     * Getter for the connection controller.
     *
     * @return The initialized IConnectionController instance.
     */
    public IConnectionController getController() {
        return this.connectionController;
    }
}
