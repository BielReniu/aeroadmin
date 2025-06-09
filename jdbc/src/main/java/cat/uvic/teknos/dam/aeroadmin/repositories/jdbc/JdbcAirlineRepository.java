// src/main/java/cat/uvic/teknos/dam/aeroadmin/repositories/jdbc/JdbcAirlineRepository.java
package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc;

import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import cat.uvic.teknos.dam.aeroadmin.repositories.AirlineRepository;
import cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JdbcAirlineRepository implements AirlineRepository {
    private final DataSource dataSource;

    public JdbcAirlineRepository(cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.datasources.DataSource dataSource) {
        // o bé injecta’l des de la fàbrica
        this.dataSource = new SingleConnectionDataSource();
    }

    @Override
    public Set<Airline> getAll() {
        List<Airline> airlines = new ArrayList<>();
        String sql = "SELECT airline_id, name, iata_code FROM airlines";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Airline a = new Airline() {
                    @Override
                    public int getAirlineId() {
                        return 0;
                    }

                    @Override
                    public void setAirlineId(int airlineId) {

                    }

                    @Override
                    public String getName() {
                        return "";
                    }

                    @Override
                    public void setName(String name) {

                    }

                    @Override
                    public String getIataCode() {
                        return "";
                    }

                    @Override
                    public void setIataCode(String iataCode) {

                    }

                    @Override
                    public String getIcaoCode() {
                        return "";
                    }

                    @Override
                    public void setIcaoCode(String icaoCode) {

                    }

                    @Override
                    public String getCountry() {
                        return "";
                    }

                    @Override
                    public void setCountry(String country) {

                    }

                    @Override
                    public Integer getFoundationYear() {
                        return 0;
                    }

                    @Override
                    public void setFoundationYear(Integer foundationYear) {

                    }

                    @Override
                    public String getWebsite() {
                        return "";
                    }

                    @Override
                    public void setWebsite(String website) {

                    }

                    @Override
                    public void setCode(String ib) {

                    }
                };
                a.setAirlineId(rs.getInt("airline_id"));
                a.setName(rs.getString("name"));
                a.setIataCode(rs.getString("iata_code"));
                airlines.add(a);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching airlines", e);
        }
        return (Set<Airline>) airlines;
    }

    @Override
    public Airline get(int id) {
        String sql = "SELECT airline_id, name, iata_code FROM airlines WHERE airline_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Airline a = new Airline() {
                        @Override
                        public int getAirlineId() {
                            return 0;
                        }

                        @Override
                        public void setAirlineId(int airlineId) {

                        }

                        @Override
                        public String getName() {
                            return "";
                        }

                        @Override
                        public void setName(String name) {

                        }

                        @Override
                        public String getIataCode() {
                            return "";
                        }

                        @Override
                        public void setIataCode(String iataCode) {

                        }

                        @Override
                        public String getIcaoCode() {
                            return "";
                        }

                        @Override
                        public void setIcaoCode(String icaoCode) {

                        }

                        @Override
                        public String getCountry() {
                            return "";
                        }

                        @Override
                        public void setCountry(String country) {

                        }

                        @Override
                        public Integer getFoundationYear() {
                            return 0;
                        }

                        @Override
                        public void setFoundationYear(Integer foundationYear) {

                        }

                        @Override
                        public String getWebsite() {
                            return "";
                        }

                        @Override
                        public void setWebsite(String website) {

                        }

                        @Override
                        public void setCode(String ib) {

                        }
                    };
                    a.setAirlineId(rs.getInt("airline_id"));
                    a.setName(rs.getString("name"));
                    a.setIataCode(rs.getString("iata_code"));
                    return a;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching airline by ID", e);
        }
        return null;
    }

    @Override
    public void save(Airline entity) {
        if (entity.getAirlineId() == 0) {
            // insert
            String sql = "INSERT INTO airlines (name, iata_code) VALUES (?, ?)";
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, entity.getName());
                ps.setString(2, entity.getIataCode());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        entity.setAirlineId(keys.getInt(1));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error inserting airline", e);
            }
        } else {
            // update
            String sql = "UPDATE airlines SET name = ?, iata_code = ? WHERE airline_id = ?";
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, entity.getName());
                ps.setString(2, entity.getIataCode());
                ps.setInt(3, entity.getAirlineId());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error updating airline", e);
            }
        }
    }

    @Override
    public void delete(Airline entity) {
        String sql = "DELETE FROM airlines WHERE airline_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getAirlineId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting airline", e);
        }
    }

    @Override
    public Airline get(Integer id) {
        return null;
    }

    @Override
    public Set<Airline> getByCountry(String country) {
        return Set.of();
    }

    @Override
    public Airline create() {
        return null;
    }
}
