package cat.uvic.teknos.dam.aeroadmin.jpa.model;

import cat.uvic.teknos.dam.aeroadmin.model.model.*;

public class JpaModelFactory implements ModelFactory {

    @Override
    public Aircraft createAircraft() {
        return new JpaAircraft();
    }

    @Override
    public Pilot createPilot() {
        return new JpaPilot();
    }

    @Override
    public AircraftDetail createAircraftDetail() {
        return null;
    }

    @Override
    public Airline createAirline() {
        return new JpaAirline();
    }

    @Override
    public PilotLicense createPilotLicense() {
        return null;
    }

    @Override
    public PilotAssignment createPilotAssignment() {
        return null;
    }

    @Override
    public Flight createFlight() {
        return new JpaFlight();
    }
}