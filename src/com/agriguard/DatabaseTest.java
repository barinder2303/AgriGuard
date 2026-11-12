package com.agriguard;

import com.agriguard.util.DatabaseConnection;

import java.sql.Connection;

public class DatabaseTest {

    public static void main(String[] args) {

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            System.out.println("=================================");
            System.out.println("MYSQL CONNECTION SUCCESSFUL");
            System.out.println("=================================");

            System.out.println(
                    "Connected to database: "
                    + connection.getCatalog()
            );

        } catch (Exception e) {

            System.out.println("MYSQL CONNECTION FAILED");
            e.printStackTrace();
        }
    }
}