package com.agriguard.service;

import com.agriguard.model.Produce;
import com.agriguard.model.RiskResult;
import com.agriguard.model.Storage;

public class DecisionEngine {

    public String makeDecision(Produce produce,
                               RiskResult riskResult,
                               Storage storage) {

        double availableStorage = storage.getAvailableCapacity();
        double requiredStorage = produce.getQuantity();

        // Critical risk → immediate distribution
        if (riskResult.getRiskLevel().equalsIgnoreCase("CRITICAL")) {
            return "DISTRIBUTE";
        }

        // High risk → process if storage is available,
        // otherwise distribute
        if (riskResult.getRiskLevel().equalsIgnoreCase("HIGH")) {

            if (availableStorage >= requiredStorage) {
                return "PROCESS";
            } else {
                return "DISTRIBUTE";
            }
        }

        // Medium risk → prioritize based on storage availability
        if (riskResult.getRiskLevel().equalsIgnoreCase("MEDIUM")) {

            if (availableStorage >= requiredStorage) {
                return "STORE";
            } else {
                return "PRIORITIZE";
            }
        }

        // Low risk → store if possible
        if (availableStorage >= requiredStorage) {
            return "STORE";
        }

        return "PRIORITIZE";
    }
}