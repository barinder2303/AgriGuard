package com.agriguard.model;

public class Produce {

    private int produceId;
    private String cropName;
    private double quantity;
    private int harvestAge;
    private double temperature;
    private double humidity;
    private String qualityGrade;

    public Produce(int produceId,
                   String cropName,
                   double quantity,
                   int harvestAge,
                   double temperature,
                   double humidity,
                   String qualityGrade) {

        this.produceId = produceId;
        this.cropName = cropName;
        this.quantity = quantity;
        this.harvestAge = harvestAge;
        this.temperature = temperature;
        this.humidity = humidity;
        this.qualityGrade = qualityGrade;
    }

    public int getProduceId() {
        return produceId;
    }

    public String getCropName() {
        return cropName;
    }

    public double getQuantity() {
        return quantity;
    }

    public int getHarvestAge() {
        return harvestAge;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public String getQualityGrade() {
        return qualityGrade;
    }
}