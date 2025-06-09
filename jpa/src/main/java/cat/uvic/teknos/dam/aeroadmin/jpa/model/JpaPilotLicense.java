package cat.uvic.teknos.dam.aeroadmin.jpa.model;

import cat.uvic.teknos.dam.aeroadmin.model.enums.LicenseType;
import cat.uvic.teknos.dam.aeroadmin.model.model.PilotLicense;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pilot_license")
public class JpaPilotLicense implements PilotLicense {

    @Id
    private int pilotId;

    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    private LicenseType licenseType;

    private LocalDate issueDate;
    private LocalDate expirationDate;

    // Getters y setters

    @Override
    public int getPilotId() {
        return pilotId;
    }

    @Override
    public void setPilotId(int pilotId) {
        this.pilotId = pilotId;
    }

    @Override
    public String getLicenseNumber() {
        return licenseNumber;
    }

    @Override
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    @Override
    public LicenseType getLicenseType() {
        return licenseType;
    }

    @Override
    public void setLicenseType(LicenseType licenseType) {
        this.licenseType = licenseType;
    }

    @Override
    public LocalDate getIssueDate() {
        return issueDate;
    }

    @Override
    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    @Override
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    @Override
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
}