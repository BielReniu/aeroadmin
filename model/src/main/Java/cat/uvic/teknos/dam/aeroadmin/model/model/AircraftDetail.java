package cat.uvic.teknos.dam.aeroadmin.model.model;

public interface AircraftDetail {
    int getAircraftId();

    void setAircraftId(int aircraftId);

    int getPassengerCapacity();

    void setPassengerCapacity(int passengerCapacity);

    int getMaxRangeKm();

    void setMaxRangeKm(int maxRangeKm);

    int getMaxSpeedKmh();

    void setMaxSpeedKmh(int maxSpeedKmh);

    int getFuelCapacityLiters();

    void setFuelCapacityLiters(int fuelCapacityLiters);
}