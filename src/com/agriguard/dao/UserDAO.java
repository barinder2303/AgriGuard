package com.agriguard.dao;

import com.agriguard.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public boolean addUser(
            String username,
            String password,
            String fullName,
            String role) {

        String sql =
                "INSERT INTO users " +
                "(username, password, full_name, role) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, fullName);
            statement.setString(4, role);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error adding user: "
                    + e.getMessage()
            );

            return false;
        }
    }

    public boolean validateUser(
            String username,
            String password) {

        String sql =
                "SELECT * FROM users " +
                "WHERE username = ? AND password = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                return resultSet.next();
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error validating user: "
                    + e.getMessage()
            );

            return false;
        }
    }
}