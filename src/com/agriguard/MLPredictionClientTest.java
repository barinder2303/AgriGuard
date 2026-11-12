package com.agriguard;

import com.agriguard.service.MLPredictionClient;

public class MLPredictionClientTest {

    public static void main(String[] args) {

        MLPredictionClient client =
                new MLPredictionClient();

        String result =
                client.predict(
                        "Tomato",
                        24,
                        90,
                        20,
                        300
                );

        System.out.println("===== ML PREDICTION TEST =====");
        System.out.println(result);
        System.out.println("==============================");
    }
}