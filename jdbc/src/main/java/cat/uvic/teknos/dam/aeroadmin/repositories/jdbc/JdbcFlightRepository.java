package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.model.enums.FlightStatus;
import cat.uvic.teknos.dam.aeroadmin.model.impl.AircraftImpl;
import cat.uvic.teknos.dam.aeroadmin.model.impl.AirlineImpl;
import cat.uvic.teknos.dam.aeroadmin.model.model.Flight;
import cat.uvic.teknos.dam.aeroadmin.model.model.Aircraft;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.model.impl.FlightImpl;
import cat.uvic.teknos.dam.aeroadmin.repositories.FlightRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.DataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcFlightRepository implements FlightRepository {

    private final DataSource dataSource;

    // 1. CONSTRUCTOR CORREGIT
    public JdbcFlightRepository(DataSource dataSource) {
        this.dataSource = dataSource;
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

    // 2. Base de la consulta optimitzada amb JOINs
    private static final String QUERY_BASE = "SELECT f.*, " +
            "al.airline_name, al.iata_code, al.icao_code, " + // Camps de Airline
            "ac.model, ac.manufacturer, ac.registration_number " + // Camps de Aircraft
            "FROM flight f " +
            "LEFT JOIN airline al ON f.airline_id = al.airline_id " +
            "LEFT JOIN aircraft ac ON f.aircraft_id = ac.aircraft_id ";

    @Override
    public Flight get(Integer id) {
        String sql = QUERY_BASE + "WHERE f.flight_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching flight by ID", e);
        }
        return null;
    }

    @Override
    public Set<Flight> getAll() {
        Set<Flight> flights = new HashSet<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(QUERY_BASE)) {
            while (rs.next()) {
                flights.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all flights", e);
        }
        return flights;
    }

    // 3. MÈTODE IMPLEMENTAT
    @Override
    public Set<Flight> getByDepartureAirport(String departureAirport) {
        Set<Flight> flights = new HashSet<>();
        String sql = QUERY_BASE + "WHERE f.departure_airport = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, departureAirport);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    flights.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching flights by departure airport", e);
        }
        return flights;
    }

    // 3. MÈTODE IMPLEMENTAT
    @Override
    public Flight create() {
        return new FlightImpl();
    }

    private Flight mapRow(ResultSet rs) throws SQLException {
        Flight flight = new FlightImpl();
        flight.setFlightId(rs.getInt("flight_id"));
        flight.setFlightNumber(rs.getString("flight_number"));
        flight.setDepartureAirport(rs.getString("departure_airport"));
        flight.setArrivalAirport(rs.getString("arrival_airport"));
        flight.setScheduledDeparture(rs.getTimestamp("scheduled_departure").toLocalDateTime());
        flight.setScheduledArrival(rs.getTimestamp("scheduled_arrival").toLocalDateTime());
        flight.setStatus(FlightStatus.valueOf(rs.getString("status")));

        // Mapejar Airline des del JOIN
        int airlineId = rs.getInt("airline_id");
        if (!rs.wasNull()) {
            Airline airline = new AirlineImpl();
            airline.setAirlineId(airlineId);
            airline.setAirlineName(rs.getString("airline_name"));
            airline.setIataCode(rs.getString("iata_code"));
            airline.setIcaoCode(rs.getString("icao_code"));
            flight.setAirline(airline);
        }

        // Mapejar Aircraft des del JOIN
        int aircraftId = rs.getInt("aircraft_id");
        if (!rs.wasNull()) {
            Aircraft aircraft = new AircraftImpl();
            aircraft.setAircraftId(aircraftId);
            aircraft.setModel(rs.getString("model"));
            aircraft.setManufacturer(rs.getString("manufacturer"));
            aircraft.setRegistrationNumber(rs.getString("registration_number"));
            flight.setAircraft(aircraft);
        }

        return flight;
    }
}