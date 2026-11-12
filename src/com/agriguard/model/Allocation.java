package com.agriguard.model;

public class Allocation {

    private int allocationId;
    private int produceId;
    private int buyerId;
    private double allocatedQuantity;
    private double matchScore;
    private String status;

    public Allocation(int allocationId,
                      int produceId,
                      int buyerId,
                      double allocatedQuantity,
                      double matchScore,
                      String status) {

        this.allocationId = allocationId;
        this.produceId = produceId;
        this.buyerId = buyerId;
        this.allocatedQuantity = allocatedQuantity;
        this.matchScore = matchScore;
        this.status = status;
    }

    public int getAllocationId() {
        return allocationId;
    }

    public int getProduceId() {
        return produceId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public double getAllocatedQuantity() {
        return allocatedQuantity;
    }

    public double getMatchScore() {
        return matchScore;
    }

    public String getStatus() {
        return status;
    }
}