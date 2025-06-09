package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.model.model.Pilot;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.model.impl.PilotImpl;
import cat.uvic.teknos.dam.aeroadmin.repositories.PilotRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.SingleConnectionDataSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class JdbcPilotRepository implements PilotRepository {

    private final DataSource dataSource;

    public JdbcPilotRepository(DataSource dataSource) {
        this.dataSource = new DataSource() {
            @Override
            public Connection getConnection() {
                return null;
            }
        };
    }

    @Override
    public void save(Pilot pilot) {
        if (pilot.getPilotId() == 0) {
            insert(pilot);
        } else {
            update(pilot);
        }
    }

    private void insert(Pilot pilot) {
        String sql = "INSERT INTO pilot (first_name, last_name, date_of_birth, nationality, airline_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pilot.getFirstName());
            stmt.setString(2, pilot.getLastName());
            stmt.setDate(3, java.sql.Date.valueOf(pilot.getDateOfBirth()));
            stmt.setString(4, pilot.getNationality());
            stmt.setInt(5, pilot.getAirline().getAirlineId());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    pilot.setPilotId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting pilot", e);
        }
    }

    private void update(Pilot pilot) {
        String sql = "UPDATE pilot SET first_name = ?, last_name = ?, date_of_birth = ?, nationality = ?, airline_id = ? WHERE pilot_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pilot.getFirstName());
            stmt.setString(2, pilot.getLastName());
            stmt.setDate(3, java.sql.Date.valueOf(pilot.getDateOfBirth()));
            stmt.setString(4, pilot.getNationality());
            stmt.setInt(5, pilot.getAirline().getAirlineId());
            stmt.setInt(6, pilot.getPilotId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating pilot", e);
        }
    }

    @Override
    public void delete(Pilot pilot) {
        String sql = "DELETE FROM pilot WHERE pilot_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pilot.getPilotId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting pilot", e);
        }
    }

    @Override
    public Pilot get(Integer id) {
        String sql = "SELECT * FROM pilot WHERE pilot_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching pilot by ID", e);
        }
        return null;
    }

    @Override
    public Set<Pilot> getAll() {
        Set<Pilot> pilots = new HashSet<>();
        String sql = "SELECT * FROM pilot";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                pilots.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all pilots", e);
        }
        return pilots;
    }

    private Pilot mapRow(ResultSet rs) throws SQLException {
        var pilot = new PilotImpl();
        pilot.setPilotId(rs.getInt("pilot_id"));
        pilot.setFirstName(rs.getString("first_name"));
        pilot.setLastName(rs.getString("last_name"));
        pilot.setDateOfBirth(LocalDate.parse(rs.getString("date_of_birth")));
        pilot.setNationality(rs.getString("nationality"));

        int airlineId = rs.getInt("airline_id");
        Airline airline = new JdbcAirlineRepository(dataSource).get(airlineId);
        pilot.setAirline(airline);

        return pilot;
    }

    @Override
    public Set<Pilot> getByAirline(Airline airline) {
        return Set.of();
    }

    @Override
    public Pilot create() {
        return null;
    }
}