package cat.uvic.teknos.dam.aeroadmin.model.jpa;

import cat.uvic.teknos.dam.aeroadmin.model.model.Aircraft;
import cat.uvic.teknos.dam.aeroadmin.model.model.AircraftDetail;
import cat.uvic.teknos.dam.aeroadmin.model.model.Airline;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "details_id")
    private JpaAircraftDetail details;

    @ManyToOne
    @JoinColumn(name = "airline_id")
    private JpaAirline airline;

    @OneToMany(mappedBy = "aircraft", cascade = CascadeType.ALL)
    private Set<JpaFlight> flights = new HashSet<>();

    // Getters y setters

    @Override
    public int getAircraftId() {
        return aircraftId;
    }

    @Override
    public void setAircraftId(int aircraftId) {
        this.aircraftId = aircraftId;
    }

    @Override
    public Airline getAirline() {
        return airline;
    }

    @Override
    public void setAirline(Airline airline) {
        this.airline = (JpaAirline) airline;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    @Override
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    @Override
    public int getProductionYear() {
        return productionYear;
    }

    @Override
    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    @Override
    public AircraftDetail getDetails() {
        return details;
    }

    @Override
    public void setDetails(AircraftDetail details) {
        this.details = (JpaAircraftDetail) details;
    }
}