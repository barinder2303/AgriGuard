package com.agriguard;

import com.agriguard.dao.ProduceDAO;
import com.agriguard.model.Produce;

import java.util.List;

public class ProduceDAOTest {

    public static void main(String[] args) {

        ProduceDAO dao = new ProduceDAO();

        System.out.println("=================================");
        System.out.println("       PRODUCE DAO TEST");
        System.out.println("=================================");

        Produce tomato = new Produce(
                0,
                "Tomato",
                500,
                5,
                31,
                82,
                "B"
        );

        boolean added =
                dao.addProduce(tomato, 1);

        System.out.println(
                "Produce Added: "
                + (added ? "YES" : "NO")
        );

        List<Produce> produceList =
                dao.getAllProduce();

        System.out.println(
                "Total Produce Records: "
                + produceList.size()
        );

        for (Produce produce : produceList) {

            System.out.println(
                    "Produce ID: "
                    + produce.getProduceId()
            );

            System.out.println(
                    "Crop: "
                    + produce.getCropName()
            );

            System.out.println(
                    "Quantity: "
                    + produce.getQuantity()
                    + " kg"
            );

            System.out.println(
                    "Harvest Age: "
                    + produce.getHarvestAge()
                    + " days"
            );

            System.out.println(
                    "Temperature: "
                    + produce.getTemperature()
                    + " °C"
            );

            System.out.println(
                    "Humidity: "
                    + produce.getHumidity()
                    + "%"
            );

            System.out.println(
                    "Quality: "
                    + produce.getQualityGrade()
            );

            System.out.println();
        }

        System.out.println("=================================");
    }
}