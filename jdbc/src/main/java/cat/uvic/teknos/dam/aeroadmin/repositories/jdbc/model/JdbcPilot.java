package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.model;

import java.time.LocalDate;

public class JdbcPilot {

    private int pilotId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String nationality;
    private JdbcAirline airline;

    // Getters i Setters
    public int getPilotId() {
        return pilotId;
    }

    public void setPilotId(int pilotId) {
        this.pilotId = pilotId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public JdbcAirline getAirline() {
        return airline;
    }

    public void setAirline(JdbcAirline airline) {
        this.airline = airline;
    }
}