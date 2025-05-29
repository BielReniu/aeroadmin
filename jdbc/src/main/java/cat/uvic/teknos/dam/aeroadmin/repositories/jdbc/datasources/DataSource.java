package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources;

import java.sql.Connection;

public interface DataSource {
    Connection getConnection();
}
