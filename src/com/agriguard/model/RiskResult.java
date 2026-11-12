package com.agriguard.model;

public class RiskResult {

    private double riskScore;
    private String riskLevel;
    private String recommendedAction;
    private String explanation;

    public RiskResult(double riskScore,
                      String riskLevel,
                      String recommendedAction,
                      String explanation) {

        this.riskScore = riskScore;
        this.riskLevel = riskLevel;
        this.recommendedAction = recommendedAction;
        this.explanation = explanation;
    }

    public double getRiskScore() {
        return riskScore;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public String getRecommendedAction() {
        return recommendedAction;
    }

    public String getExplanation() {
        return explanation;
    }
}