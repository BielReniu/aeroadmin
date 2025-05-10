package cat.uvic.teknos.dam.aeroadmin.model;

import cat.uvic.teknos.dam.aeroadmin.enums.FlightStatus;

import java.time.LocalDateTime;

public interface Flight {
    int getFlightId();
    void setFlightId(int flightId);

    Airline getAirline();
    void setAirline(Airline airline);

    Aircraft getAircraft();
    void setAircraft(Aircraft aircraft);

    String getFlightNumber();
    void setFlightNumber(String flightNumber);

    String getDepartureAirport();
    void setDepartureAirport(String departureAirport);

    String getArrivalAirport();
    void setArrivalAirport(String arrivalAirport);

    LocalDateTime getScheduledDeparture();
    void setScheduledDeparture(LocalDateTime scheduledDeparture);

    LocalDateTime getScheduledArrival();
    void setScheduledArrival(LocalDateTime scheduledArrival);

    FlightStatus getStatus();
    void setStatus(FlightStatus status);
}