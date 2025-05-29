package cat.uvic.teknos.dam.aeroadmin.repositories;

import cat.uvic.teknos.dam.aeroadmin.repositories.*;

public interface RepositoryFactory {

    AircraftDetailRepository getAircraftDetailRepository();

    AircraftRepository getAircraftRepository();

    AirlineRepository getAirlineRepository();

    FlightRepository getFlightRepository();

    PilotAssignmentRepository getPilotAssignmentRepository();

    PilotLicenseRepository getPilotLicenseRepository();

    PilotRepository getPilotRepository();

    AircraftRepository createAircraftRepository();
    AircraftDetailRepository createAircraftDetailRepository();
    AirlineRepository createAirlineRepository();
    PilotRepository createPilotRepository();
    PilotLicenseRepository createPilotLicenseRepository();
    PilotAssignmentRepository createPilotAssignmentRepository();
    FlightRepository createFlightRepository();
}