package com.agriguard.service;

import com.agriguard.model.Allocation;
import com.agriguard.model.Buyer;
import com.agriguard.model.Produce;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AllocationEngine {

    public List<Allocation> allocateProduce(
            Produce produce,
            List<Buyer> buyers) {

        List<Allocation> allocations = new ArrayList<>();

        BuyerMatcher matcher = new BuyerMatcher();

        // Create a copy so the original buyer list is not modified
        List<Buyer> sortedBuyers = new ArrayList<>(buyers);

        // Sort buyers by match score from highest to lowest
        sortedBuyers.sort(
                Comparator.comparingDouble(
                        (Buyer buyer) ->
                                matcher.calculateMatchScore(
                                        produce,
                                        buyer
                                )
                ).reversed()
        );

        double remainingQuantity = produce.getQuantity();
        int allocationId = 1;

        // Allocate to the best matching buyers first
        for (Buyer buyer : sortedBuyers) {

            if (remainingQuantity <= 0) {
                break;
            }

            double matchScore =
                    matcher.calculateMatchScore(
                            produce,
                            buyer
                    );

            // Ignore unsuitable buyers
            if (matchScore < 60) {
                continue;
            }

            double quantityToAllocate =
                    Math.min(
                            remainingQuantity,
                            buyer.getQuantityRequired()
                    );

            if (quantityToAllocate <= 0) {
                continue;
            }

            Allocation allocation = new Allocation(
                    allocationId,
                    produce.getProduceId(),
                    buyer.getBuyerId(),
                    quantityToAllocate,
                    matchScore,
                    "ALLOCATED"
            );

            allocations.add(allocation);

            remainingQuantity -= quantityToAllocate;
            allocationId++;
        }

        return allocations;
    }
}