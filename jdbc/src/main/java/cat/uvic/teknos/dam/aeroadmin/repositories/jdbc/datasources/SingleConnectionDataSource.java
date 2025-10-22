package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// Important: Aquesta classe implementa la teva interfície DataSource
public class SingleConnectionDataSource implements DataSource {

    private final Connection connection;

    public SingleConnectionDataSource(Properties properties) {
        try {
            // Agafa les propietats (URL, USER, PASSWORD) i crea la connexió
            this.connection = DriverManager.getConnection(
                    properties.getProperty("URL"),
                    properties.getProperty("USER"),
                    properties.getProperty("PASSWORD")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error en connectar a la base de dades", e);
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}