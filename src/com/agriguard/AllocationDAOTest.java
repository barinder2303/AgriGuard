package com.agriguard;

import com.agriguard.dao.AllocationDAO;
import com.agriguard.model.Allocation;

import java.util.List;

public class AllocationDAOTest {

    public static void main(String[] args) {

        AllocationDAO dao = new AllocationDAO();

        System.out.println("=================================");
        System.out.println("      ALLOCATION DAO TEST");
        System.out.println("=================================");

        Allocation allocation = new Allocation(
                0,
                1,
                1,
                300,
                93,
                "ALLOCATED"
        );

        boolean added =
                dao.addAllocation(allocation);

        System.out.println(
                "Allocation Added: "
                + (added ? "YES" : "NO")
        );

        List<Allocation> allocations =
                dao.getAllAllocations();

        System.out.println(
                "Total Allocations: "
                + allocations.size()
        );

        for (Allocation currentAllocation : allocations) {

            System.out.println(
                    "Allocation ID: "
                    + currentAllocation.getAllocationId()
            );

            System.out.println(
                    "Produce ID: "
                    + currentAllocation.getProduceId()
            );

            System.out.println(
                    "Buyer ID: "
                    + currentAllocation.getBuyerId()
            );

            System.out.println(
                    "Allocated Quantity: "
                    + currentAllocation.getAllocatedQuantity()
                    + " kg"
            );

            System.out.println(
                    "Match Score: "
                    + currentAllocation.getMatchScore()
            );

            System.out.println(
                    "Status: "
                    + currentAllocation.getStatus()
            );

            System.out.println();
        }

        System.out.println("=================================");
    }
}