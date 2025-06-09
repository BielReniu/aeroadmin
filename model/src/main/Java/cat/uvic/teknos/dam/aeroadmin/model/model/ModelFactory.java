package cat.uvic.teknos.dam.aeroadmin.model.model;

import cat.uvic.teknos.dam.aeroadmin.model.model.*;

public interface ModelFactory {
    Aircraft createAircraft();
    Pilot createPilot();

    AircraftDetail createAircraftDetail();

    Airline createAirline();

    PilotLicense createPilotLicense();

    PilotAssignment createPilotAssignment();

    Flight createFlight();
}