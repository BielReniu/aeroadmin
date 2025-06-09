package cat.uvic.teknos.dam.aeroadmin.jpa.model;

import cat.uvic.teknos.dam.aeroadmin.model.model.AircraftDetail;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import jakarta.persistence.*;
import cat.uvic.teknos.dam.aeroadmin.model.model.Aircraft;

@Entity
@Table(name = "aircraft")
public class JpaAircraft implements Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int aircraftId;

    private String model;
    private String manufacturer;
    private String registrationNumber;
    private int productionYear;

    // Getters i setters

    @Override
    public int getAircraftId() { return aircraftId; }
    @Override
    public void setAircraftId(int id) { this.aircraftId = id; }

    @Override
    public Airline getAirline() {
        return null;
    }

    @Override
    public void setAirline(Airline airline) {

    }

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
    public void setRegistrationNumber(String number) { this.registrationNumber = number; }

    @Override
    public int getProductionYear() { return productionYear; }
    @Override
    public void setProductionYear(int year) { this.productionYear = year; }

    @Override
    public AircraftDetail getDetails() {
        return null;
    }

    @Override
    public void setDetails(AircraftDetail details) {

    }
}