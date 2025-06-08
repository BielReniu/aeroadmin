package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.jupiter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(LoadDatabaseExtension.class)
class LoadDatabaseExtensionTest {
    @Test void loadSchema() {
        assertTrue(true);
    }
}