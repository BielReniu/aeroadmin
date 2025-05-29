package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.model.impl.AirlineImpl;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.SingleConnectionDataSource;
import cat.uvic.teknos.dam.aeroadmin.repositories.AirlineRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcAirlineRepository implements AirlineRepository {

    private final DataSource dataSource;

    public JdbcAirlineRepository(DataSource dataSource) {
        this.dataSource = new SingleConnectionDataSource();
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
        String sql = "INSERT INTO airline (name, iata_code, icao_code, country, foundation_year, website) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, airline.getName());
            stmt.setString(2, airline.getIataCode());
            stmt.setString(3, airline.getIcaoCode());
            stmt.setString(4, airline.getCountry());
            stmt.setInt(5, airline.getFoundationYear());
            stmt.setString(6, airline.getWebsite());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    airline.setAirlineId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting airline", e);
        }
    }

    private void update(Airline airline) {
        String sql = "UPDATE airline SET name = ?, iata_code = ?, icao_code = ?, country = ?, foundation_year = ?, website = ? WHERE airline_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, airline.getName());
            stmt.setString(2, airline.getIataCode());
            stmt.setString(3, airline.getIcaoCode());
            stmt.setString(4, airline.getCountry());
            stmt.setInt(5, airline.getFoundationYear());
            stmt.setString(6, airline.getWebsite());
            stmt.setInt(7, airline.getAirlineId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating airline", e);
        }
    }

    @Override
    public void delete(Airline airline) {
        String sql = "DELETE FROM airline WHERE airline_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, airline.getAirlineId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting airline", e);
        }
    }

    @Override
    public Airline get(Integer id) {
        String sql = "SELECT * FROM airline WHERE airline_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
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

    private Airline mapRow(ResultSet rs) throws SQLException {
        var airline = new AirlineImpl();
        airline.setAirlineId(rs.getInt("airline_id"));
        airline.setName(rs.getString("name"));
        airline.setIataCode(rs.getString("iata_code"));
        airline.setIcaoCode(rs.getString("icao_code"));
        airline.setCountry(rs.getString("country"));
        airline.setFoundationYear(rs.getInt("foundation_year"));
        airline.setWebsite(rs.getString("website"));
        return airline;
    }

    @Override
    public Set<Airline> getByCountry(String country) {
        return Set.of();
    }
}