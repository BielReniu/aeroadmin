package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.model.impl.AirlineImpl;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.repositories.AirlineRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.DataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcAirlineRepository implements AirlineRepository {

    private final DataSource dataSource;

    public JdbcAirlineRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Airline airline) {
        if (airline.getAirlineId() == 0) {
            insert(airline);
        } else {
            update(airline);
        }
    }

    private void insert(Airline airline) {
        String sql = "INSERT INTO airline (airline_name, iata_code, icao_code, country, foundation_year, website) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, airline.getAirlineName());
            ps.setString(2, airline.getIataCode());
            ps.setString(3, airline.getIcaoCode());
            ps.setString(4, airline.getCountry());
            ps.setObject(5, airline.getFoundationYear());
            ps.setString(6, airline.getWebsite());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    airline.setAirlineId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting airline", e);
        }
    }

    private void update(Airline airline) {
        String sql = "UPDATE airline SET airline_name = ?, iata_code = ?, icao_code = ?, country = ?, foundation_year = ?, website = ? WHERE airline_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, airline.getAirlineName());
            ps.setString(2, airline.getIataCode());
            ps.setString(3, airline.getIcaoCode());
            ps.setString(4, airline.getCountry());
            ps.setObject(5, airline.getFoundationYear());
            ps.setString(6, airline.getWebsite());
            ps.setInt(7, airline.getAirlineId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating airline", e);
        }
    }

    @Override
    public void delete(Airline airline) {
        String sql = "DELETE FROM airline WHERE airline_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, airline.getAirlineId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting airline", e);
        }
    }

    @Override
    public Airline get(Integer id) {
        String sql = "SELECT * FROM airline WHERE airline_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching airline by ID", e);
        }
        return null;
    }

    @Override
    public Set<Airline> getAll() {
        Set<Airline> airlines = new HashSet<>();
        String sql = "SELECT * FROM airline";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                airlines.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all airlines", e);
        }
        return airlines;
    }

    @Override
    public Set<Airline> getByCountry(String country) {
        Set<Airline> airlines = new HashSet<>();
        String sql = "SELECT * FROM airline WHERE country = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, country);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    airlines.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching airlines by country", e);
        }
        return airlines;
    }

    @Override
    public Airline create() {
        return new AirlineImpl();
    }

    private Airline mapRow(ResultSet rs) throws SQLException {
        Airline airline = new AirlineImpl();
        airline.setAirlineId(rs.getInt("airline_id"));
        airline.setAirlineName(rs.getString("airline_name"));
        airline.setIataCode(rs.getString("iata_code"));
        airline.setIcaoCode(rs.getString("icao_code"));
        airline.setCountry(rs.getString("country"));
        airline.setFoundationYear(rs.getObject("foundation_year", Integer.class));
        airline.setWebsite(rs.getString("website"));
        return airline;
    }
}