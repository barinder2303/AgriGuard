import json
from http.server import BaseHTTPRequestHandler, HTTPServer
from pathlib import Path

import joblib
import pandas as pd

MODEL_PATH = Path(__file__).resolve().parent / "agriguard_spoilage_model.joblib"
HOST = "127.0.0.1"
PORT = 5000

model = joblib.load(MODEL_PATH)


class PredictionHandler(BaseHTTPRequestHandler):

    def send_json(self, status_code, data):
        response = json.dumps(data).encode("utf-8")

        self.send_response(status_code)
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(response)))
        self.end_headers()
        self.wfile.write(response)

    def do_GET(self):
        if self.path == "/health":
            self.send_json(200, {
                "status": "OK",
                "service": "AgriGuard ML Prediction Service"
            })
        else:
            self.send_json(404, {"error": "Endpoint not found"})

    def do_POST(self):
        if self.path != "/predict":
            self.send_json(404, {"error": "Endpoint not found"})
            return

        try:
            length = int(self.headers.get("Content-Length", 0))
            body = self.rfile.read(length)
            data = json.loads(body.decode("utf-8"))

            required = ["Fruit", "Temp", "Humid (%)", "Light (Fux)", "CO2 (pmm)"]

            for field in required:
                if field not in data:
                    self.send_json(400, {
                        "error": f"Missing field: {field}"
                    })
                    return

            input_data = pd.DataFrame([{
                "Fruit": str(data["Fruit"]),
                "Temp": float(data["Temp"]),
                "Humid (%)": float(data["Humid (%)"]),
                "Light (Fux)": float(data["Light (Fux)"]),
                "CO2 (pmm)": float(data["CO2 (pmm)"])
            }])

            prediction = str(model.predict(input_data)[0])

            result = {
                "prediction": prediction
            }

            if hasattr(model, "predict_proba"):
                probabilities = model.predict_proba(input_data)[0]
                classes = model.classes_

                result["probabilities"] = {
                    str(cls): round(float(prob), 4)
                    for cls, prob in zip(classes, probabilities)
                }

                result["confidence"] = round(
                    float(max(probabilities)) * 100, 2
                )

            self.send_json(200, result)

        except Exception as e:
            self.send_json(500, {
                "error": str(e)
            })


if __name__ == "__main__":
    print("======================================")
    print("AgriGuard ML Prediction Service")
    print("======================================")
    print(f"Model: {MODEL_PATH}")
    print(f"Server: http://{HOST}:{PORT}")
    print("Health: http://127.0.0.1:5000/health")
    print("Press Ctrl+C to stop.")
    print("======================================")

    server = HTTPServer((HOST, PORT), PredictionHandler)
    server.serve_forever()
