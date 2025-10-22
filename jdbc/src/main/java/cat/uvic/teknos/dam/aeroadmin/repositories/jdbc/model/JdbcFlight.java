package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.model;

import cat.uvic.teknos.dam.aeroadmin.model.enums.FlightStatus;
import java.time.LocalDateTime;

public class JdbcFlight {

    private int flightId;
    private String flightNumber;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime scheduledArrival;
    private FlightStatus status;
    private JdbcAircraft aircraft;
    private JdbcAirline airline;

    // Getters i Setters
    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public LocalDateTime getScheduledDeparture() {
        return scheduledDeparture;
    }

    public void setScheduledDeparture(LocalDateTime scheduledDeparture) {
        this.scheduledDeparture = scheduledDeparture;
    }

    public LocalDateTime getScheduledArrival() {
        return scheduledArrival;
    }

    public void setScheduledArrival(LocalDateTime scheduledArrival) {
        this.scheduledArrival = scheduledArrival;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public JdbcAircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(JdbcAircraft aircraft) {
        this.aircraft = aircraft;
    }

    public JdbcAirline getAirline() {
        return airline;
    }

    public void setAirline(JdbcAirline airline) {
        this.airline = airline;
    }
}