package com.agriguard;

import com.agriguard.dao.UserDAO;

public class UserDAOTest {

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();

        System.out.println("=================================");
        System.out.println("       USER DAO TEST");
        System.out.println("=================================");

        boolean added = userDAO.addUser(
                "admin",
                "admin123",
                "AgriGuard Administrator",
                "ADMIN"
        );

        System.out.println(
                "User Added: " + (added ? "YES" : "NO")
        );

        boolean valid = userDAO.validateUser(
                "admin",
                "admin123"
        );

        System.out.println(
                "Login Validation: "
                + (valid ? "SUCCESS" : "FAILED")
        );

        System.out.println("=================================");
    }
}