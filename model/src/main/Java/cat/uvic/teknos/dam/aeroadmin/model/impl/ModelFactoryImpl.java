package cat.uvic.teknos.dam.aeroadmin.model.impl;

import cat.uvic.teknos.dam.aeroadmin.model.model.ModelFactory;
import cat.uvic.teknos.dam.aeroadmin.model.model.*;

public class ModelFactoryImpl implements ModelFactory {

    @Override
    public Aircraft createAircraft() {
        return new AircraftImpl();
    }

    @Override
    public AircraftDetail createAircraftDetail() {
        return new AircraftDetailImpl();
    }

    @Override
    public Airline createAirline() {
        return new AirlineImpl();
    }

    @Override
    public Pilot createPilot() {
        return new PilotImpl();
    }

    @Override
    public PilotLicense createPilotLicense() {
        return new PilotLicenseImpl();
    }

    @Override
    public PilotAssignment createPilotAssignment() {
        return new PilotAssignmentImpl();
    }

    @Override
    public Flight createFlight() {
        return new FlightImpl();
    }
}