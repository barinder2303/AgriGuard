package com.agriguard.service;

import com.agriguard.model.CropProfile;
import com.agriguard.model.Produce;
import com.agriguard.model.RiskResult;

public class RiskAnalyzer {

    public RiskResult analyzeRisk(Produce produce, CropProfile profile) {

        double riskScore = 0;
        StringBuilder explanation = new StringBuilder();

        // 1. Temperature analysis
        if (produce.getTemperature() > profile.getMaxTemperature()) {
            riskScore += 30;
            explanation.append("Temperature is above safe limit. ");
        } else if (produce.getTemperature() > profile.getIdealTemperature()) {
            riskScore += 15;
            explanation.append("Temperature is above ideal range. ");
        }

        // 2. Humidity analysis
        if (produce.getHumidity() > profile.getMaxHumidity()) {
            riskScore += 25;
            explanation.append("Humidity is above safe limit. ");
        } else if (produce.getHumidity() > profile.getIdealHumidity()) {
            riskScore += 10;
            explanation.append("Humidity is above ideal range. ");
        }

        // 3. Harvest age analysis
        if (produce.getHarvestAge() > profile.getMaxStorageDays()) {
            riskScore += 30;
            explanation.append("Harvest age has exceeded storage limit. ");
        } else if (produce.getHarvestAge() >= profile.getMaxStorageDays() - 2) {
            riskScore += 15;
            explanation.append("Produce is approaching storage limit. ");
        }

        // 4. Quality analysis
        if (produce.getQualityGrade().equalsIgnoreCase("C")) {
            riskScore += 20;
            explanation.append("Quality grade is low. ");
        } else if (produce.getQualityGrade().equalsIgnoreCase("B")) {
            riskScore += 10;
            explanation.append("Quality grade is moderate. ");
        }

        // Maximum score = 100
        if (riskScore > 100) {
            riskScore = 100;
        }

        // 5. Determine risk level and action
        String riskLevel;
        String recommendedAction;

        if (riskScore >= 70) {
            riskLevel = "CRITICAL";
            recommendedAction = "DISTRIBUTE";
        } else if (riskScore >= 45) {
            riskLevel = "HIGH";
            recommendedAction = "PROCESS";
        } else if (riskScore >= 20) {
            riskLevel = "MEDIUM";
            recommendedAction = "PRIORITIZE";
        } else {
            riskLevel = "LOW";
            recommendedAction = "STORE";
        }

        if (explanation.length() == 0) {
            explanation.append("Produce is currently within acceptable storage conditions.");
        }

        return new RiskResult(
                riskScore,
                riskLevel,
                recommendedAction,
                explanation.toString()
        );
    }
}