package com.agriguard.dao;

import com.agriguard.model.CropProfile;
import com.agriguard.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CropProfileDAO {

    public boolean addCropProfile(CropProfile profile) {

        String sql =
                "INSERT INTO crop_profiles " +
                "(crop_name, ideal_temperature, max_temperature, " +
                "ideal_humidity, max_humidity, max_storage_days) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, profile.getCropName());
            statement.setDouble(2, profile.getIdealTemperature());
            statement.setDouble(3, profile.getMaxTemperature());
            statement.setDouble(4, profile.getIdealHumidity());
            statement.setDouble(5, profile.getMaxHumidity());
            statement.setInt(6, profile.getMaxStorageDays());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error adding crop profile: "
                    + e.getMessage()
            );

            return false;
        }
    }

    public List<CropProfile> getAllCropProfiles() {

        List<CropProfile> profiles =
                new ArrayList<>();

        String sql =
                "SELECT crop_name, ideal_temperature, " +
                "max_temperature, ideal_humidity, " +
                "max_humidity, max_storage_days " +
                "FROM crop_profiles";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                CropProfile profile =
                        new CropProfile(
                                resultSet.getString("crop_name"),
                                resultSet.getDouble("ideal_temperature"),
                                resultSet.getDouble("max_temperature"),
                                resultSet.getDouble("ideal_humidity"),
                                resultSet.getDouble("max_humidity"),
                                resultSet.getInt("max_storage_days")
                        );

                profiles.add(profile);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error retrieving crop profiles: "
                    + e.getMessage()
            );
        }

        return profiles;
    }

    public int getCropIdByName(String cropName) {

        String sql =
                "SELECT crop_id FROM crop_profiles " +
                "WHERE crop_name = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, cropName);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return resultSet.getInt(
                            "crop_id"
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error finding crop ID: "
                    + e.getMessage()
            );
        }

        return -1;
    }
}