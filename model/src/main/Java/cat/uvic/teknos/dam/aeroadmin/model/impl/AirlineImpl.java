package cat.uvic.teknos.dam.aeroadmin.model.impl;

import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;

public class AirlineImpl implements Airline {
    private int airlineId;
    private String airlineName; // Canviat de "name" a "airlineName"
    private String iataCode;
    private String icaoCode;
    private String country;
    private Integer foundationYear;
    private String website;

    @Override
    public int getAirlineId() {
        return airlineId;
    }

    @Override
    public void setAirlineId(int airlineId) {
        this.airlineId = airlineId;
    }

    // Mètodes canviats de "getName" a "getAirlineName"
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

    // El mètode setCode s'ha eliminat
}