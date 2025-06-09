package cat.uvic.teknos.dam.aeroadmin.repositories;

import cat.uvic.teknos.dam.aeroadmin.repositories.AircraftRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.PilotRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.AirlineRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.FlightRepository;

public interface RepositoryFactory {
    AircraftRepository getAircraftRepository();

    PilotAssignmentRepository getPilotAssignmentRepository();

    PilotLicenseRepository getPilotLicenseRepository();

    PilotRepository getPilotRepository();

    AircraftDetailRepository getAircraftDetailRepository();

    AirlineRepository getAirlineRepository();
    FlightRepository getFlightRepository();

    // Si no vols eliminar-los, pots implementar els mètodes createXxx():
    AircraftRepository createAircraftRepository();

    AircraftDetailRepository createAircraftDetailRepository();

    AirlineRepository createAirlineRepository();

    PilotRepository createPilotRepository();

    PilotLicenseRepository createPilotLicenseRepository();

    PilotAssignmentRepository createPilotAssignmentRepository();

    FlightRepository createFlightRepository();
}