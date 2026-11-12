
package com.agriguard.dao;

import com.agriguard.model.MLPrediction;
import com.agriguard.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MLPredictionDAO {

    public boolean savePrediction(MLPrediction prediction) {

        String sql =
                "INSERT INTO ml_predictions " +
                "(fruit, temperature, humidity, light, co2, " +
                "prediction, confidence) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1, prediction.getFruit()
            );

            statement.setDouble(
                    2, prediction.getTemperature()
            );

            statement.setDouble(
                    3, prediction.getHumidity()
            );

            statement.setDouble(
                    4, prediction.getLight()
            );

            statement.setDouble(
                    5, prediction.getCo2()
            );

            statement.setString(
                    6, prediction.getPrediction()
            );

            statement.setDouble(
                    7, prediction.getConfidence()
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error saving ML prediction: "
                            + e.getMessage()
            );

            return false;
        }
    }
}
