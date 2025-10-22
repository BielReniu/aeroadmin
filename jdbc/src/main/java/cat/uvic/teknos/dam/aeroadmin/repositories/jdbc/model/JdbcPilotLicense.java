package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.model;

import cat.uvic.teknos.dam.aeroadmin.model.enums.LicenseType;
import java.time.LocalDate;

public class JdbcPilotLicense {

    private int pilotId;
    private String licenseNumber;
    private LicenseType licenseType;
    private LocalDate issueDate;
    private LocalDate expirationDate;

    // Getters i Setters
    public int getPilotId() {
        return pilotId;
    }

    public void setPilotId(int pilotId) {
        this.pilotId = pilotId;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public LicenseType getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
}