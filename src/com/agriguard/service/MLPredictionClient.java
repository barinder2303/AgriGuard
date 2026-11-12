package com.agriguard.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MLPredictionClient {

    private static final String API_URL =
            "http://127.0.0.1:5000/predict";

    private final HttpClient client;

    public MLPredictionClient() {
        client = HttpClient.newHttpClient();
    }

    public String predict(
            String fruit,
            double temperature,
            double humidity,
            double light,
            double co2) {

        String json =
                "{"
                + "\"Fruit\":\"" + fruit + "\","
                + "\"Temp\":" + temperature + ","
                + "\"Humid (%)\":" + humidity + ","
                + "\"Light (Fux)\":" + light + ","
                + "\"CO2 (pmm)\":" + co2
                + "}";

        try {

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(API_URL))
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(json)
                            )
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            return response.body();

        } catch (Exception e) {

            return "{\"error\":\""
                    + e.getMessage()
                    + "\"}";
        }
    }
}