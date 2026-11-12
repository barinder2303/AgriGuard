package com.agriguard.dao;

import com.agriguard.model.Allocation;
import com.agriguard.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AllocationDAO {

    public boolean addAllocation(Allocation allocation) {

        String sql =
                "INSERT INTO allocations " +
                "(produce_id, buyer_id, allocated_quantity, " +
                "match_score, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    allocation.getProduceId()
            );

            statement.setInt(
                    2,
                    allocation.getBuyerId()
            );

            statement.setDouble(
                    3,
                    allocation.getAllocatedQuantity()
            );

            statement.setDouble(
                    4,
                    allocation.getMatchScore()
            );

            statement.setString(
                    5,
                    allocation.getStatus()
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error adding allocation: "
                    + e.getMessage()
            );

            return false;
        }
    }

    public List<Allocation> getAllAllocations() {

        List<Allocation> allocations =
                new ArrayList<>();

        String sql =
                "SELECT allocation_id, produce_id, buyer_id, " +
                "allocated_quantity, match_score, status " +
                "FROM allocations";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Allocation allocation =
                        new Allocation(
                                resultSet.getInt("allocation_id"),
                                resultSet.getInt("produce_id"),
                                resultSet.getInt("buyer_id"),
                                resultSet.getDouble(
                                        "allocated_quantity"
                                ),
                                resultSet.getDouble(
                                        "match_score"
                                ),
                                resultSet.getString("status")
                        );

                allocations.add(allocation);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error retrieving allocations: "
                    + e.getMessage()
            );
        }

        return allocations;
    }
}