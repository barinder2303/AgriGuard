package com.agriguard.service;

import com.agriguard.model.Produce;
import com.agriguard.model.Storage;

public class StorageAnalyzer {

    public boolean canStore(Produce produce, Storage storage) {

        return storage.getAvailableCapacity()
                >= produce.getQuantity();
    }

    public double getUtilizationPercentage(Storage storage) {

        if (storage.getTotalCapacity() <= 0) {
            return 0;
        }

        return (storage.getOccupiedCapacity()
                / storage.getTotalCapacity()) * 100;
    }

    public double getAvailableCapacity(Storage storage) {

        return storage.getAvailableCapacity();
    }

    public String getStorageStatus(Storage storage) {

        double utilization =
                getUtilizationPercentage(storage);

        if (utilization >= 90) {
            return "CRITICAL";
        } else if (utilization >= 75) {
            return "HIGH";
        } else if (utilization >= 50) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    public String getStorageRecommendation(
            Produce produce,
            Storage storage) {

        if (canStore(produce, storage)) {

            String status = getStorageStatus(storage);

            if (status.equals("CRITICAL")) {
                return "Storage is available, but capacity is critically high.";
            }

            if (status.equals("HIGH")) {
                return "Storage is available, but monitor capacity closely.";
            }

            return "Produce can be safely placed in available storage.";
        }

        return "Insufficient storage capacity. Consider distribution or processing.";
    }
}