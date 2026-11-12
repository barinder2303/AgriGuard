package com.agriguard.dao;

import com.agriguard.model.Produce;
import com.agriguard.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProduceDAO {

    public boolean addProduce(Produce produce, int cropId) {

        String sql =
                "INSERT INTO produce " +
                "(crop_id, quantity, harvest_age, temperature, " +
                "humidity, quality_grade) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, cropId);
            statement.setDouble(2, produce.getQuantity());
            statement.setInt(3, produce.getHarvestAge());
            statement.setDouble(4, produce.getTemperature());
            statement.setDouble(5, produce.getHumidity());
            statement.setString(6, produce.getQualityGrade());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error adding produce: "
                    + e.getMessage()
            );

            return false;
        }
    }

    public List<Produce> getAllProduce() {

        List<Produce> produceList =
                new ArrayList<>();

        String sql =
                "SELECT p.produce_id, c.crop_name, " +
                "p.quantity, p.harvest_age, " +
                "p.temperature, p.humidity, " +
                "p.quality_grade " +
                "FROM produce p " +
                "JOIN crop_profiles c " +
                "ON p.crop_id = c.crop_id";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Produce produce =
                        new Produce(
                                resultSet.getInt("produce_id"),
                                resultSet.getString("crop_name"),
                                resultSet.getDouble("quantity"),
                                resultSet.getInt("harvest_age"),
                                resultSet.getDouble("temperature"),
                                resultSet.getDouble("humidity"),
                                resultSet.getString("quality_grade")
                        );

                produceList.add(produce);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error retrieving produce: "
                    + e.getMessage()
            );
        }

        return produceList;
    }
}