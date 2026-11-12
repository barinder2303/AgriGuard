
package com.agriguard.gui;

import com.agriguard.dao.MLPredictionDAO;
import com.agriguard.model.MLPrediction;
import com.agriguard.service.MLPredictionClient;

import javax.swing.*;
import java.awt.*;

public class MLPredictionFrame extends JFrame {

    private JComboBox<String> fruitBox;
    private JTextField temperatureField;
    private JTextField humidityField;
    private JTextField lightField;
    private JTextField co2Field;

    private JLabel predictionLabel;
    private JLabel confidenceLabel;
    private JLabel probabilityLabel;
    private JLabel saveStatusLabel;

    private final MLPredictionClient mlClient =
            new MLPredictionClient();

    private final MLPredictionDAO predictionDAO =
            new MLPredictionDAO();

    public MLPredictionFrame() {

        setTitle("AgriGuard - ML Spoilage Prediction");
        setSize(650, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createUI();
    }

    private void createUI() {

        JPanel mainPanel =
                new JPanel(new BorderLayout(15, 15));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 35, 25, 35)
        );

        JLabel titleLabel =
                new JLabel(
                        "ML SPOILAGE PREDICTION",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel =
                new JPanel(new GridLayout(5, 2, 12, 12));

        inputPanel.add(new JLabel("Fruit:"));

        fruitBox = new JComboBox<>(
                new String[]{
                        "Tomato",
                        "Orange",
                        "Pineapple",
                        "Banana"
                }
        );

        inputPanel.add(fruitBox);

        inputPanel.add(new JLabel("Temperature:"));
        temperatureField = new JTextField("24");
        inputPanel.add(temperatureField);

        inputPanel.add(new JLabel("Humidity (%):"));
        humidityField = new JTextField("90");
        inputPanel.add(humidityField);

        inputPanel.add(new JLabel("Light (Fux):"));
        lightField = new JTextField("20");
        inputPanel.add(lightField);

        inputPanel.add(new JLabel("CO2 (pmm):"));
        co2Field = new JTextField("300");
        inputPanel.add(co2Field);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        JButton predictButton =
                new JButton("PREDICT SPOILAGE");

        predictButton.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        predictionLabel = new JLabel("Prediction: -");
        confidenceLabel = new JLabel("Confidence: -");
        probabilityLabel = new JLabel("Probabilities: -");
        saveStatusLabel = new JLabel("Database: Waiting for prediction");

        JPanel resultPanel =
                new JPanel(new GridLayout(4, 1, 5, 5));

        resultPanel.setBorder(
                BorderFactory.createTitledBorder("ML Result")
        );

        resultPanel.add(predictionLabel);
        resultPanel.add(confidenceLabel);
        resultPanel.add(probabilityLabel);
        resultPanel.add(saveStatusLabel);

        JPanel southPanel =
                new JPanel(new BorderLayout(10, 10));

        southPanel.add(predictButton, BorderLayout.NORTH);
        southPanel.add(resultPanel, BorderLayout.CENTER);

        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);

        predictButton.addActionListener(
                e -> predictSpoilage(predictButton)
        );
    }

    private void predictSpoilage(JButton predictButton) {

        try {

            String fruit =
                    fruitBox.getSelectedItem().toString();

            double temperature =
                    Double.parseDouble(temperatureField.getText().trim());

            double humidity =
                    Double.parseDouble(humidityField.getText().trim());

            double light =
                    Double.parseDouble(lightField.getText().trim());

            double co2 =
                    Double.parseDouble(co2Field.getText().trim());

            if (!Double.isFinite(temperature)
                    || !Double.isFinite(humidity)
                    || !Double.isFinite(light)
                    || !Double.isFinite(co2)
                    || temperature < 0
                    || humidity < 0
                    || humidity > 100
                    || light < 0
                    || co2 < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Enter valid values. Humidity must be between 0 and 100.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            predictButton.setEnabled(false);
            saveStatusLabel.setText("Database: Processing prediction...");

            String result =
                    mlClient.predict(
                            fruit,
                            temperature,
                            humidity,
                            light,
                            co2
                    );

            displayAndSaveResult(
                    result,
                    fruit,
                    temperature,
                    humidity,
                    light,
                    co2
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric values.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );

        } finally {
            predictButton.setEnabled(true);
        }
    }

    private void displayAndSaveResult(
            String result,
            String fruit,
            double temperature,
            double humidity,
            double light,
            double co2) {

        String prediction =
                extractValue(result, "prediction");

        String confidenceText =
                extractValue(result, "confidence");

        if (prediction.equals("N/A")
                || confidenceText.equals("N/A")
                || result.contains("\"error\"")) {

            predictionLabel.setText("Prediction: Failed");
            confidenceLabel.setText("Confidence: -");
            probabilityLabel.setText("Probabilities: -");
            saveStatusLabel.setText("Database: Not saved");

            JOptionPane.showMessageDialog(
                    this,
                    "Could not obtain a valid ML prediction. "
                            + "Check that the Python ML service is running.",
                    "Prediction Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        double confidence;

        try {
            confidence = Double.parseDouble(confidenceText);
        } catch (NumberFormatException e) {
            saveStatusLabel.setText("Database: Not saved");
            predictionLabel.setText("Prediction: Invalid response");
            confidenceLabel.setText("Confidence: -");
            probabilityLabel.setText("Probabilities: -");
            return;
        }

        String bad =
                extractProbability(result, "Bad");

        String good =
                extractProbability(result, "Good");

        predictionLabel.setText("Prediction: " + prediction);
        confidenceLabel.setText(
                String.format("Confidence: %.2f%%", confidence)
        );

        probabilityLabel.setText(
                "Good: " + good + "%    Bad: " + bad + "%"
        );

        MLPrediction mlPrediction =
                new MLPrediction(
                        0,
                        fruit,
                        temperature,
                        humidity,
                        light,
                        co2,
                        prediction,
                        confidence
                );

        boolean saved =
                predictionDAO.savePrediction(mlPrediction);

        if (saved) {
            saveStatusLabel.setText(
                    "Database: Prediction saved successfully"
            );
        } else {
            saveStatusLabel.setText(
                    "Database: Save failed. Check CMD for details."
            );
        }
    }

    private String extractValue(String json, String key) {

        String search = "\"" + key + "\":";
        int start = json.indexOf(search);

        if (start == -1) {
            return "N/A";
        }

        start += search.length();

        int end = json.indexOf(",", start);

        if (end == -1) {
            end = json.indexOf("}", start);
        }

        if (end == -1) {
            return "N/A";
        }

        return json.substring(start, end)
                .trim()
                .replace("\"", "");
    }

    private String extractProbability(
            String json,
            String className) {

        try {

            int probabilitiesStart =
                    json.indexOf("\"probabilities\"");

            if (probabilitiesStart == -1) {
                return "N/A";
            }

            int objectStart =
                    json.indexOf("{", probabilitiesStart);

            int objectEnd =
                    json.indexOf("}", objectStart);

            if (objectStart == -1 || objectEnd == -1) {
                return "N/A";
            }

            String probabilities =
                    json.substring(objectStart, objectEnd + 1);

            String search = "\"" + className + "\":";
            int start = probabilities.indexOf(search);

            if (start == -1) {
                return "N/A";
            }

            start += search.length();

            int end = probabilities.indexOf(",", start);

            if (end == -1) {
                end = probabilities.indexOf("}", start);
            }

            if (end == -1) {
                return "N/A";
            }

            double value = Double.parseDouble(
                    probabilities.substring(start, end).trim()
            );

            return String.format("%.2f", value * 100);

        } catch (Exception e) {
            return "N/A";
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            MLPredictionFrame frame = new MLPredictionFrame();
            frame.setVisible(true);
        });
    }
}
