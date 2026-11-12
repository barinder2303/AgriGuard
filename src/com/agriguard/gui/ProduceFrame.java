
package com.agriguard.gui;

import com.agriguard.dao.CropProfileDAO;
import com.agriguard.dao.ProduceDAO;
import com.agriguard.model.CropProfile;
import com.agriguard.model.Produce;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProduceFrame extends JFrame {

    private JComboBox<String> cropBox;
    private JTextField quantityField;
    private JTextField ageField;
    private JTextField temperatureField;
    private JTextField humidityField;
    private JComboBox<String> qualityBox;

    private DefaultTableModel tableModel;
    private JTable produceTable;

    private final CropProfileDAO cropProfileDAO;
    private final ProduceDAO produceDAO;

    public ProduceFrame() {
        cropProfileDAO = new CropProfileDAO();
        produceDAO = new ProduceDAO();

        setTitle("AgriGuard - Produce Management");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 248, 246));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        // HEADER
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(20, 55, 35));
        headerPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        );

        JLabel titleLabel = new JLabel("AgriGuard - Produce Management");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel subtitleLabel =
                new JLabel("Add and manage agricultural produce");
        subtitleLabel.setForeground(new Color(220, 240, 225));
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(new Color(20, 55, 35));
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // FORM
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 230, 223)
                        ),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                )
        );

        cropBox = new JComboBox<>();
        loadCropProfiles();

        quantityField = new JTextField();
        ageField = new JTextField();
        temperatureField = new JTextField();
        humidityField = new JTextField();

        qualityBox = new JComboBox<>(new String[]{"A", "B", "C"});

        formPanel.add(createLabel("Crop Name:"));
        formPanel.add(cropBox);

        formPanel.add(createLabel("Quantity (kg):"));
        formPanel.add(quantityField);

        formPanel.add(createLabel("Harvest Age (days):"));
        formPanel.add(ageField);

        formPanel.add(createLabel("Temperature (C):"));
        formPanel.add(temperatureField);

        formPanel.add(createLabel("Humidity (%):"));
        formPanel.add(humidityField);

        formPanel.add(createLabel("Quality Grade:"));
        formPanel.add(qualityBox);

        JButton addButton = new JButton("ADD PRODUCE");
        addButton.setBackground(new Color(46, 125, 50));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 13));
        addButton.setFocusPainted(false);

        JButton clearButton = new JButton("CLEAR");
        clearButton.setFont(new Font("Arial", Font.BOLD, 13));
        clearButton.setFocusPainted(false);

        formPanel.add(addButton);
        formPanel.add(clearButton);

        // TABLE
        String[] columns = {
                "ID", "Crop", "Quantity (kg)", "Age (days)",
                "Temperature", "Humidity", "Quality"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        produceTable = new JTable(tableModel);
        produceTable.setRowHeight(28);
        produceTable.setFont(new Font("Arial", Font.PLAIN, 13));
        produceTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        JScrollPane tableScrollPane = new JScrollPane(produceTable);
        tableScrollPane.setBorder(
                BorderFactory.createLineBorder(new Color(220, 230, 223))
        );

        // CENTER LAYOUT
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(new Color(245, 248, 246));
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);

        // BUTTON ACTIONS
        addButton.addActionListener(e -> addProduce());
        clearButton.addActionListener(e -> clearForm());

        loadProduceTable();
    }

    // LOAD CROPS FROM MYSQL
    private void loadCropProfiles() {
        cropBox.removeAllItems();

        try {
            List<CropProfile> profiles =
                    cropProfileDAO.getAllCropProfiles();

            if (profiles == null || profiles.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No crop profiles found in the database.",
                        "Crop Profiles",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            for (CropProfile profile : profiles) {
                cropBox.addItem(profile.getCropName());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load crop profiles: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ADD PRODUCE TO MYSQL
    private void addProduce() {
        if (cropBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No crop profile available. Add crop profiles first.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String cropName = cropBox.getSelectedItem().toString();
        String quantityText = quantityField.getText().trim();
        String ageText = ageField.getText().trim();
        String temperatureText = temperatureField.getText().trim();
        String humidityText = humidityField.getText().trim();

        if (quantityText.isEmpty()
                || ageText.isEmpty()
                || temperatureText.isEmpty()
                || humidityText.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            double quantity = Double.parseDouble(quantityText);
            int age = Integer.parseInt(ageText);
            double temperature = Double.parseDouble(temperatureText);
            double humidity = Double.parseDouble(humidityText);

            if (!Double.isFinite(quantity)
                    || !Double.isFinite(temperature)
                    || !Double.isFinite(humidity)
                    || quantity <= 0
                    || age < 0
                    || humidity < 0
                    || humidity > 100) {
                JOptionPane.showMessageDialog(
                        this,
                        "Enter a positive quantity, non-negative age, "
                                + "valid temperature, and humidity from 0 to 100.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String quality = qualityBox.getSelectedItem().toString();

            int cropId = cropProfileDAO.getCropIdByName(cropName);

            if (cropId == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Crop ID not found for " + cropName,
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            Produce produce = new Produce(
                    0,
                    cropName,
                    quantity,
                    age,
                    temperature,
                    humidity,
                    quality
            );

            boolean success = produceDAO.addProduce(produce, cropId);

            if (success) {
                JOptionPane.showMessageDialog(
                        this,
                        "Produce added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearForm();
                loadProduceTable();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add produce. Check the database connection.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter valid numeric values in quantity, age, temperature, "
                            + "and humidity.",
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

    // LOAD SAVED PRODUCE INTO TABLE
    private void loadProduceTable() {
        tableModel.setRowCount(0);

        try {
            List<Produce> produceList = produceDAO.getAllProduce();

            if (produceList == null) {
                return;
            }

            for (Produce produce : produceList) {
                tableModel.addRow(new Object[]{
                        produce.getProduceId(),
                        produce.getCropName(),
                        produce.getQuantity(),
                        produce.getHarvestAge(),
                        produce.getTemperature(),
                        produce.getHumidity(),
                        produce.getQualityGrade()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load produce records: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // CLEAR FORM
    private void clearForm() {
        quantityField.setText("");
        ageField.setText("");
        temperatureField.setText("");
        humidityField.setText("");

        if (cropBox.getItemCount() > 0) {
            cropBox.setSelectedIndex(0);
        }

        qualityBox.setSelectedIndex(0);
        quantityField.requestFocusInWindow();
    }

    // LABEL DESIGN
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(new Color(45, 55, 50));
        return label;
    }

    // RUN THIS SCREEN DIRECTLY
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProduceFrame frame = new ProduceFrame();
            frame.setVisible(true);
        });
    }
}
