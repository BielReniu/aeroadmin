package cat.uvic.teknos.dam.aeroadmin.model.model;

public interface Airline {
    int getAirlineId();

    void setAirlineId(int airlineId);

    String getName();

    void setName(String name);

    String getIataCode();

    void setIataCode(String iataCode);

    String getIcaoCode();

    void setIcaoCode(String icaoCode);

    String getCountry();

    void setCountry(String country);

    Integer getFoundationYear();

    void setFoundationYear(Integer foundationYear);

    String getWebsite();

    void setWebsite(String website);

    void setCode(String ib);
}