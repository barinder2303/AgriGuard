package com.agriguard;

import com.agriguard.model.Allocation;
import com.agriguard.model.Buyer;
import com.agriguard.model.CropProfile;
import com.agriguard.model.Produce;
import com.agriguard.model.RiskResult;
import com.agriguard.model.Storage;

import com.agriguard.service.AllocationEngine;
import com.agriguard.service.DecisionEngine;
import com.agriguard.service.RiskAnalyzer;
import com.agriguard.service.StorageAnalyzer;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("          AGRIGUARD");
        System.out.println("       INTEGRATED TEST");
        System.out.println("=================================");

        // =========================================
        // 1. PRODUCE
        // =========================================

        Produce tomato = new Produce(
                1001,
                "Tomato",
                500,
                5,
                31,
                82,
                "B"
        );

        // =========================================
        // 2. CROP PROFILE
        // =========================================

        CropProfile tomatoProfile = new CropProfile(
                "Tomato",
                25,
                30,
                70,
                85,
                7
        );

        // =========================================
        // 3. STORAGE
        // =========================================

        Storage storage = new Storage(
                501,
                "Rajpura",
                2000,
                1400,
                25,
                70
        );

        // =========================================
        // 4. RISK ANALYSIS
        // =========================================

        RiskAnalyzer riskAnalyzer =
                new RiskAnalyzer();

        RiskResult riskResult =
                riskAnalyzer.analyzeRisk(
                        tomato,
                        tomatoProfile
                );

        // =========================================
        // 5. DECISION ENGINE
        // =========================================

        DecisionEngine decisionEngine =
                new DecisionEngine();

        String finalDecision =
                decisionEngine.makeDecision(
                        tomato,
                        riskResult,
                        storage
                );

        // =========================================
        // 6. STORAGE ANALYSIS
        // =========================================

        StorageAnalyzer storageAnalyzer =
                new StorageAnalyzer();

        boolean canStore =
                storageAnalyzer.canStore(
                        tomato,
                        storage
                );

        double utilization =
                storageAnalyzer.getUtilizationPercentage(
                        storage
                );

        String storageStatus =
                storageAnalyzer.getStorageStatus(
                        storage
                );

        String storageRecommendation =
                storageAnalyzer.getStorageRecommendation(
                        tomato,
                        storage
                );

        // =========================================
        // 7. BUYERS
        // =========================================

        Buyer buyer1 = new Buyer(
                101,
                "FreshMart",
                "Chandigarh",
                "Tomato",
                300,
                "B",
                8
        );

        Buyer buyer2 = new Buyer(
                102,
                "FoodFactory",
                "Mohali",
                "Tomato",
                700,
                "A",
                6
        );

        Buyer buyer3 = new Buyer(
                103,
                "VeggieHub",
                "Patiala",
                "Potato",
                200,
                "C",
                9
        );

        List<Buyer> buyers =
                new ArrayList<>();

        buyers.add(buyer1);
        buyers.add(buyer2);
        buyers.add(buyer3);

        // =========================================
        // 8. SMART ALLOCATION
        // =========================================

        AllocationEngine allocationEngine =
                new AllocationEngine();

        List<Allocation> allocations =
                allocationEngine.allocateProduce(
                        tomato,
                        buyers
                );

        // =========================================
        // 9. FINAL REPORT
        // =========================================

        System.out.println("\n--- PRODUCE ---");

        System.out.println(
                "Crop: "
                + tomato.getCropName()
        );

        System.out.println(
                "Quantity: "
                + tomato.getQuantity()
                + " kg"
        );

        System.out.println(
                "Quality: "
                + tomato.getQualityGrade()
        );

        // Risk

        System.out.println("\n--- RISK ANALYSIS ---");

        System.out.println(
                "Risk Score: "
                + riskResult.getRiskScore()
        );

        System.out.println(
                "Risk Level: "
                + riskResult.getRiskLevel()
        );

        System.out.println(
                "Explanation: "
                + riskResult.getExplanation()
        );

        // Decision

        System.out.println("\n--- FINAL DECISION ---");

        System.out.println(
                "Recommended Action: "
                + finalDecision
        );

        // Storage

        System.out.println("\n--- STORAGE ANALYSIS ---");

        System.out.println(
                "Total Capacity: "
                + storage.getTotalCapacity()
                + " kg"
        );

        System.out.println(
                "Occupied: "
                + storage.getOccupiedCapacity()
                + " kg"
        );

        System.out.println(
                "Available: "
                + storage.getAvailableCapacity()
                + " kg"
        );

        System.out.println(
                "Utilization: "
                + utilization
                + "%"
        );

        System.out.println(
                "Storage Status: "
                + storageStatus
        );

        System.out.println(
                "Can Store: "
                + (canStore ? "YES" : "NO")
        );

        System.out.println(
                "Storage Recommendation: "
                + storageRecommendation
        );

        // Allocation

        System.out.println("\n--- SMART ALLOCATION ---");

        double totalAllocated = 0;

        for (Allocation allocation : allocations) {

            System.out.println(
                    "Buyer ID: "
                    + allocation.getBuyerId()
            );

            System.out.println(
                    "Allocated Quantity: "
                    + allocation.getAllocatedQuantity()
                    + " kg"
            );

            System.out.println(
                    "Match Score: "
                    + allocation.getMatchScore()
            );

            System.out.println(
                    "Status: "
                    + allocation.getStatus()
            );

            System.out.println();

            totalAllocated +=
                    allocation.getAllocatedQuantity();
        }

        double remainingQuantity =
                tomato.getQuantity()
                - totalAllocated;

        System.out.println(
                "Total Produce: "
                + tomato.getQuantity()
                + " kg"
        );

        System.out.println(
                "Total Allocated: "
                + totalAllocated
                + " kg"
        );

        System.out.println(
                "Remaining Produce: "
                + remainingQuantity
                + " kg"
        );

        // =========================================
        // COMPLETE
        // =========================================

        System.out.println("\n=================================");
        System.out.println("   AGRIGUARD INTEGRATED TEST");
        System.out.println("          COMPLETED");
        System.out.println("=================================");
    }
}