package com.agriguard;

import com.agriguard.dao.RiskResultDAO;
import com.agriguard.model.RiskResult;

import java.util.List;

public class RiskResultDAOTest {

    public static void main(String[] args) {

        RiskResultDAO dao = new RiskResultDAO();

        System.out.println("=================================");
        System.out.println("     RISK RESULT DAO TEST");
        System.out.println("=================================");

        RiskResult riskResult = new RiskResult(
                65.0,
                "HIGH",
                "PROCESS",
                "Temperature is above safe limit. "
                + "Humidity is above ideal range. "
                + "Produce is approaching storage limit. "
                + "Quality grade is moderate."
        );

        boolean added =
                dao.addRiskResult(
                        1,
                        riskResult
                );

        System.out.println(
                "Risk Result Added: "
                + (added ? "YES" : "NO")
        );

        List<RiskResult> riskResults =
                dao.getAllRiskResults();

        System.out.println(
                "Total Risk Results: "
                + riskResults.size()
        );

        for (RiskResult result : riskResults) {

            System.out.println(
                    "Risk Score: "
                    + result.getRiskScore()
            );

            System.out.println(
                    "Risk Level: "
                    + result.getRiskLevel()
            );

            System.out.println(
                    "Recommended Action: "
                    + result.getRecommendedAction()
            );

            System.out.println(
                    "Explanation: "
                    + result.getExplanation()
            );

            System.out.println();
        }

        System.out.println("=================================");
    }
}