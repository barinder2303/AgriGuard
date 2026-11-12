package com.agriguard.model;

public class CropProfile {

    private String cropName;
    private double idealTemperature;
    private double maxTemperature;
    private double idealHumidity;
    private double maxHumidity;
    private int maxStorageDays;

    public CropProfile(String cropName,
                       double idealTemperature,
                       double maxTemperature,
                       double idealHumidity,
                       double maxHumidity,
                       int maxStorageDays) {

        this.cropName = cropName;
        this.idealTemperature = idealTemperature;
        this.maxTemperature = maxTemperature;
        this.idealHumidity = idealHumidity;
        this.maxHumidity = maxHumidity;
        this.maxStorageDays = maxStorageDays;
    }

    public String getCropName() {
        return cropName;
    }

    public double getIdealTemperature() {
        return idealTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public double getIdealHumidity() {
        return idealHumidity;
    }

    public double getMaxHumidity() {
        return maxHumidity;
    }

    public int getMaxStorageDays() {
        return maxStorageDays;
    }
}