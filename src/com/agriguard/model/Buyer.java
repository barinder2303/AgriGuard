package com.agriguard.model;

public class Buyer {

    private int buyerId;
    private String buyerName;
    private String location;
    private String cropRequired;
    private double quantityRequired;
    private String minimumQuality;
    private int priority;

    public Buyer(int buyerId,
                 String buyerName,
                 String location,
                 String cropRequired,
                 double quantityRequired,
                 String minimumQuality,
                 int priority) {

        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.location = location;
        this.cropRequired = cropRequired;
        this.quantityRequired = quantityRequired;
        this.minimumQuality = minimumQuality;
        this.priority = priority;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getLocation() {
        return location;
    }

    public String getCropRequired() {
        return cropRequired;
    }

    public double getQuantityRequired() {
        return quantityRequired;
    }

    public String getMinimumQuality() {
        return minimumQuality;
    }

    public int getPriority() {
        return priority;
    }
}