package cat.uvic.teknos.dam.aeroadmin.model.jpa;

import cat.uvic.teknos.dam.aeroadmin.model.jpa.JpaPilotLicense;
import cat.uvic.teknos.dam.aeroadmin.model.model.PilotLicense;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JpaPilotLicenseTest {

    @Test
    void shouldSetAndGetProperties() {
        PilotLicense license = new JpaPilotLicense();

        license.setPilotId(1);
        license.setLicenseNumber("LIC123456");
        license.setLicenseType(cat.uvic.teknos.dam.aeroadmin.model.enums.LicenseType.ATPL);
        license.setIssueDate(LocalDate.of(2010, 5, 10));
        license.setExpirationDate(LocalDate.of(2020, 5, 10));

        assertEquals(1, license.getPilotId());
        assertEquals("LIC123456", license.getLicenseNumber());
        assertEquals(cat.uvic.teknos.dam.aeroadmin.model.enums.LicenseType.ATPL, license.getLicenseType());
        assertEquals(LocalDate.of(2010, 5, 10), license.getIssueDate());
        assertEquals(LocalDate.of(2020, 5, 10), license.getExpirationDate());
    }
}