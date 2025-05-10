package cat.uvic.teknos.dam.aeroadmin.impl;

import cat.uvic.teknos.dam.aeroadmin.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.model.Pilot;
import cat.uvic.teknos.dam.aeroadmin.model.PilotLicense;

import java.time.LocalDate;

public class PilotImpl implements Pilot {
    private int pilotId;
    private Airline airline;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String nationality;
    private PilotLicense license;

    @Override
    public int getPilotId() { return pilotId; }

    @Override
    public void setPilotId(int pilotId) { this.pilotId = pilotId; }

    @Override
    public Airline getAirline() { return airline; }

    @Override
    public void setAirline(Airline airline) { this.airline = airline; }

    @Override
    public String getFirstName() { return firstName; }

    @Override
    public void setFirstName(String firstName) { this.firstName = firstName; }

    @Override
    public String getLastName() { return lastName; }

    @Override
    public void setLastName(String lastName) { this.lastName = lastName; }

    @Override
    public LocalDate getDateOfBirth() { return dateOfBirth; }

    @Override
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    @Override
    public String getNationality() { return nationality; }

    @Override
    public void setNationality(String nationality) { this.nationality = nationality; }

    @Override
    public PilotLicense getLicense() { return license; }

    @Override
    public void setLicense(PilotLicense license) { this.license = license; }
}