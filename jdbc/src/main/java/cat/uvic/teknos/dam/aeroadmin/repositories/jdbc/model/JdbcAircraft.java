package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.model;

// Aquesta classe representa l'entitat 'Aircraft'
public class JdbcAircraft {

    private int aircraftId;
    private String model;
    private String manufacturer;
    private String registrationNumber;
    private int productionYear;
    private JdbcAirline airline; // Utilitzem la teva classe JdbcAirline

    // Getters i Setters per a cada propietat

    public int getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(int aircraftId) {
        this.aircraftId = aircraftId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public JdbcAirline getAirline() {
        return airline;
    }

    public void setAirline(JdbcAirline airline) {
        this.airline = airline;
    }
}