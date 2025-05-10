package cat.uvic.teknos.dam.aeroadmin.impl;

import cat.uvic.teknos.dam.aeroadmin.model.Aircraft;
import cat.uvic.teknos.dam.aeroadmin.model.AircraftDetail;
import cat.uvic.teknos.dam.aeroadmin.model.Airline;

public class AircraftImpl implements Aircraft {
    private int aircraftId;
    private Airline airline;
    private String model;
    private String manufacturer;
    private String registrationNumber;
    private int productionYear;
    private AircraftDetail details;

    @Override
    public int getAircraftId() { return aircraftId; }

    @Override
    public void setAircraftId(int aircraftId) { this.aircraftId = aircraftId; }

    @Override
    public Airline getAirline() { return airline; }

    @Override
    public void setAirline(Airline airline) { this.airline = airline; }

    @Override
    public String getModel() { return model; }

    @Override
    public void setModel(String model) { this.model = model; }

    @Override
    public String getManufacturer() { return manufacturer; }

    @Override
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    @Override
    public String getRegistrationNumber() { return registrationNumber; }

    @Override
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    @Override
    public int getProductionYear() { return productionYear; }

    @Override
    public void setProductionYear(int productionYear) { this.productionYear = productionYear; }

    @Override
    public AircraftDetail getDetails() { return details; }

    @Override
    public void setDetails(AircraftDetail details) { this.details = details; }
}