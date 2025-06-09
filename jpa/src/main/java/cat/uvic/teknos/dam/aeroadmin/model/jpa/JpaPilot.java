package cat.uvic.teknos.dam.aeroadmin.model.jpa;

import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.model.model.Pilot;
import cat.uvic.teknos.dam.aeroadmin.model.model.PilotLicense;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pilot")
public class JpaPilot implements Pilot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pilotId;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String nationality;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "license_id")
    private JpaPilotLicense license;

    @ManyToOne
    @JoinColumn(name = "airline_id")
    private JpaAirline airline;

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
    public Airline getAirline() {
        return airline;
    }

    @Override
    public void setAirline(Airline airline) {
        this.airline = (JpaAirline) airline;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String getNationality() {
        return nationality;
    }

    @Override
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Override
    public PilotLicense getLicense() {
        return license;
    }

    @Override
    public void setLicense(PilotLicense license) {
        this.license = (JpaPilotLicense) license;
    }

    @Override
    public String getName() {
        return getFirstName();
    }

    @Override
    public void setName(String name) {
        setFirstName(name);
    }

    @Override
    public void setSurname(String surname) {
        setLastName(surname);
    }

    @Override
    public void setLicenseNumber(String licenseNumber) {
        if (this.license == null) {
            this.license = new JpaPilotLicense();
        }
        this.license.setLicenseNumber(licenseNumber);
    }

    @Override
    public void setRole(String role) {
        // Implementar si necesario
    }

    @Override
    public void setExperienceYears(int experienceYears) {
        // Implementar si necesario
    }
}