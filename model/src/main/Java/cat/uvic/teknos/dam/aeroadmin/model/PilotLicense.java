package cat.uvic.teknos.dam.aeroadmin.model;

import cat.uvic.teknos.dam.aeroadmin.enums.LicenseType;

import java.time.LocalDate;

public interface PilotLicense {
    int getPilotId();
    void setPilotId(int pilotId);

    String getLicenseNumber();
    void setLicenseNumber(String licenseNumber);

    LicenseType getLicenseType();
    void setLicenseType(LicenseType licenseType);

    LocalDate getIssueDate();
    void setIssueDate(LocalDate issueDate);

    LocalDate getExpirationDate();
    void setExpirationDate(LocalDate expirationDate);
}