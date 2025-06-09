package cat.uvic.teknos.dam.aeroadmin.jpa.model;

import cat.uvic.teknos.dam.aeroadmin.model.enums.FlightStatus;
import cat.uvic.teknos.dam.aeroadmin.model.model.Aircraft;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.model.model.Flight;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "flight")
public class JpaFlight implements Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int flightId;

    private String flightNumber;
    private String departureAirport;
    private String arrivalAirport;

    @Column(name = "scheduled_departure")
    private LocalDateTime scheduledDeparture;

    @Column(name = "scheduled_arrival")
    private LocalDateTime scheduledArrival;

    @Enumerated(EnumType.STRING)
    private FlightStatus status;

    @ManyToOne
    @JoinColumn(name = "airline_id")
    private JpaAirline airline;

    @ManyToOne
    @JoinColumn(name = "aircraft_id")
    private JpaAircraft aircraft;

    // Getters y setters

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
        this.airline = (JpaAirline) airline;
    }

    @Override
    public Aircraft getAircraft() {
        return aircraft;
    }

    @Override
    public void setAircraft(Aircraft aircraft) {
        this.aircraft = (JpaAircraft) aircraft;
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