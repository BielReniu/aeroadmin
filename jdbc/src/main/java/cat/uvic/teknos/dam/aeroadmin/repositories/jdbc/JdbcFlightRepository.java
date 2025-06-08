package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.model.enums.FlightStatus;
import cat.uvic.teknos.dam.aeroadmin.model.model.Flight;
import cat.uvic.teknos.dam.aeroadmin.model.model.Aircraft;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.model.impl.FlightImpl;
import cat.uvic.teknos.dam.aeroadmin.repositories.FlightRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.SingleConnectionDataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcFlightRepository implements FlightRepository {

    private final DataSource dataSource;

    public JdbcFlightRepository(DataSource dataSource) {
        this.dataSource = new SingleConnectionDataSource();
    }

    @Override
    public void save(Flight flight) {
        if (flight.getFlightId() == 0) {
            insert(flight);
        } else {
            update(flight);
        }
    }

    private void insert(Flight flight) {
        String sql = "INSERT INTO flight (flight_number, departure_airport, arrival_airport, scheduled_departure, scheduled_arrival, status, aircraft_id, airline_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, flight.getFlightNumber());
            stmt.setString(2, flight.getDepartureAirport());
            stmt.setString(3, flight.getArrivalAirport());
            stmt.setTimestamp(4, Timestamp.valueOf(flight.getScheduledDeparture()));
            stmt.setTimestamp(5, Timestamp.valueOf(flight.getScheduledArrival()));
            stmt.setString(6, flight.getStatus().name());
            stmt.setInt(7, flight.getAircraft().getAircraftId());
            stmt.setInt(8, flight.getAirline().getAirlineId());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    flight.setFlightId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting flight", e);
        }
    }

    private void update(Flight flight) {
        String sql = "UPDATE flight SET flight_number = ?, departure_airport = ?, arrival_airport = ?, scheduled_departure = ?, scheduled_arrival = ?, status = ?, aircraft_id = ?, airline_id = ? WHERE flight_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, flight.getFlightNumber());
            stmt.setString(2, flight.getDepartureAirport());
            stmt.setString(3, flight.getArrivalAirport());
            stmt.setTimestamp(4, Timestamp.valueOf(flight.getScheduledDeparture()));
            stmt.setTimestamp(5, Timestamp.valueOf(flight.getScheduledArrival()));
            stmt.setString(6, flight.getStatus().name());
            stmt.setInt(7, flight.getAircraft().getAircraftId());
            stmt.setInt(8, flight.getAirline().getAirlineId());
            stmt.setInt(9, flight.getFlightId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating flight", e);
        }
    }

    @Override
    public void delete(Flight flight) {
        String sql = "DELETE FROM flight WHERE flight_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, flight.getFlightId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting flight", e);
        }
    }

    @Override
    public Flight get(Integer id) {
        String sql = "SELECT * FROM flight WHERE flight_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching flight by ID", e);
        }
        return null;
    }

    @Override
    public Set<Flight> getAll() {
        Set<Flight> flights = new HashSet<>();
        String sql = "SELECT * FROM flight";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                flights.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all flights", e);
        }
        return flights;
    }

    private Flight mapRow(ResultSet rs) throws SQLException {
        var flight = new FlightImpl();
        flight.setFlightId(rs.getInt("flight_id"));
        flight.setFlightNumber(rs.getString("flight_number"));
        flight.setDepartureAirport(rs.getString("departure_airport"));
        flight.setArrivalAirport(rs.getString("arrival_airport"));
        flight.setScheduledDeparture(rs.getTimestamp("scheduled_departure").toLocalDateTime());
        flight.setScheduledArrival(rs.getTimestamp("scheduled_arrival").toLocalDateTime());
        flight.setStatus(FlightStatus.valueOf(rs.getString("status")));

        int airlineId = rs.getInt("airline_id");
        Airline airline = new JdbcAirlineRepository(dataSource).get(airlineId);
        flight.setAirline(airline);

        int aircraftId = rs.getInt("aircraft_id");
        Aircraft aircraft = new JdbcAircraftRepository(dataSource).get(aircraftId);
        flight.setAircraft(aircraft);

        return flight;
    }

    @Override
    public Set<Flight> getByDepartureAirport(String departureAirport) {
        return Set.of();
    }
}