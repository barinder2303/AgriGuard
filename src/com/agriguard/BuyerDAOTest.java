
    

package com.agriguard;

import com.agriguard.dao.BuyerDAO;
import com.agriguard.model.Buyer;

import java.util.List;

public class BuyerDAOTest {

    public static void main(String[] args) {

        BuyerDAO dao = new BuyerDAO();

        System.out.println("=================================");
        System.out.println("        BUYER DAO TEST");
        System.out.println("=================================");

        Buyer buyer = new Buyer(
                0,
                "FreshMart",
                "Chandigarh",
                "Tomato",
                300,
                "B",
                8
        );

        boolean added =
                dao.addBuyer(buyer);

        System.out.println(
                "Buyer Added: "
                + (added ? "YES" : "NO")
        );

        List<Buyer> buyers =
                dao.getAllBuyers();

        System.out.println(
                "Total Buyers: "
                + buyers.size()
        );

        for (Buyer currentBuyer : buyers) {

            System.out.println(
                    "Buyer ID: "
                    + currentBuyer.getBuyerId()
            );

            System.out.println(
                    "Name: "
                    + currentBuyer.getBuyerName()
            );

            System.out.println(
                    "Location: "
                    + currentBuyer.getLocation()
            );

            System.out.println(
                    "Crop Required: "
                    + currentBuyer.getCropRequired()
            );

            System.out.println(
                    "Quantity Required: "
                    + currentBuyer.getQuantityRequired()
                    + " kg"
            );

            System.out.println(
                    "Minimum Quality: "
                    + currentBuyer.getMinimumQuality()
            );

            System.out.println(
                    "Priority: "
                    + currentBuyer.getPriority()
            );

            System.out.println();
        }

        System.out.println("=================================");
    }
}