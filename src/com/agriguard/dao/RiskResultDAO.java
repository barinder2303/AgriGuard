package com.agriguard.dao;

import com.agriguard.model.RiskResult;
import com.agriguard.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RiskResultDAO {

    public boolean addRiskResult(
            int produceId,
            RiskResult riskResult) {

        String sql =
                "INSERT INTO risk_results " +
                "(produce_id, risk_score, risk_level, " +
                "recommended_action, explanation) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, produceId);
            statement.setDouble(
                    2,
                    riskResult.getRiskScore()
            );
            statement.setString(
                    3,
                    riskResult.getRiskLevel()
            );
            statement.setString(
                    4,
                    riskResult.getRecommendedAction()
            );
            statement.setString(
                    5,
                    riskResult.getExplanation()
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error adding risk result: "
                    + e.getMessage()
            );

            return false;
        }
    }

    public List<RiskResult> getAllRiskResults() {

        List<RiskResult> riskResults =
                new ArrayList<>();

        String sql =
                "SELECT risk_score, risk_level, " +
                "recommended_action, explanation " +
                "FROM risk_results";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                RiskResult riskResult =
                        new RiskResult(
                                resultSet.getDouble("risk_score"),
                                resultSet.getString("risk_level"),
                                resultSet.getString("recommended_action"),
                                resultSet.getString("explanation")
                        );

                riskResults.add(riskResult);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error retrieving risk results: "
                    + e.getMessage()
            );
        }

        return riskResults;
    }
}