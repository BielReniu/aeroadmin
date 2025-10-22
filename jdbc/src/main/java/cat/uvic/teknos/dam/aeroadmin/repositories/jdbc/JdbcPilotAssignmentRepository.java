package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.model.enums.AssignmentRole;
import cat.uvic.teknos.dam.aeroadmin.model.impl.FlightImpl;
import cat.uvic.teknos.dam.aeroadmin.model.impl.PilotImpl;
import cat.uvic.teknos.dam.aeroadmin.model.impl.PilotLicenseImpl;
import cat.uvic.teknos.dam.aeroadmin.model.model.Flight;
import cat.uvic.teknos.dam.aeroadmin.model.model.Pilot;
import cat.uvic.teknos.dam.aeroadmin.model.model.PilotAssignment;
import cat.uvic.teknos.dam.aeroadmin.model.impl.PilotAssignmentImpl;
import cat.uvic.teknos.dam.aeroadmin.model.model.PilotLicense;
import cat.uvic.teknos.dam.aeroadmin.repositories.PilotAssignmentRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.DataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcPilotAssignmentRepository implements PilotAssignmentRepository {

    private final DataSource dataSource;

    public JdbcPilotAssignmentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // ... els mètodes save, insert, update, delete, get, getAll i getByFlight ja estaven correctes ...
    @Override
    public void save(PilotAssignment assignment) {
        if (assignment.getAssignmentId() == 0) {
            insert(assignment);
        } else {
            update(assignment);
        }
    }

    private void insert(PilotAssignment assignment) {
        String sql = "INSERT INTO pilot_assignment (flight_id, pilot_id, role, lead_pilot, assigned_hours) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, assignment.getFlight().getFlightId());
            stmt.setInt(2, assignment.getPilot().getPilotId());
            stmt.setString(3, assignment.getRole().name());
            stmt.setBoolean(4, assignment.isLeadPilot());
            stmt.setBigDecimal(5, assignment.getAssignedHours());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    assignment.setAssignmentId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting pilot assignment", e);
        }
    }

    private void update(PilotAssignment assignment) {
        String sql = "UPDATE pilot_assignment SET flight_id = ?, pilot_id = ?, role = ?, lead_pilot = ?, assigned_hours = ? WHERE assignment_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, assignment.getFlight().getFlightId());
            stmt.setInt(2, assignment.getPilot().getPilotId());
            stmt.setString(3, assignment.getRole().name());
            stmt.setBoolean(4, assignment.isLeadPilot());
            stmt.setBigDecimal(5, assignment.getAssignedHours());
            stmt.setInt(6, assignment.getAssignmentId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating pilot assignment", e);
        }
    }

    @Override
    public void delete(PilotAssignment assignment) {
        String sql = "DELETE FROM pilot_assignment WHERE assignment_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, assignment.getAssignmentId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting pilot assignment", e);
        }
    }

    private static final String QUERY_BASE = "SELECT pa.*, " +
            "f.flight_number, f.departure_airport, f.arrival_airport, f.scheduled_departure, " +
            "p.first_name, p.last_name, pl.license_number " + // Canviat p.license_number per pl.license_number
            "FROM pilot_assignment pa " +
            "LEFT JOIN flight f ON pa.flight_id = f.flight_id " +
            "LEFT JOIN pilot p ON pa.pilot_id = p.pilot_id " +
            "LEFT JOIN pilot_license pl ON p.pilot_id = pl.pilot_id "; // Afegit JOIN a pilot_license

    @Override
    public PilotAssignment get(Integer id) {
        String sql = QUERY_BASE + "WHERE pa.assignment_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching pilot assignment by ID", e);
        }
        return null;
    }

    @Override
    public Set<PilotAssignment> getAll() {
        Set<PilotAssignment> assignments = new HashSet<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(QUERY_BASE)) {
            while (rs.next()) {
                assignments.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all pilot assignments", e);
        }
        return assignments;
    }

    @Override
    public Set<PilotAssignment> getByFlight(Flight flight) {
        Set<PilotAssignment> assignments = new HashSet<>();
        String sql = QUERY_BASE + "WHERE pa.flight_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, flight.getFlightId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    assignments.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching pilot assignments by flight", e);
        }
        return assignments;
    }

    @Override
    public PilotAssignment create() {
        return new PilotAssignmentImpl();
    }

    private PilotAssignment mapRow(ResultSet rs) throws SQLException {
        PilotAssignment assignment = new PilotAssignmentImpl();
        assignment.setAssignmentId(rs.getInt("assignment_id"));
        assignment.setRole(AssignmentRole.valueOf(rs.getString("role")));
        assignment.setLeadPilot(rs.getBoolean("lead_pilot"));
        assignment.setAssignedHours(rs.getBigDecimal("assigned_hours"));

        // Mapejar Flight des del JOIN
        int flightId = rs.getInt("flight_id");
        if (!rs.wasNull()) {
            Flight flight = new FlightImpl();
            flight.setFlightId(flightId);
            flight.setFlightNumber(rs.getString("flight_number"));
            flight.setDepartureAirport(rs.getString("departure_airport"));
            flight.setArrivalAirport(rs.getString("arrival_airport"));

            // CORRECCIÓ: Comprovem si el timestamp és null
            Timestamp departureTimestamp = rs.getTimestamp("scheduled_departure");
            if (departureTimestamp != null) {
                flight.setScheduledDeparture(departureTimestamp.toLocalDateTime());
            }

            assignment.setFlight(flight);
        }

        // Mapejar Pilot des del JOIN
        int pilotId = rs.getInt("pilot_id");
        if (!rs.wasNull()) {
            Pilot pilot = new PilotImpl();
            pilot.setPilotId(pilotId);
            pilot.setFirstName(rs.getString("first_name"));
            pilot.setLastName(rs.getString("last_name"));

            // CORRECCIÓ: Creem i assignem la llicència al pilot
            String licenseNumber = rs.getString("license_number");
            if (licenseNumber != null) {
                PilotLicense license = new PilotLicenseImpl();
                license.setLicenseNumber(licenseNumber);
                // Aquí podríem omplir més dades de la llicència si les tinguéssim al JOIN
                pilot.setLicense(license);
            }

            assignment.setPilot(pilot);
        }

        return assignment;
    }
}