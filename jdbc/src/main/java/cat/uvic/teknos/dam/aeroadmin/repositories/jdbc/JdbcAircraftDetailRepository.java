package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.model.impl.AircraftDetailImpl;
import cat.uvic.teknos.dam.aeroadmin.model.model.AircraftDetail;
import cat.uvic.teknos.dam.aeroadmin.repositories.AircraftDetailRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.DataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

class JdbcAircraftDetailRepository implements AircraftDetailRepository {

    private final DataSource dataSource;

    // AQUEST ÉS EL CONSTRUCTOR CORREGIT
    public JdbcAircraftDetailRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(AircraftDetail detail) {
        if (detail.getAircraftId() == 0) {
            insert(detail);
        } else {
            update(detail);
        }
    }

    private void insert(AircraftDetail detail) {
        // Aquesta implementació assumeix que l'ID es gestiona a la taula AIRCRAFT
        // i ja existeix quan es desa el detall.
        String sql = "INSERT INTO aircraft_detail (aircraft_id, passenger_capacity, max_range_km, max_speed_kmh, fuel_capacity_liters) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detail.getAircraftId());
            stmt.setInt(2, detail.getPassengerCapacity());
            stmt.setInt(3, detail.getMaxRangeKm());
            stmt.setInt(4, detail.getMaxSpeedKmh());
            stmt.setInt(5, detail.getFuelCapacityLiters());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting aircraft detail", e);
        }
    }

    private void update(AircraftDetail detail) {
        String sql = "UPDATE aircraft_detail SET passenger_capacity = ?, max_range_km = ?, max_speed_kmh = ?, fuel_capacity_liters = ? WHERE aircraft_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detail.getPassengerCapacity());
            stmt.setInt(2, detail.getMaxRangeKm());
            stmt.setInt(3, detail.getMaxSpeedKmh());
            stmt.setInt(4, detail.getFuelCapacityLiters());
            stmt.setInt(5, detail.getAircraftId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating aircraft detail", e);
        }
    }

    @Override
    public void delete(AircraftDetail detail) {
        String sql = "DELETE FROM aircraft_detail WHERE aircraft_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detail.getAircraftId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting aircraft detail", e);
        }
    }

    @Override
    public AircraftDetail get(Integer id) {
        String sql = "SELECT * FROM aircraft_detail WHERE aircraft_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching aircraft detail by ID", e);
        }
        return null;
    }

    @Override
    public Set<AircraftDetail> getAll() {
        Set<AircraftDetail> details = new HashSet<>();
        String sql = "SELECT * FROM aircraft_detail";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                details.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all aircraft details", e);
        }
        return details;
    }

    private AircraftDetail mapRow(ResultSet rs) throws SQLException {
        var detail = new AircraftDetailImpl();
        detail.setAircraftId(rs.getInt("aircraft_id"));
        detail.setPassengerCapacity(rs.getInt("passenger_capacity"));
        detail.setMaxRangeKm(rs.getInt("max_range_km"));
        detail.setMaxSpeedKmh(rs.getInt("max_speed_kmh"));
        detail.setFuelCapacityLiters(rs.getInt("fuel_capacity_liters"));
        return detail;
    }

    @Override
    public Set<AircraftDetail> getByPassengerCapacity(int minCapacity, int maxCapacity) {
        // Aquest mètode està pendent d'implementar
        return Set.of();
    }

    @Override
    public AircraftDetail create() {
        // Aquest mètode està pendent d'implementar
        return new AircraftDetailImpl();
    }
}