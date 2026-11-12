package com.agriguard.dao;

import com.agriguard.model.Buyer;
import com.agriguard.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BuyerDAO {

    public boolean addBuyer(Buyer buyer) {

        String sql =
                "INSERT INTO buyers " +
                "(buyer_name, location, crop_required, " +
                "quantity_required, minimum_quality, priority) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, buyer.getBuyerName());
            statement.setString(2, buyer.getLocation());
            statement.setString(3, buyer.getCropRequired());
            statement.setDouble(4, buyer.getQuantityRequired());
            statement.setString(5, buyer.getMinimumQuality());
            statement.setInt(6, buyer.getPriority());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error adding buyer: "
                    + e.getMessage()
            );

            return false;
        }
    }

    public List<Buyer> getAllBuyers() {

        List<Buyer> buyers =
                new ArrayList<>();

        String sql =
                "SELECT buyer_id, buyer_name, location, " +
                "crop_required, quantity_required, " +
                "minimum_quality, priority " +
                "FROM buyers";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Buyer buyer =
                        new Buyer(
                                resultSet.getInt("buyer_id"),
                                resultSet.getString("buyer_name"),
                                resultSet.getString("location"),
                                resultSet.getString("crop_required"),
                                resultSet.getDouble("quantity_required"),
                                resultSet.getString("minimum_quality"),
                                resultSet.getInt("priority")
                        );

                buyers.add(buyer);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error retrieving buyers: "
                    + e.getMessage()
            );
        }

        return buyers;
    }
}