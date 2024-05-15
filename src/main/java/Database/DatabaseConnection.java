package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private String url;
    private String username;
    private String password;
    private Connection connection;

    public DatabaseConnection(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    // Connect to the database
    public Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load PostgreSQL JDBC driver
                Class.forName("org.postgresql.Driver");
                // Establish connection
                connection = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new SQLException("Failed to load JDBC driver");
            }
        }
        return connection;
    }

    // Get the current connection
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            return connect();
        }
        return connection;
    }

    // Close the connection
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
