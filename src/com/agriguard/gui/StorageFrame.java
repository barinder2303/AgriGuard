package com.agriguard.gui;

import com.agriguard.dao.StorageDAO;
import com.agriguard.model.Storage;
import com.agriguard.service.StorageAnalyzer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StorageFrame extends JFrame {

    private JTextField locationField;
    private JTextField totalCapacityField;
    private JTextField occupiedCapacityField;
    private JTextField temperatureField;
    private JTextField humidityField;

    private DefaultTableModel tableModel;
    private JTable storageTable;

    private StorageDAO storageDAO;
    private StorageAnalyzer storageAnalyzer;

    public StorageFrame() {

        storageDAO = new StorageDAO();
        storageAnalyzer = new StorageAnalyzer();

        setTitle("AgriGuard - Storage Management");
        setSize(1150, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel =
                new JPanel(new BorderLayout(15, 15));

        mainPanel.setBackground(
                new Color(245, 248, 246)
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 25, 20, 25
                )
        );

        // ================= HEADER =================

        JPanel headerPanel =
                new JPanel(new BorderLayout());

        headerPanel.setBackground(
                new Color(20, 55, 35)
        );

        headerPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 20, 15, 20
                )
        );

        JLabel titleLabel =
                new JLabel("STORAGE MANAGEMENT");

        titleLabel.setForeground(Color.WHITE);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        JLabel subtitleLabel =
                new JLabel(
                        "Manage storage capacity and environmental conditions"
                );

        subtitleLabel.setForeground(
                new Color(220, 240, 225)
        );

        subtitleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        JPanel titlePanel =
                new JPanel(
                        new GridLayout(2, 1)
                );

        titlePanel.setBackground(
                new Color(20, 55, 35)
        );

        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        headerPanel.add(
                titlePanel,
                BorderLayout.WEST
        );

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        // ================= FORM =================

        JPanel formPanel =
                new JPanel(
                        new GridLayout(6, 2, 10, 10)
                );

        formPanel.setBackground(Color.WHITE);

        formPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 230, 223)
                        ),
                        BorderFactory.createEmptyBorder(
                                20, 20, 20, 20
                        )
                )
        );

        locationField = new JTextField();
        totalCapacityField = new JTextField();
        occupiedCapacityField = new JTextField();
        temperatureField = new JTextField();
        humidityField = new JTextField();

        formPanel.add(
                createLabel("Location:")
        );
        formPanel.add(locationField);

        formPanel.add(
                createLabel("Total Capacity (kg):")
        );
        formPanel.add(totalCapacityField);

        formPanel.add(
                createLabel("Occupied Capacity (kg):")
        );
        formPanel.add(occupiedCapacityField);

        formPanel.add(
                createLabel("Temperature (°C):")
        );
        formPanel.add(temperatureField);

        formPanel.add(
                createLabel("Humidity (%):")
        );
        formPanel.add(humidityField);

        JButton addButton =
                new JButton("ADD STORAGE");

        addButton.setBackground(
                new Color(46, 125, 50)
        );

        addButton.setForeground(Color.WHITE);

        addButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        addButton.setFocusPainted(false);

        JButton analyzeButton =
                new JButton("ANALYZE STORAGE");

        analyzeButton.setBackground(
                new Color(33, 100, 70)
        );

        analyzeButton.setForeground(Color.WHITE);

        analyzeButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        analyzeButton.setFocusPainted(false);

        formPanel.add(addButton);
        formPanel.add(analyzeButton);

        // ================= TABLE =================

        String[] columns = {
                "ID",
                "Location",
                "Total Capacity",
                "Occupied",
                "Available",
                "Temperature",
                "Humidity"
        };

        tableModel =
                new DefaultTableModel(
                        columns,
                        0
                );

        storageTable =
                new JTable(tableModel);

        storageTable.setRowHeight(28);

        storageTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13
                        )
                );

        storageTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        JScrollPane tableScrollPane =
                new JScrollPane(
                        storageTable
                );

        tableScrollPane.setBorder(
                BorderFactory.createLineBorder(
                        new Color(220, 230, 223)
                )
        );

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout(
                                15,
                                15
                        )
                );

        centerPanel.setBackground(
                new Color(245, 248, 246)
        );

        centerPanel.add(
                formPanel,
                BorderLayout.NORTH
        );

        centerPanel.add(
                tableScrollPane,
                BorderLayout.CENTER
        );

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        add(mainPanel);

        // ================= BUTTON ACTIONS =================

        addButton.addActionListener(
                e -> addStorage()
        );

        analyzeButton.addActionListener(
                e -> analyzeSelectedStorage()
        );

        loadStorageTable();
    }

    // ================= ADD STORAGE =================

    private void addStorage() {

        String location =
                locationField.getText().trim();

        String totalText =
                totalCapacityField.getText().trim();

        String occupiedText =
                occupiedCapacityField.getText().trim();

        String temperatureText =
                temperatureField.getText().trim();

        String humidityText =
                humidityField.getText().trim();

        if (location.isEmpty()
                || totalText.isEmpty()
                || occupiedText.isEmpty()
                || temperatureText.isEmpty()
                || humidityText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        try {

            double totalCapacity =
                    Double.parseDouble(totalText);

            double occupiedCapacity =
                    Double.parseDouble(occupiedText);

            double temperature =
                    Double.parseDouble(temperatureText);

            double humidity =
                    Double.parseDouble(humidityText);

            if (totalCapacity <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Total capacity must be greater than 0.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            if (occupiedCapacity < 0
                    || occupiedCapacity > totalCapacity) {

                JOptionPane.showMessageDialog(
                        this,
                        "Occupied capacity must be between 0 and total capacity.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            if (humidity < 0 || humidity > 100) {

                JOptionPane.showMessageDialog(
                        this,
                        "Humidity must be between 0 and 100.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            Storage storage =
                    new Storage(
                            0,
                            location,
                            totalCapacity,
                            occupiedCapacity,
                            temperature,
                            humidity
                    );

            boolean success =
                    storageDAO.addStorage(storage);

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Storage added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearForm();
                loadStorageTable();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add storage.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric values.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ================= ANALYZE STORAGE =================

    private void analyzeSelectedStorage() {

        int selectedRow =
                storageTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a storage record from the table first.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int storageId =
                Integer.parseInt(
                        tableModel.getValueAt(
                                selectedRow,
                                0
                        ).toString()
                );

        String location =
                tableModel.getValueAt(
                        selectedRow,
                        1
                ).toString();

        double totalCapacity =
                Double.parseDouble(
                        tableModel.getValueAt(
                                selectedRow,
                                2
                        ).toString()
                );

        double occupiedCapacity =
                Double.parseDouble(
                        tableModel.getValueAt(
                                selectedRow,
                                3
                        ).toString()
                );

        double temperature =
                Double.parseDouble(
                        tableModel.getValueAt(
                                selectedRow,
                                5
                        ).toString()
                );

        double humidity =
                Double.parseDouble(
                        tableModel.getValueAt(
                                selectedRow,
                                6
                        ).toString()
                );

        Storage storage =
                new Storage(
                        storageId,
                        location,
                        totalCapacity,
                        occupiedCapacity,
                        temperature,
                        humidity
                );

        double utilization =
                storageAnalyzer.getUtilizationPercentage(
                        storage
                );

        double availableCapacity =
                storageAnalyzer.getAvailableCapacity(
                        storage
                );

        String status =
                storageAnalyzer.getStorageStatus(
                        storage
                );

        String recommendation =
                getStorageRecommendation(
                        storage,
                        status
                );

        String message =
                "STORAGE ANALYSIS\n\n"
                + "Storage ID: " + storageId + "\n"
                + "Location: " + location + "\n"
                + "Total Capacity: "
                + totalCapacity + " kg\n"
                + "Occupied Capacity: "
                + occupiedCapacity + " kg\n"
                + "Available Capacity: "
                + availableCapacity + " kg\n"
                + "Temperature: "
                + temperature + " °C\n"
                + "Humidity: "
                + humidity + " %\n\n"
                + "Utilization: "
                + String.format("%.2f", utilization)
                + " %\n"
                + "Storage Status: "
                + status + "\n\n"
                + "Recommendation:\n"
                + recommendation;

        JOptionPane.showMessageDialog(
                this,
                message,
                "Storage Analysis Result",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ================= STORAGE RECOMMENDATION =================

    private String getStorageRecommendation(
            Storage storage,
            String status) {

        double available =
                storage.getAvailableCapacity();

        if (available <= 0) {
            return "Storage is full. Consider distribution or processing.";
        }

        if (status.equals("CRITICAL")) {
            return "Storage capacity is critically high. Avoid unnecessary storage.";
        }

        if (status.equals("HIGH")) {
            return "Storage capacity is high. Monitor capacity closely.";
        }

        if (status.equals("MEDIUM")) {
            return "Storage has moderate utilization. Capacity is currently available.";
        }

        return "Storage has sufficient available capacity.";
    }

    // ================= LOAD TABLE =================

    private void loadStorageTable() {

        tableModel.setRowCount(0);

        List<Storage> storageList =
                storageDAO.getAllStorage();

        for (Storage storage :
                storageList) {

            tableModel.addRow(
                    new Object[]{
                            storage.getStorageId(),
                            storage.getLocation(),
                            storage.getTotalCapacity(),
                            storage.getOccupiedCapacity(),
                            storage.getAvailableCapacity(),
                            storage.getTemperature(),
                            storage.getHumidity()
                    }
            );
        }
    }

    // ================= CLEAR FORM =================

    private void clearForm() {

        locationField.setText("");
        totalCapacityField.setText("");
        occupiedCapacityField.setText("");
        temperatureField.setText("");
        humidityField.setText("");
    }

    // ================= LABEL =================

    private JLabel createLabel(String text) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        label.setForeground(
                new Color(
                        45,
                        55,
                        50
                )
        );

        return label;
    }

    // ================= MAIN =================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            StorageFrame frame =
                    new StorageFrame();

            frame.setVisible(true);
        });
    }
}