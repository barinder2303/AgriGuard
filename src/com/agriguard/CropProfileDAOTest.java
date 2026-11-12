package com.agriguard;

import com.agriguard.dao.CropProfileDAO;
import com.agriguard.model.CropProfile;

import java.util.List;

public class CropProfileDAOTest {

    public static void main(String[] args) {

        CropProfileDAO dao = new CropProfileDAO();

        System.out.println("=================================");
        System.out.println("   CROP PROFILE DAO TEST");
        System.out.println("=================================");

        CropProfile tomatoProfile = new CropProfile(
                "Tomato",
                25,
                30,
                70,
                85,
                7
        );

        boolean added =
                dao.addCropProfile(tomatoProfile);

        System.out.println(
                "Crop Profile Added: "
                + (added ? "YES" : "NO")
        );

        List<CropProfile> profiles =
                dao.getAllCropProfiles();

        System.out.println(
                "Total Crop Profiles: "
                + profiles.size()
        );

        for (CropProfile profile : profiles) {

            System.out.println(
                    "Crop: "
                    + profile.getCropName()
            );

            System.out.println(
                    "Temperature: "
                    + profile.getIdealTemperature()
                    + " - "
                    + profile.getMaxTemperature()
            );

            System.out.println(
                    "Humidity: "
                    + profile.getIdealHumidity()
                    + " - "
                    + profile.getMaxHumidity()
            );

            System.out.println(
                    "Max Storage Days: "
                    + profile.getMaxStorageDays()
            );

            System.out.println();
        }

        System.out.println("=================================");
    }
}