package cat.uvic.teknos.dam.aeroadmin.model.model;

public interface Aircraft {
    int getAircraftId();

    void setAircraftId(int aircraftId);

    Airline getAirline();

    void setAirline(Airline airline);

    String getModel();

    void setModel(String model);

    String getManufacturer();

    void setManufacturer(String manufacturer);

    String getRegistrationNumber();

    void setRegistrationNumber(String registrationNumber);

    int getProductionYear();

    void setProductionYear(int productionYear);

    AircraftDetail getDetails();

    void setDetails(AircraftDetail details);
}
