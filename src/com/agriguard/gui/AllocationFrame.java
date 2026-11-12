package com.agriguard.gui;

import com.agriguard.dao.AllocationDAO;
import com.agriguard.dao.BuyerDAO;
import com.agriguard.dao.ProduceDAO;
import com.agriguard.model.Allocation;
import com.agriguard.model.Buyer;
import com.agriguard.model.Produce;
import com.agriguard.service.AllocationEngine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AllocationFrame extends JFrame {

    private JComboBox<String> produceBox;

    private DefaultTableModel tableModel;
    private JTable allocationTable;

    private ProduceDAO produceDAO;
    private BuyerDAO buyerDAO;
    private AllocationDAO allocationDAO;
    private AllocationEngine allocationEngine;

    public AllocationFrame() {

        produceDAO = new ProduceDAO();
        buyerDAO = new BuyerDAO();
        allocationDAO = new AllocationDAO();
        allocationEngine = new AllocationEngine();

        setTitle("AgriGuard - Smart Allocation");
        setSize(1150, 700);
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
                new JLabel("SMART ALLOCATION");

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
                        "Automatically match produce with suitable buyers"
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

        // ================= SELECTION =================

        JPanel selectionPanel =
                new JPanel(
                        new GridLayout(1, 3, 10, 10)
                );

        selectionPanel.setBackground(Color.WHITE);

        selectionPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 230, 223)
                        ),
                        BorderFactory.createEmptyBorder(
                                15, 15, 15, 15
                        )
                )
        );

        JLabel produceLabel =
                new JLabel("Select Produce:");

        produceLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        produceBox =
                new JComboBox<>();

        JButton allocateButton =
                new JButton("ALLOCATE PRODUCE");

        allocateButton.setBackground(
                new Color(46, 125, 50)
        );

        allocateButton.setForeground(Color.WHITE);

        allocateButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        allocateButton.setFocusPainted(false);

        selectionPanel.add(produceLabel);
        selectionPanel.add(produceBox);
        selectionPanel.add(allocateButton);

        mainPanel.add(
                selectionPanel,
                BorderLayout.CENTER
        );

        // ================= TABLE =================

        String[] columns = {
                "Allocation ID",
                "Produce ID",
                "Buyer ID",
                "Allocated Quantity",
                "Match Score",
                "Status"
        };

        tableModel =
                new DefaultTableModel(
                        columns,
                        0
                );

        allocationTable =
                new JTable(tableModel);

        allocationTable.setRowHeight(30);

        allocationTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        allocationTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13
                        )
                );

        JScrollPane tableScrollPane =
                new JScrollPane(
                        allocationTable
                );

        tableScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Smart Allocation Results"
                )
        );

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout()
                );

        bottomPanel.setBackground(
                new Color(245, 248, 246)
        );

        bottomPanel.add(
                tableScrollPane,
                BorderLayout.CENTER
        );

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        add(mainPanel);

        // ================= ACTION =================

        allocateButton.addActionListener(
                e -> allocateSelectedProduce()
        );

        loadProduce();
    }

    // ================= LOAD PRODUCE =================

    private void loadProduce() {

        produceBox.removeAllItems();

        List<Produce> produceList =
                produceDAO.getAllProduce();

        for (Produce produce :
                produceList) {

            produceBox.addItem(
                    produce.getProduceId()
                            + " - "
                            + produce.getCropName()
            );
        }

        if (produceList.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No produce records found.\nPlease add produce first.",
                    "No Data",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    // ================= ALLOCATION =================

    private void allocateSelectedProduce() {

        if (produceBox.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a produce record.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        String selected =
                produceBox.getSelectedItem()
                        .toString();

        int produceId =
                Integer.parseInt(
                        selected
                                .split(" - ")[0]
                );

        Produce selectedProduce =
                findProduceById(produceId);

        if (selectedProduce == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Produce record not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        List<Buyer> buyers =
                buyerDAO.getAllBuyers();

        if (buyers.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No buyers found.\nPlease add buyers first.",
                    "No Buyers",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        // Generate smart allocations
        List<Allocation> allocations =
                allocationEngine.allocateProduce(
                        selectedProduce,
                        buyers
                );

        tableModel.setRowCount(0);

        if (allocations.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No suitable buyers found for this produce.",
                    "Allocation Result",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        double totalAllocated = 0;
        int savedCount = 0;

        // ================= SAVE ALLOCATIONS =================

        for (Allocation allocation :
                allocations) {

            // Save allocation to MySQL
            boolean saved =
                    allocationDAO.addAllocation(
                            allocation
                    );

            if (saved) {
                savedCount++;
            }

            // Display allocation in table
            tableModel.addRow(
                    new Object[]{
                            allocation.getAllocationId(),
                            allocation.getProduceId(),
                            allocation.getBuyerId(),
                            allocation.getAllocatedQuantity(),
                            allocation.getMatchScore(),
                            allocation.getStatus()
                    }
            );

            totalAllocated +=
                    allocation.getAllocatedQuantity();
        }

        double remaining =
                selectedProduce.getQuantity()
                        - totalAllocated;

        // ================= RESULT MESSAGE =================

        JOptionPane.showMessageDialog(
                this,
                "Smart allocation completed!\n\n"
                        + "Total Produce: "
                        + selectedProduce.getQuantity()
                        + " kg\n"
                        + "Total Allocated: "
                        + totalAllocated
                        + " kg\n"
                        + "Remaining Produce: "
                        + remaining
                        + " kg\n\n"
                        + "Saved to Database: "
                        + savedCount
                        + " / "
                        + allocations.size()
                        + " allocations",
                "Allocation Complete",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ================= FIND PRODUCE =================

    private Produce findProduceById(
            int produceId) {

        List<Produce> produceList =
                produceDAO.getAllProduce();

        for (Produce produce :
                produceList) {

            if (produce.getProduceId()
                    == produceId) {

                return produce;
            }
        }

        return null;
    }

    // ================= MAIN =================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            AllocationFrame frame =
                    new AllocationFrame();

            frame.setVisible(true);
        });
    }
}