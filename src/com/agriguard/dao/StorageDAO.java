package com.agriguard.dao;

import com.agriguard.model.Storage;
import com.agriguard.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StorageDAO {

    public boolean addStorage(Storage storage) {

        String sql =
                "INSERT INTO storage " +
                "(location, total_capacity, occupied_capacity, " +
                "temperature, humidity) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, storage.getLocation());
            statement.setDouble(2, storage.getTotalCapacity());
            statement.setDouble(3, storage.getOccupiedCapacity());
            statement.setDouble(4, storage.getTemperature());
            statement.setDouble(5, storage.getHumidity());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error adding storage: "
                    + e.getMessage()
            );

            return false;
        }
    }

    public List<Storage> getAllStorage() {

        List<Storage> storageList =
                new ArrayList<>();

        String sql =
                "SELECT storage_id, location, total_capacity, " +
                "occupied_capacity, temperature, humidity " +
                "FROM storage";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Storage storage =
                        new Storage(
                                resultSet.getInt("storage_id"),
                                resultSet.getString("location"),
                                resultSet.getDouble("total_capacity"),
                                resultSet.getDouble("occupied_capacity"),
                                resultSet.getDouble("temperature"),
                                resultSet.getDouble("humidity")
                        );

                storageList.add(storage);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error retrieving storage: "
                    + e.getMessage()
            );
        }

        return storageList;
    }
}