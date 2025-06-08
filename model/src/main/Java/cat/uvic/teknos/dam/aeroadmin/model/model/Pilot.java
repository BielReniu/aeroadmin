package cat.uvic.teknos.dam.aeroadmin.model.model;

import java.time.LocalDate;

public interface Pilot {
    int getPilotId();

    void setPilotId(int pilotId);

    Airline getAirline();

    void setAirline(Airline airline);

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    LocalDate getDateOfBirth();

    void setDateOfBirth(LocalDate dateOfBirth);

    String getNationality();

    void setNationality(String nationality);

    PilotLicense getLicense();

    void setLicense(PilotLicense license);

    String getName();

    void setName(String name);

    void setSurname(String surname);

    void setLicenseNumber(String licenseNumber);

    void setRole(String role);

    void setExperienceYears(int experienceYears);
}