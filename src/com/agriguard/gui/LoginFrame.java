package com.agriguard.gui;

import com.agriguard.dao.UserDAO;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {

        setTitle("AgriGuard - Login");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(
                new BorderLayout(10, 10)
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 30, 20, 30
                )
        );

        // Title
        JLabel titleLabel =
                new JLabel(
                        "AGRIGUARD",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        // Subtitle
        JLabel subtitleLabel =
                new JLabel(
                        "Agricultural Intelligence System",
                        SwingConstants.CENTER
                );

        subtitleLabel.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        // Title panel
        JPanel titlePanel =
                new JPanel(
                        new GridLayout(2, 1)
                );

        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        // Form panel
        JPanel formPanel =
                new JPanel(
                        new GridLayout(2, 2, 10, 15)
                );

        JLabel usernameLabel =
                new JLabel("Username:");

        JLabel passwordLabel =
                new JLabel("Password:");

        usernameField =
                new JTextField();

        passwordField =
                new JPasswordField();

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);

        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        // Login button
        JButton loginButton =
                new JButton("LOGIN");

        loginButton.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        loginButton.addActionListener(
                e -> login()
        );

        // Button panel
        JPanel buttonPanel =
                new JPanel();

        buttonPanel.add(loginButton);

        // Add everything
        mainPanel.add(
                titlePanel,
                BorderLayout.NORTH
        );

        mainPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        add(mainPanel);
    }

    private void login() {

        String username =
                usernameField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        if (username.isEmpty() ||
                password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        UserDAO userDAO =
                new UserDAO();

        boolean valid =
                userDAO.validateUser(
                        username,
                        password
                );

        if (valid) {

            JOptionPane.showMessageDialog(
                    this,
                    "Login Successful!"
            );

            dispose();

            DashboardFrame dashboard =
                    new DashboardFrame();

            dashboard.setVisible(true);

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            LoginFrame frame =
                    new LoginFrame();

            frame.setVisible(true);
        });
    }
}