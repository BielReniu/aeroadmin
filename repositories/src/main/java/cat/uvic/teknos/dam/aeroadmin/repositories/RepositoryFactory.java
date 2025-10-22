package cat.uvic.teknos.dam.aeroadmin.repositories;

public interface RepositoryFactory {
    AircraftRepository getAircraftRepository();

    AircraftDetailRepository getAircraftDetailRepository();

    AirlineRepository getAirlineRepository();

    FlightRepository getFlightRepository();

    PilotRepository getPilotRepository();

    PilotLicenseRepository getPilotLicenseRepository();

    PilotAssignmentRepository getPilotAssignmentRepository();
}