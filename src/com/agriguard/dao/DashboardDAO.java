package com.agriguard.dao;

import com.agriguard.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardDAO {

    // Get total quantity of produce in database
    public double getTotalProduce() {

        String sql =
                "SELECT COALESCE(SUM(quantity), 0) AS total " +
                "FROM produce";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getDouble("total");
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error getting total produce: "
                    + e.getMessage()
            );
        }

        return 0;
    }


    // Get number of high or critical risk results
    public int getHighRiskCount() {

        String sql =
                "SELECT COUNT(*) AS total " +
                "FROM risk_results " +
                "WHERE risk_level IN ('HIGH', 'CRITICAL')";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error getting high risk count: "
                    + e.getMessage()
            );
        }

        return 0;
    }


    // Get number of buyers
    public int getActiveBuyers() {

        String sql =
                "SELECT COUNT(*) AS total " +
                "FROM buyers";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error getting buyer count: "
                    + e.getMessage()
            );
        }

        return 0;
    }


    // Get storage utilization percentage
    public double getStorageUtilization() {

        String sql =
                "SELECT " +
                "COALESCE(SUM(occupied_capacity), 0) AS occupied, " +
                "COALESCE(SUM(total_capacity), 0) AS total " +
                "FROM storage";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            if (resultSet.next()) {

                double occupied =
                        resultSet.getDouble("occupied");

                double total =
                        resultSet.getDouble("total");

                if (total > 0) {
                    return (occupied / total) * 100;
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error getting storage utilization: "
                    + e.getMessage()
            );
        }

        return 0;
    }


    // Get latest risk analysis
    public String getLatestRiskSummary() {

        String sql =
                "SELECT risk_score, risk_level, recommended_action " +
                "FROM risk_results " +
                "ORDER BY analyzed_at DESC " +
                "LIMIT 1";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            if (resultSet.next()) {

                return "Risk Score: "
                        + resultSet.getDouble("risk_score")
                        + " | Level: "
                        + resultSet.getString("risk_level")
                        + " | Action: "
                        + resultSet.getString("recommended_action");
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error getting latest risk: "
                    + e.getMessage()
            );
        }

        return "No risk analysis available.";
    }


    // Get latest activities from database
    public String getRecentActivity() {

        String sql =
                "SELECT activity, activity_time " +
                "FROM (" +

                "SELECT " +
                "'Produce record added' AS activity, " +
                "created_at AS activity_time " +
                "FROM produce " +

                "UNION ALL " +

                "SELECT " +
                "'Risk analysis completed' AS activity, " +
                "analyzed_at AS activity_time " +
                "FROM risk_results " +

                "UNION ALL " +

                "SELECT " +
                "'Buyer allocation created' AS activity, " +
                "created_at AS activity_time " +
                "FROM allocations " +

                "UNION ALL " +

                "SELECT " +
                "'Storage record added' AS activity, " +
                "created_at AS activity_time " +
                "FROM storage " +

                ") AS activities " +
                "ORDER BY activity_time DESC " +
                "LIMIT 4";

        StringBuilder activity =
                new StringBuilder();

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                activity.append("- ")
                        .append(
                                resultSet.getString(
                                        "activity"
                                )
                        )
                        .append("\n");
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error getting recent activity: "
                    + e.getMessage()
            );
        }

        if (activity.length() == 0) {
            return "No recent activity available.";
        }

        return activity.toString().trim();
    }
}