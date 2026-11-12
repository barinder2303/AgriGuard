package com.agriguard.service;

import com.agriguard.model.Buyer;
import com.agriguard.model.Produce;

public class BuyerMatcher {

    public double calculateMatchScore(Produce produce, Buyer buyer) {

        double score = 0;

        // 1. Crop compatibility
        if (produce.getCropName().equalsIgnoreCase(buyer.getCropRequired())) {
            score += 40;
        } else {
            return 0;
        }

        // 2. Quantity compatibility
        if (produce.getQuantity() >= buyer.getQuantityRequired()) {
            score += 25;
        } else if (produce.getQuantity() >= buyer.getQuantityRequired() * 0.5) {
            score += 15;
        }

        // 3. Quality compatibility
        if (isQualityCompatible(
                produce.getQualityGrade(),
                buyer.getMinimumQuality())) {

            score += 20;
        }

        // 4. Buyer priority
        score += buyer.getPriority();

        // Maximum score = 100
        if (score > 100) {
            score = 100;
        }

        return score;
    }

    private boolean isQualityCompatible(
            String produceQuality,
            String minimumQuality) {

        int produceLevel = getQualityLevel(produceQuality);
        int minimumLevel = getQualityLevel(minimumQuality);

        return produceLevel >= minimumLevel;
    }

    private int getQualityLevel(String quality) {

        if (quality.equalsIgnoreCase("A")) {
            return 3;
        }

        if (quality.equalsIgnoreCase("B")) {
            return 2;
        }

        if (quality.equalsIgnoreCase("C")) {
            return 1;
        }

        return 0;
    }

    public boolean isSuitableBuyer(Produce produce, Buyer buyer) {

        return calculateMatchScore(produce, buyer) >= 60;
    }
}