package cat.uvic.teknos.dam.aeroadmin.impl;

import cat.uvic.teknos.dam.aeroadmin.enums.LicenseType;
import cat.uvic.teknos.dam.aeroadmin.model.PilotLicense;

import java.time.LocalDate;

public class PilotLicenseImpl implements PilotLicense {
    private int pilotId;
    private String licenseNumber;
    private LicenseType licenseType;
    private LocalDate issueDate;
    private LocalDate expirationDate;

    @Override
    public int getPilotId() { return pilotId; }

    @Override
    public void setPilotId(int pilotId) { this.pilotId = pilotId; }

    @Override
    public String getLicenseNumber() { return licenseNumber; }

    @Override
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    @Override
    public LicenseType getLicenseType() { return licenseType; }

    @Override
    public void setLicenseType(LicenseType licenseType) { this.licenseType = licenseType; }

    @Override
    public LocalDate getIssueDate() { return issueDate; }

    @Override
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    @Override
    public LocalDate getExpirationDate() { return expirationDate; }

    @Override
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }
}