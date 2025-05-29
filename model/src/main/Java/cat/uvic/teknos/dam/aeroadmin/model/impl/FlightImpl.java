package cat.uvic.teknos.dam.aeroadmin.model.impl;

import cat.uvic.teknos.dam.aeroadmin.model.enums.FlightStatus;
import cat.uvic.teknos.dam.aeroadmin.model.model.Aircraft;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.model.model.Flight;

import java.time.LocalDateTime;

public class FlightImpl implements Flight {
    private int flightId;
    private Airline airline;
    private Aircraft aircraft;
    private String flightNumber;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime scheduledArrival;
    private FlightStatus status;

    @Override
    public int getFlightId() {
        return flightId;
    }

    @Override
    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    @Override
    public Airline getAirline() {
        return airline;
    }

    @Override
    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    @Override
    public Aircraft getAircraft() {
        return aircraft;
    }

    @Override
    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    @Override
    public String getFlightNumber() {
        return flightNumber;
    }

    @Override
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    @Override
    public String getDepartureAirport() {
        return departureAirport;
    }

    @Override
    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    @Override
    public String getArrivalAirport() {
        return arrivalAirport;
    }

    @Override
    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    @Override
    public LocalDateTime getScheduledDeparture() {
        return scheduledDeparture;
    }

    @Override
    public void setScheduledDeparture(LocalDateTime scheduledDeparture) {
        this.scheduledDeparture = scheduledDeparture;
    }

    @Override
    public LocalDateTime getScheduledArrival() {
        return scheduledArrival;
    }

    @Override
    public void setScheduledArrival(LocalDateTime scheduledArrival) {
        this.scheduledArrival = scheduledArrival;
    }

    @Override
    public FlightStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(FlightStatus status) {
        this.status = status;
    }
}