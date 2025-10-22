package cat.uvic.teknos.dam.aeroadmin.repositories.jdbc.model;

public class JdbcAircraftDetail {

    private int aircraftId;
    private int passengerCapacity;
    private int maxRangeKm;
    private int maxSpeedKmh;
    private int fuelCapacityLiters;

    // Getters i Setters
    public int getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(int aircraftId) {
        this.aircraftId = aircraftId;
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public void setPassengerCapacity(int passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }

    public int getMaxRangeKm() {
        return maxRangeKm;
    }

    public void setMaxRangeKm(int maxRangeKm) {
        this.maxRangeKm = maxRangeKm;
    }

    public int getMaxSpeedKmh() {
        return maxSpeedKmh;
    }

    public void setMaxSpeedKmh(int maxSpeedKmh) {
        this.maxSpeedKmh = maxSpeedKmh;
    }

    public int getFuelCapacityLiters() {
        return fuelCapacityLiters;
    }

    public void setFuelCapacityLiters(int fuelCapacityLiters) {
        this.fuelCapacityLiters = fuelCapacityLiters;
    }
}