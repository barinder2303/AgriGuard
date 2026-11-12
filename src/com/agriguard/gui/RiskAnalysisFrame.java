package com.agriguard.gui;

import com.agriguard.dao.CropProfileDAO;
import com.agriguard.dao.ProduceDAO;
import com.agriguard.dao.RiskResultDAO;
import com.agriguard.model.CropProfile;
import com.agriguard.model.Produce;
import com.agriguard.model.RiskResult;
import com.agriguard.service.RiskAnalyzer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RiskAnalysisFrame extends JFrame {

    private JComboBox<String> produceBox;

    private JLabel riskScoreLabel;
    private JLabel riskLevelLabel;
    private JLabel actionLabel;
    private JTextArea explanationArea;

    private DefaultTableModel tableModel;
    private JTable riskTable;

    private ProduceDAO produceDAO;
    private CropProfileDAO cropProfileDAO;
    private RiskResultDAO riskResultDAO;
    private RiskAnalyzer riskAnalyzer;

    public RiskAnalysisFrame() {

        produceDAO = new ProduceDAO();
        cropProfileDAO = new CropProfileDAO();
        riskResultDAO = new RiskResultDAO();
        riskAnalyzer = new RiskAnalyzer();

        setTitle("AgriGuard - Risk Analysis");
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
                new JLabel("RISK ANALYSIS");

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
                        "Analyze produce spoilage risk and recommended action"
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

        // ================= TOP PANEL =================

        JPanel topPanel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        topPanel.setBackground(Color.WHITE);

        topPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 230, 223)
                        ),
                        BorderFactory.createEmptyBorder(
                                15, 15, 15, 15
                        )
                )
        );

        JLabel selectLabel =
                new JLabel("Select Produce:");

        selectLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        produceBox =
                new JComboBox<>();

        JButton analyzeButton =
                new JButton("ANALYZE RISK");

        analyzeButton.setBackground(
                new Color(46, 125, 50)
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

        JPanel selectionPanel =
                new JPanel(
                        new GridLayout(1, 3, 10, 10)
                );

        selectionPanel.setBackground(Color.WHITE);

        selectionPanel.add(selectLabel);
        selectionPanel.add(produceBox);
        selectionPanel.add(analyzeButton);

        topPanel.add(
                selectionPanel,
                BorderLayout.CENTER
        );

        // ================= RESULT PANEL =================

        JPanel resultPanel =
                new JPanel(
                        new GridLayout(1, 3, 10, 10)
                );

        resultPanel.setBackground(
                new Color(245, 248, 246)
        );

        riskScoreLabel =
                createResultLabel(
                        "Risk Score: --"
                );

        riskLevelLabel =
                createResultLabel(
                        "Risk Level: --"
                );

        actionLabel =
                createResultLabel(
                        "Action: --"
                );

        resultPanel.add(
                createResultCard(
                        "RISK SCORE",
                        riskScoreLabel
                )
        );

        resultPanel.add(
                createResultCard(
                        "RISK LEVEL",
                        riskLevelLabel
                )
        );

        resultPanel.add(
                createResultCard(
                        "RECOMMENDED ACTION",
                        actionLabel
                )
        );

        // ================= EXPLANATION =================

        explanationArea =
                new JTextArea();

        explanationArea.setEditable(false);

        explanationArea.setLineWrap(true);

        explanationArea.setWrapStyleWord(true);

        explanationArea.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        explanationArea.setText(
                "Select a produce record and click ANALYZE RISK."
        );

        explanationArea.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10
                )
        );

        JScrollPane explanationScrollPane =
                new JScrollPane(
                        explanationArea
                );

        explanationScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Risk Explanation"
                )
        );

        JPanel analysisPanel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        analysisPanel.setBackground(
                new Color(245, 248, 246)
        );

        analysisPanel.add(
                resultPanel,
                BorderLayout.NORTH
        );

        analysisPanel.add(
                explanationScrollPane,
                BorderLayout.CENTER
        );

        // ================= TABLE =================

        String[] columns = {
                "Produce ID",
                "Crop",
                "Risk Score",
                "Risk Level",
                "Recommended Action",
                "Explanation"
        };

        tableModel =
                new DefaultTableModel(
                        columns,
                        0
                );

        riskTable =
                new JTable(tableModel);

        riskTable.setRowHeight(30);

        riskTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        12
                )
        );

        riskTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                12
                        )
                );

        JScrollPane tableScrollPane =
                new JScrollPane(
                        riskTable
                );

        tableScrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Risk Analysis Results"
                )
        );

        // ================= CENTER =================

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
                topPanel,
                BorderLayout.NORTH
        );

        centerPanel.add(
                analysisPanel,
                BorderLayout.CENTER
        );

        centerPanel.add(
                tableScrollPane,
                BorderLayout.SOUTH
        );

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER
        );

        add(mainPanel);

        // ================= ACTION =================

        analyzeButton.addActionListener(
                e -> analyzeSelectedProduce()
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

    // ================= ANALYZE =================

    private void analyzeSelectedProduce() {

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

        Produce selectedProduce = null;

        List<Produce> produceList =
                produceDAO.getAllProduce();

        for (Produce produce :
                produceList) {

            if (produce.getProduceId()
                    == produceId) {

                selectedProduce = produce;
                break;
            }
        }

        if (selectedProduce == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Produce record not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        CropProfile profile =
                findCropProfile(
                        selectedProduce.getCropName()
                );

        if (profile == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Crop profile not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // ================= RISK ANALYSIS =================

        RiskResult result =
                riskAnalyzer.analyzeRisk(
                        selectedProduce,
                        profile
                );

        // ================= SAVE TO DATABASE =================

        boolean saved =
                riskResultDAO.addRiskResult(
                        selectedProduce.getProduceId(),
                        result
                );

        // ================= DISPLAY RESULT =================

        displayResult(
                selectedProduce,
                result
        );

        if (saved) {

            JOptionPane.showMessageDialog(
                    this,
                    "Risk analysis completed successfully.\n\n"
                            + "Risk Score: "
                            + result.getRiskScore()
                            + "\n"
                            + "Risk Level: "
                            + result.getRiskLevel()
                            + "\n"
                            + "Recommended Action: "
                            + result.getRecommendedAction()
                            + "\n\n"
                            + "Result saved to database.",
                    "Risk Analysis Complete",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Risk analysis completed, but the result "
                            + "could not be saved to the database.",
                    "Database Warning",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // ================= FIND PROFILE =================

    private CropProfile findCropProfile(
            String cropName) {

        List<CropProfile> profiles =
                cropProfileDAO
                        .getAllCropProfiles();

        for (CropProfile profile :
                profiles) {

            if (profile.getCropName()
                    .equalsIgnoreCase(cropName)) {

                return profile;
            }
        }

        return null;
    }

    // ================= DISPLAY RESULT =================

    private void displayResult(
            Produce produce,
            RiskResult result) {

        riskScoreLabel.setText(
                "Risk Score: "
                        + result.getRiskScore()
        );

        riskLevelLabel.setText(
                "Risk Level: "
                        + result.getRiskLevel()
        );

        actionLabel.setText(
                "Action: "
                        + result.getRecommendedAction()
        );

        explanationArea.setText(
                result.getExplanation()
        );

        tableModel.addRow(
                new Object[]{
                        produce.getProduceId(),
                        produce.getCropName(),
                        result.getRiskScore(),
                        result.getRiskLevel(),
                        result.getRecommendedAction(),
                        result.getExplanation()
                }
        );
    }

    // ================= RESULT CARD =================

    private JPanel createResultCard(
            String title,
            JLabel valueLabel) {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 230, 223)
                        ),
                        BorderFactory.createEmptyBorder(
                                15, 15, 15, 15
                        )
                )
        );

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        titleLabel.setForeground(
                new Color(
                        100,
                        110,
                        105
                )
        );

        panel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        panel.add(
                valueLabel,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JLabel createResultLabel(
            String text) {

        JLabel label =
                new JLabel(
                        text
                );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        label.setForeground(
                new Color(
                        20,
                        55,
                        35
                )
        );

        return label;
    }

    // ================= MAIN =================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            RiskAnalysisFrame frame =
                    new RiskAnalysisFrame();

            frame.setVisible(true);
        });
    }
}