package com.agriguard.gui;

import com.agriguard.dao.BuyerDAO;
import com.agriguard.model.Buyer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BuyerFrame extends JFrame {

    private JTextField nameField;
    private JTextField locationField;
    private JTextField cropField;
    private JTextField quantityField;
    private JComboBox<String> qualityBox;
    private JSpinner prioritySpinner;

    private DefaultTableModel tableModel;
    private JTable buyerTable;

    private BuyerDAO buyerDAO;

    public BuyerFrame() {

        buyerDAO = new BuyerDAO();

        setTitle("AgriGuard - Buyer Management");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));

        mainPanel.setBackground(
                new Color(245, 248, 246)
        );

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 25, 20, 25
                )
        );

        // HEADER
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
                new JLabel("🏪  Buyer Management");

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
                        "Add and manage agricultural produce buyers"
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

        // FORM
        JPanel formPanel =
                new JPanel(
                        new GridLayout(7, 2, 10, 10)
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

        nameField = new JTextField();
        locationField = new JTextField();
        cropField = new JTextField();
        quantityField = new JTextField();

        qualityBox =
                new JComboBox<>(
                        new String[]{
                                "A",
                                "B",
                                "C"
                        }
                );

        prioritySpinner =
                new JSpinner(
                        new SpinnerNumberModel(
                                1,
                                1,
                                10,
                                1
                        )
                );

        formPanel.add(
                createLabel("Buyer Name:")
        );
        formPanel.add(nameField);

        formPanel.add(
                createLabel("Location:")
        );
        formPanel.add(locationField);

        formPanel.add(
                createLabel("Crop Required:")
        );
        formPanel.add(cropField);

        formPanel.add(
                createLabel("Quantity Required (kg):")
        );
        formPanel.add(quantityField);

        formPanel.add(
                createLabel("Minimum Quality:")
        );
        formPanel.add(qualityBox);

        formPanel.add(
                createLabel("Priority (1-10):")
        );
        formPanel.add(prioritySpinner);

        JButton addButton =
                new JButton("ADD BUYER");

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

        JButton clearButton =
                new JButton("CLEAR");

        clearButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        clearButton.setFocusPainted(false);

        formPanel.add(addButton);
        formPanel.add(clearButton);

        // TABLE
        String[] columns = {
                "ID",
                "Buyer Name",
                "Location",
                "Crop Required",
                "Quantity",
                "Min Quality",
                "Priority"
        };

        tableModel =
                new DefaultTableModel(
                        columns,
                        0
                );

        buyerTable =
                new JTable(tableModel);

        buyerTable.setRowHeight(28);

        buyerTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13
                        )
                );

        buyerTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        JScrollPane tableScrollPane =
                new JScrollPane(
                        buyerTable
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

        // BUTTON ACTIONS
        clearButton.addActionListener(
                e -> clearForm()
        );

        addButton.addActionListener(
                e -> addBuyer()
        );

        loadBuyerTable();
    }

    private void addBuyer() {

        String name =
                nameField.getText().trim();

        String location =
                locationField.getText().trim();

        String crop =
                cropField.getText().trim();

        String quantityText =
                quantityField.getText().trim();

        String quality =
                qualityBox.getSelectedItem()
                        .toString();

        int priority =
                (Integer) prioritySpinner.getValue();

        if (name.isEmpty()
                || location.isEmpty()
                || crop.isEmpty()
                || quantityText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        try {

            double quantity =
                    Double.parseDouble(
                            quantityText
                    );

            if (quantity <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Quantity must be greater than 0.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            Buyer buyer =
                    new Buyer(
                            0,
                            name,
                            location,
                            crop,
                            quantity,
                            quality,
                            priority
                    );

            boolean success =
                    buyerDAO.addBuyer(buyer);

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Buyer added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearForm();
                loadBuyerTable();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add buyer.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid quantity.",
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

    private void loadBuyerTable() {

        tableModel.setRowCount(0);

        List<Buyer> buyers =
                buyerDAO.getAllBuyers();

        for (Buyer buyer : buyers) {

            tableModel.addRow(
                    new Object[]{
                            buyer.getBuyerId(),
                            buyer.getBuyerName(),
                            buyer.getLocation(),
                            buyer.getCropRequired(),
                            buyer.getQuantityRequired(),
                            buyer.getMinimumQuality(),
                            buyer.getPriority()
                    }
            );
        }
    }

    private void clearForm() {

        nameField.setText("");
        locationField.setText("");
        cropField.setText("");
        quantityField.setText("");

        qualityBox.setSelectedIndex(0);
        prioritySpinner.setValue(1);
    }

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

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            BuyerFrame frame =
                    new BuyerFrame();

            frame.setVisible(true);
        });
    }
}