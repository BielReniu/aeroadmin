package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.model.model.Aircraft;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.model.impl.AircraftImpl;
import cat.uvic.teknos.dam.aeroadmin.repositories.AircraftRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.SingleConnectionDataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcAircraftRepository implements AircraftRepository {

    private final DataSource dataSource;

    public JdbcAircraftRepository(DataSource dataSource) {
        this.dataSource = new DataSource() {
            @Override
            public Connection getConnection() {
                return null;
            }
        };
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
            stmt.setInt(5, aircraft.getAirline().getAirlineId());

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
            stmt.setInt(5, aircraft.getAirline().getAirlineId());
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
        String sql = "SELECT * FROM aircraft WHERE aircraft_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching aircraft by ID", e);
        }
        return null;
    }

    @Override
    public Set<Aircraft> getAll() {
        Set<Aircraft> aircrafts = new HashSet<>();
        String sql = "SELECT * FROM aircraft";
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

    private Aircraft mapRow(ResultSet rs) throws SQLException {
        var aircraft = new AircraftImpl();
        aircraft.setAircraftId(rs.getInt("aircraft_id"));
        aircraft.setModel(rs.getString("model"));
        aircraft.setManufacturer(rs.getString("manufacturer"));
        aircraft.setRegistrationNumber(rs.getString("registration_number"));
        aircraft.setProductionYear(rs.getInt("production_year"));

        int airlineId = rs.getInt("airline_id");
        Airline airline = new JdbcAirlineRepository(dataSource).get(airlineId);
        aircraft.setAirline(airline);

        return aircraft;
    }

    @Override
    public Set<Aircraft> getByManufacturer(String manufacturer) {
        return Set.of();
    }

    @Override
    public Aircraft create() {
        return null;
    }
}