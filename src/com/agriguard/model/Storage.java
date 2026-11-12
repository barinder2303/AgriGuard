package com.agriguard.model;

public class Storage {

    private int storageId;
    private String location;
    private double totalCapacity;
    private double occupiedCapacity;
    private double temperature;
    private double humidity;

    public Storage(int storageId,
                   String location,
                   double totalCapacity,
                   double occupiedCapacity,
                   double temperature,
                   double humidity) {

        this.storageId = storageId;
        this.location = location;
        this.totalCapacity = totalCapacity;
        this.occupiedCapacity = occupiedCapacity;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public int getStorageId() {
        return storageId;
    }

    public String getLocation() {
        return location;
    }

    public double getTotalCapacity() {
        return totalCapacity;
    }

    public double getOccupiedCapacity() {
        return occupiedCapacity;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getAvailableCapacity() {
        return totalCapacity - occupiedCapacity;
    }
}