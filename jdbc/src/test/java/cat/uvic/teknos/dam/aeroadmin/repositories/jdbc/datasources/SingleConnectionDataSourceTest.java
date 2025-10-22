package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources;

import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.exceptions.DataSourceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class SingleConnectionDataSourceTest {

    @Test
    void getDriver() {
        var dataSource = new SingleConnectionDataSource();

        assertEquals("mysql", dataSource.getDriver());
    }

    @Test
    void getServer() {
    }

    @Test
    void getDatabase() {
    }

    @Test
    void getUser() {
    }

    @Test
    void getPassword() {
    }




}