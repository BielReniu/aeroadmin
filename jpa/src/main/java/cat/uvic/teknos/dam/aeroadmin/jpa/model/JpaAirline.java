package cat.uvic.teknos.dam.aeroadmin.jpa.model;

import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import jakarta.persistence.*;

@Entity
@Table(name = "airline")
public class JpaAirline implements Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "airline_id")
    private int airlineId;

    @Column(name = "airline_name")
    private String airlineName;

    @Column(name = "iata_code")
    private String iataCode;

    @Column(name = "icao_code")
    private String icaoCode;

    private String country;

    @Column(name = "foundation_year")
    private Integer foundationYear;

    private String website;

    // Getters i Setters que implementen la interfície Airline

    @Override
    public int getAirlineId() {
        return airlineId;
    }

    @Override
    public void setAirlineId(int airlineId) {
        this.airlineId = airlineId;
    }

    @Override
    public String getAirlineName() {
        return airlineName;
    }

    @Override
    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    @Override
    public String getIataCode() {
        return iataCode;
    }

    @Override
    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    @Override
    public String getIcaoCode() {
        return icaoCode;
    }

    @Override
    public void setIcaoCode(String icaoCode) {
        this.icaoCode = icaoCode;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public Integer getFoundationYear() {
        return foundationYear;
    }

    @Override
    public void setFoundationYear(Integer foundationYear) {
        this.foundationYear = foundationYear;
    }

    @Override
    public String getWebsite() {
        return website;
    }

    @Override
    public void setWebsite(String website) {
        this.website = website;
    }
}