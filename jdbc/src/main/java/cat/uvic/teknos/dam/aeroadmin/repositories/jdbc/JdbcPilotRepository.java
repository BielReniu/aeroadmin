package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.model.impl.AirlineImpl;
import cat.uvic.teknos.dam.aeroadmin.model.model.Pilot;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.model.impl.PilotImpl;
import cat.uvic.teknos.dam.aeroadmin.repositories.PilotRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.DataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcPilotRepository implements PilotRepository {

    private final DataSource dataSource;

    // 1. CONSTRUCTOR CORREGIT
    public JdbcPilotRepository(DataSource dataSource) {
        this.dataSource = dataSource;
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
            if (pilot.getAirline() != null) {
                stmt.setInt(5, pilot.getAirline().getAirlineId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

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
            if (pilot.getAirline() != null) {
                stmt.setInt(5, pilot.getAirline().getAirlineId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
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

    // 2. Base de la consulta optimitzada amb JOIN
    private static final String QUERY_BASE = "SELECT p.*, al.airline_name, al.iata_code, al.icao_code " +
            "FROM pilot p " +
            "LEFT JOIN airline al ON p.airline_id = al.airline_id ";

    @Override
    public Pilot get(Integer id) {
        String sql = QUERY_BASE + "WHERE p.pilot_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching pilot by ID", e);
        }
        return null;
    }

    @Override
    public Set<Pilot> getAll() {
        Set<Pilot> pilots = new HashSet<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(QUERY_BASE)) {
            while (rs.next()) {
                pilots.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all pilots", e);
        }
        return pilots;
    }

    // 3. MÈTODE IMPLEMENTAT
    @Override
    public Set<Pilot> getByAirline(Airline airline) {
        Set<Pilot> pilots = new HashSet<>();
        String sql = QUERY_BASE + "WHERE p.airline_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, airline.getAirlineId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pilots.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching pilots by airline", e);
        }
        return pilots;
    }

    @Override
    public Pilot create() {
        return new PilotImpl();
    }

    private Pilot mapRow(ResultSet rs) throws SQLException {
        Pilot pilot = new PilotImpl();
        pilot.setPilotId(rs.getInt("pilot_id"));
        pilot.setFirstName(rs.getString("first_name"));
        pilot.setLastName(rs.getString("last_name"));
        pilot.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        pilot.setNationality(rs.getString("nationality"));

        // Mapejar Airline des del JOIN
        int airlineId = rs.getInt("airline_id");
        if (!rs.wasNull()) {
            Airline airline = new AirlineImpl();
            airline.setAirlineId(airlineId);
            airline.setAirlineName(rs.getString("airline_name"));
            airline.setIataCode(rs.getString("iata_code"));
            airline.setIcaoCode(rs.getString("icao_code"));
            pilot.setAirline(airline);
        }

        return pilot;
    }
}