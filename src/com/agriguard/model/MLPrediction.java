
package com.agriguard.model;

public class MLPrediction {

    private int predictionId;
    private String fruit;
    private double temperature;
    private double humidity;
    private double light;
    private double co2;
    private String prediction;
    private double confidence;

    public MLPrediction(
            int predictionId,
            String fruit,
            double temperature,
            double humidity,
            double light,
            double co2,
            String prediction,
            double confidence) {

        this.predictionId = predictionId;
        this.fruit = fruit;
        this.temperature = temperature;
        this.humidity = humidity;
        this.light = light;
        this.co2 = co2;
        this.prediction = prediction;
        this.confidence = confidence;
    }

    public int getPredictionId() {
        return predictionId;
    }

    public String getFruit() {
        return fruit;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getLight() {
        return light;
    }

    public double getCo2() {
        return co2;
    }

    public String getPrediction() {
        return prediction;
    }

    public double getConfidence() {
        return confidence;
    }
}
