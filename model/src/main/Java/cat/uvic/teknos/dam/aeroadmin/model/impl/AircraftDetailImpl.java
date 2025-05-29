package cat.uvic.teknos.dam.aeroadmin.model.impl;

import cat.uvic.teknos.dam.aeroadmin.model.model.AircraftDetail;

public class AircraftDetailImpl implements AircraftDetail {
    private int aircraftId;
    private int passengerCapacity;
    private int maxRangeKm;
    private int maxSpeedKmh;
    private int fuelCapacityLiters;

    @Override
    public int getAircraftId() {
        return aircraftId;
    }

    @Override
    public void setAircraftId(int aircraftId) {
        this.aircraftId = aircraftId;
    }

    @Override
    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    @Override
    public void setPassengerCapacity(int passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }

    @Override
    public int getMaxRangeKm() {
        return maxRangeKm;
    }

    @Override
    public void setMaxRangeKm(int maxRangeKm) {
        this.maxRangeKm = maxRangeKm;
    }

    @Override
    public int getMaxSpeedKmh() {
        return maxSpeedKmh;
    }

    @Override
    public void setMaxSpeedKmh(int maxSpeedKmh) {
        this.maxSpeedKmh = maxSpeedKmh;
    }

    @Override
    public int getFuelCapacityLiters() {
        return fuelCapacityLiters;
    }

    @Override
    public void setFuelCapacityLiters(int fuelCapacityLiters) {
        this.fuelCapacityLiters = fuelCapacityLiters;
    }
}