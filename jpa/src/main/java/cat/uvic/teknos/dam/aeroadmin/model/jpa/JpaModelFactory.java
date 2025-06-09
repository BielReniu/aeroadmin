package cat.uvic.teknos.dam.aeroadmin.model.jpa;

import cat.uvic.teknos.dam.aeroadmin.model.model.*;

public class JpaModelFactory implements ModelFactory {

    @Override
    public Aircraft createAircraft() {
        return new JpaAircraft();
    }

    @Override
    public AircraftDetail createAircraftDetail() {
        return new JpaAircraftDetail();
    }

    @Override
    public Airline createAirline() {
        return new JpaAirline();
    }

    @Override
    public Pilot createPilot() {
        return new JpaPilot();
    }

    @Override
    public PilotLicense createPilotLicense() {
        return new JpaPilotLicense();
    }

    @Override
    public PilotAssignment createPilotAssignment() {
        return new JpaPilotAssignment();
    }

    @Override
    public Flight createFlight() {
        return new JpaFlight();
    }
}