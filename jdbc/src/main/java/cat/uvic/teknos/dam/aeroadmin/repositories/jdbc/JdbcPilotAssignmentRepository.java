package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.model.enums.AssignmentRole;
import cat.uvic.teknos.dam.aeroadmin.model.model.Flight;
import cat.uvic.teknos.dam.aeroadmin.model.model.PilotAssignment;
import cat.uvic.teknos.dam.aeroadmin.model.impl.PilotAssignmentImpl;
import cat.uvic.teknos.dam.aeroadmin.repositories.PilotAssignmentRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.SingleConnectionDataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcPilotAssignmentRepository implements PilotAssignmentRepository {

    private final DataSource dataSource;

    public JdbcPilotAssignmentRepository(DataSource dataSource) {
        this.dataSource = new SingleConnectionDataSource();
    }

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

    @Override
    public PilotAssignment get(Integer id) {
        String sql = "SELECT * FROM pilot_assignment WHERE assignment_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching pilot assignment by ID", e);
        }
        return null;
    }

    @Override
    public Set<PilotAssignment> getAll() {
        Set<PilotAssignment> assignments = new HashSet<>();
        String sql = "SELECT * FROM pilot_assignment";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                assignments.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all pilot assignments", e);
        }
        return assignments;
    }

    private PilotAssignment mapRow(ResultSet rs) throws SQLException {
        var assignment = new PilotAssignmentImpl();
        assignment.setAssignmentId(rs.getInt("assignment_id"));
        assignment.setRole(AssignmentRole.valueOf(rs.getString("role")));
        assignment.setLeadPilot(rs.getBoolean("lead_pilot"));
        assignment.setAssignedHours(rs.getBigDecimal("assigned_hours"));

        // TODO: Load Flight and Pilot from DB
        assignment.setFlight(new JdbcFlightRepository(dataSource).get(rs.getInt("flight_id")));
        assignment.setPilot(new JdbcPilotRepository(dataSource).get(rs.getInt("pilot_id")));

        return assignment;
    }

    @Override
    public Set<PilotAssignment> getByFlight(Flight flight) {
        return Set.of();
    }
}