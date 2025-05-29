package cat.uvic.teknos.dam.aeroadmin.model.model;

import cat.uvic.teknos.dam.aeroadmin.model.model.*;

public interface ModelFactory {
    Aircraft createAircraft();
    AircraftDetail createAircraftDetail();
    Airline createAirline();
    Pilot createPilot();
    PilotLicense createPilotLicense();
    PilotAssignment createPilotAssignment();
    Flight createFlight();
}