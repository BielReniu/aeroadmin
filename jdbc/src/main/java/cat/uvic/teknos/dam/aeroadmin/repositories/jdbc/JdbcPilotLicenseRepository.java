package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.model.enums.LicenseType;
import cat.uvic.teknos.dam.aeroadmin.model.model.PilotLicense;
import cat.uvic.teknos.dam.aeroadmin.model.impl.PilotLicenseImpl;
import cat.uvic.teknos.dam.aeroadmin.repositories.PilotLicenseRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.SingleConnectionDataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcPilotLicenseRepository implements PilotLicenseRepository {

    private final DataSource dataSource;

    public JdbcPilotLicenseRepository(DataSource dataSource) {
        this.dataSource = new DataSource() {
            @Override
            public Connection getConnection() {
                return null;
            }
        };
    }

    @Override
    public void save(PilotLicense license) {
        if (license.getPilotId() == 0) {
            insert(license);
        } else {
            update(license);
        }
    }

    private void insert(PilotLicense license) {
        String sql = "INSERT INTO pilot_license (pilot_id, license_number, license_type, issue_date, expiration_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, license.getPilotId());
            stmt.setString(2, license.getLicenseNumber());
            stmt.setString(3, license.getLicenseType().name());
            stmt.setDate(4, Date.valueOf(license.getIssueDate()));
            stmt.setDate(5, Date.valueOf(license.getExpirationDate()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting pilot license", e);
        }
    }

    private void update(PilotLicense license) {
        String sql = "UPDATE pilot_license SET license_number = ?, license_type = ?, issue_date = ?, expiration_date = ? WHERE pilot_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, license.getLicenseNumber());
            stmt.setString(2, license.getLicenseType().name());
            stmt.setDate(3, Date.valueOf(license.getIssueDate()));
            stmt.setDate(4, Date.valueOf(license.getExpirationDate()));
            stmt.setInt(5, license.getPilotId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating pilot license", e);
        }
    }

    @Override
    public void delete(PilotLicense license) {
        String sql = "DELETE FROM pilot_license WHERE pilot_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, license.getPilotId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting pilot license", e);
        }
    }

    @Override
    public PilotLicense get(Integer id) {
        String sql = "SELECT * FROM pilot_license WHERE pilot_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching pilot license by ID", e);
        }
        return null;
    }

    @Override
    public Set<PilotLicense> getAll() {
        Set<PilotLicense> licenses = new HashSet<>();
        String sql = "SELECT * FROM pilot_license";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                licenses.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all pilot licenses", e);
        }
        return licenses;
    }

    private PilotLicense mapRow(ResultSet rs) throws SQLException {
        var license = new PilotLicenseImpl();
        license.setPilotId(rs.getInt("pilot_id"));
        license.setLicenseNumber(rs.getString("license_number"));
        license.setLicenseType(LicenseType.valueOf(rs.getString("license_type")));
        license.setIssueDate(rs.getDate("issue_date").toLocalDate());
        license.setExpirationDate(rs.getDate("expiration_date").toLocalDate());
        return license;
    }

    @Override
    public Set<PilotLicense> getByLicenseType(LicenseType licenseType) {
        return Set.of();
    }

    @Override
    public PilotLicense create() {
        return null;
    }
}