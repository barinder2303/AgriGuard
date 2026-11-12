package com.agriguard;

import com.agriguard.dao.StorageDAO;
import com.agriguard.model.Storage;

import java.util.List;

public class StorageDAOTest {

    public static void main(String[] args) {

        StorageDAO dao = new StorageDAO();

        System.out.println("=================================");
        System.out.println("       STORAGE DAO TEST");
        System.out.println("=================================");

        Storage storage = new Storage(
                0,
                "Rajpura",
                2000,
                1400,
                25,
                70
        );

        boolean added =
                dao.addStorage(storage);

        System.out.println(
                "Storage Added: "
                + (added ? "YES" : "NO")
        );

        List<Storage> storageList =
                dao.getAllStorage();

        System.out.println(
                "Total Storage Records: "
                + storageList.size()
        );

        for (Storage currentStorage : storageList) {

            System.out.println(
                    "Storage ID: "
                    + currentStorage.getStorageId()
            );

            System.out.println(
                    "Location: "
                    + currentStorage.getLocation()
            );

            System.out.println(
                    "Total Capacity: "
                    + currentStorage.getTotalCapacity()
                    + " kg"
            );

            System.out.println(
                    "Occupied Capacity: "
                    + currentStorage.getOccupiedCapacity()
                    + " kg"
            );

            System.out.println(
                    "Available Capacity: "
                    + currentStorage.getAvailableCapacity()
                    + " kg"
            );

            System.out.println(
                    "Temperature: "
                    + currentStorage.getTemperature()
                    + " °C"
            );

            System.out.println(
                    "Humidity: "
                    + currentStorage.getHumidity()
                    + "%"
            );

            System.out.println();
        }

        System.out.println("=================================");
    }
}