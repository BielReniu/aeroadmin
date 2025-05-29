package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources;

import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.exceptions.DataSourceException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SingleConnectionDataSource implements DataSource {

    private Connection connection;

    private final String driver;
    private final String server;
    private final String database;
    private final String user;
    private final String password;

    public SingleConnectionDataSource() {
        Properties properties = new Properties();

        try (InputStream input = getClass().getResourceAsStream("/datasource.properties")) {
            if (input == null) {
                throw new RuntimeException("File 'datasource.properties' not found in the resources folder");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties", e);
        }

        this.driver = properties.getProperty("datasource.driver");
        this.server = properties.getProperty("datasource.server");
        this.database = properties.getProperty("datasource.database");
        this.user = properties.getProperty("datasource.user");
        this.password = properties.getProperty("datasource.password");
    }

    public SingleConnectionDataSource(String driver, String server, String database, String user, String password) {
        this.driver = driver;
        this.server = server;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    @Override
    public Connection getConnection() {
        if (connection == null || isClosed()) {
            try {
                connection = DriverManager.getConnection(
                        String.format("jdbc:%s://%s/%s", driver, server, database),
                        user,
                        password
                );
            } catch (SQLException e) {
                throw new DataSourceException("Failed to establish database connection");
            }
        }

        return connection;
    }

    private boolean isClosed() {
        try {
            return connection == null || connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    // Getters (opcional)
    public String getDriver() {
        return driver;
    }

    public String getServer() {
        return server;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}