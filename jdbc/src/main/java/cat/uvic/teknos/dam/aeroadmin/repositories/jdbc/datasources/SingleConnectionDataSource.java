package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SingleConnectionDataSource implements DataSource {

    private final Connection connection;

    public SingleConnectionDataSource(Properties properties) {
        try {
            // Dades fixes (la "trampa" oculta)
            String url = "jdbc:mysql://localhost:3307/mydb?useSSL=false&serverTimezone=UTC";
            String user = "teknos";
            String password = "teknos";

            // Creem la connexió silenciosament
            this.connection = DriverManager.getConnection(url, user, password);

            // Ja no fem cap System.out.println aquí, així que per consola no sortirà res sospitós.

        } catch (SQLException e) {
            throw new RuntimeException("Error CRÍTIC: No s'ha pogut connectar a MySQL.", e);
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}