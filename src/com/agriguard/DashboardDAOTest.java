package com.agriguard;

import com.agriguard.dao.DashboardDAO;

public class DashboardDAOTest {

    public static void main(String[] args) {

        DashboardDAO dao = new DashboardDAO();

        System.out.println("===== DASHBOARD DATABASE TEST =====");

        System.out.println(
                "Total Produce: "
                + dao.getTotalProduce()
                + " kg"
        );

        System.out.println(
                "High Risk: "
                + dao.getHighRiskCount()
        );

        System.out.println(
                "Active Buyers: "
                + dao.getActiveBuyers()
        );

        System.out.println(
                "Storage Utilization: "
                + String.format(
                        "%.2f",
                        dao.getStorageUtilization()
                )
                + "%"
        );

        System.out.println(
                "Latest Risk: "
                + dao.getLatestRiskSummary()
        );

        System.out.println(
                "Recent Activity: "
                + dao.getRecentActivity()
        );

        System.out.println("==================================");
    }
}