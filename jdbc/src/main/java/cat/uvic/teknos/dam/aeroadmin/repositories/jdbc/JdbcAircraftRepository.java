package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.model.impl.AirlineImpl;
import cat.uvic.teknos.dam.aeroadmin.model.model.Aircraft;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.model.impl.AircraftImpl;
import cat.uvic.teknos.dam.aeroadmin.repositories.AircraftRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.DataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcAircraftRepository implements AircraftRepository {

    private final DataSource dataSource;

    // 1. CONSTRUCTOR CORREGIT
    public JdbcAircraftRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Aircraft aircraft) {
        if (aircraft.getAircraftId() == 0) {
            insert(aircraft);
        } else {
            update(aircraft);
        }
    }

    private void insert(Aircraft aircraft) {
        String sql = "INSERT INTO aircraft (model, manufacturer, registration_number, production_year, airline_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, aircraft.getModel());
            stmt.setString(2, aircraft.getManufacturer());
            stmt.setString(3, aircraft.getRegistrationNumber());
            stmt.setInt(4, aircraft.getProductionYear());

            // Comprova que l'aerolínia no sigui nul·la abans de desar
            if (aircraft.getAirline() != null) {
                stmt.setInt(5, aircraft.getAirline().getAirlineId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    aircraft.setAircraftId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting aircraft", e);
        }
    }

    private void update(Aircraft aircraft) {
        String sql = "UPDATE aircraft SET model = ?, manufacturer = ?, registration_number = ?, production_year = ?, airline_id = ? WHERE aircraft_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aircraft.getModel());
            stmt.setString(2, aircraft.getManufacturer());
            stmt.setString(3, aircraft.getRegistrationNumber());
            stmt.setInt(4, aircraft.getProductionYear());
            if (aircraft.getAirline() != null) {
                stmt.setInt(5, aircraft.getAirline().getAirlineId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setInt(6, aircraft.getAircraftId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating aircraft", e);
        }
    }

    @Override
    public void delete(Aircraft aircraft) {
        String sql = "DELETE FROM aircraft WHERE aircraft_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, aircraft.getAircraftId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting aircraft", e);
        }
    }

    @Override
    public Aircraft get(Integer id) {
        // 2. CONSULTA OPTIMITZADA AMB JOIN
        String sql = "SELECT a.*, al.airline_name, al.iata_code, al.icao_code FROM aircraft a " +
                "LEFT JOIN airline al ON a.airline_id = al.airline_id WHERE a.aircraft_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching aircraft by ID", e);
        }
        return null;
    }

    @Override
    public Set<Aircraft> getAll() {
        Set<Aircraft> aircrafts = new HashSet<>();
        // 2. CONSULTA OPTIMITZADA AMB JOIN
        String sql = "SELECT a.*, al.airline_name, al.iata_code, al.icao_code FROM aircraft a " +
                "LEFT JOIN airline al ON a.airline_id = al.airline_id";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                aircrafts.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all aircrafts", e);
        }
        return aircrafts;
    }

    // 3. MÈTODE getByManufacturer IMPLEMENTAT
    @Override
    public Set<Aircraft> getByManufacturer(String manufacturer) {
        Set<Aircraft> aircrafts = new HashSet<>();
        String sql = "SELECT a.*, al.airline_name, al.iata_code, al.icao_code FROM aircraft a " +
                "LEFT JOIN airline al ON a.airline_id = al.airline_id WHERE a.manufacturer = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, manufacturer);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    aircrafts.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching aircraft by manufacturer", e);
        }
        return aircrafts;
    }

    // 3. MÈTODE create IMPLEMENTAT
    @Override
    public Aircraft create() {
        return new AircraftImpl();
    }

    private Aircraft mapRow(ResultSet rs) throws SQLException {
        Aircraft aircraft = new AircraftImpl();
        aircraft.setAircraftId(rs.getInt("aircraft_id"));
        aircraft.setModel(rs.getString("model"));
        aircraft.setManufacturer(rs.getString("manufacturer"));
        aircraft.setRegistrationNumber(rs.getString("registration_number"));
        aircraft.setProductionYear(rs.getInt("production_year"));

        int airlineId = rs.getInt("airline_id");
        // Comprovem si l'airline_id és null a la base de dades
        if (!rs.wasNull()) {
            Airline airline = new AirlineImpl();
            airline.setAirlineId(airlineId);
            airline.setAirlineName(rs.getString("airline_name"));
            airline.setIataCode(rs.getString("iata_code"));
            airline.setIcaoCode(rs.getString("icao_code"));
            aircraft.setAirline(airline);
        }

        return aircraft;
    }
}