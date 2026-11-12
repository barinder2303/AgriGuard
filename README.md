# 🌾 AgriGuard – AI-Powered Agricultural Produce Loss Prevention & Smart Allocation System

AgriGuard is an intelligent agricultural management system designed to reduce post-harvest losses using Artificial Intelligence, Machine Learning, and smart resource allocation.

The system predicts the spoilage risk of agricultural produce, analyzes storage conditions, matches buyers with available produce, and supports smarter allocation decisions.

## 🚀 Key Features

- **AI-Based Spoilage Prediction:** Uses a Machine Learning model to predict the condition of agricultural produce.
- **Risk Analysis:** Analyzes environmental factors such as temperature, humidity, light, and CO₂ levels.
- **Smart Storage Analysis:** Evaluates storage conditions to help reduce produce spoilage.
- **Buyer Matching:** Matches available agricultural produce with suitable buyers.
- **Smart Allocation:** Helps optimize the allocation of agricultural produce.
- **Dashboard:** Provides a centralized interface for managing the system.
- **Database Management:** Uses MySQL for storing and managing application data.

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java | Core application development |
| Java Swing | Graphical User Interface |
| Python | Machine Learning service |
| Scikit-learn | Spoilage prediction model |
| MySQL | Database management |
| JDBC | Java–MySQL connectivity |
| Git & GitHub | Version control |

## 📂 Project Structure

```text
AgriGuard/
│
├── assets/
│   └── agriguard_banner.png
│
├── lib/
│   └── mysql-connector-j-26.7.0.jar
│
├── ml/
│   ├── agriguard_spoilage_model.joblib
│   └── predict_service.py
│
├── src/
│   └── com/agriguard/
│       ├── dao/
│       ├── gui/
│       ├── model/
│       ├── service/
│       ├── util/
│       └── Main.java
│
└── .gitignore
```

## ⚙️ Requirements

- Java JDK
- Python 3
- MySQL Server
- MySQL Workbench (recommended)
- Visual Studio Code or another Java IDE

## 🔧 Setup and Installation

### 1. Clone the Repository

```bash
git clone https://github.com/barinder2303/AgriGuard.git
```

```bash
cd AgriGuard
```

### 2. Configure Python Environment

Create a virtual environment:

```bash
python -m venv .venv
```

Activate it on Windows:

```bash
.venv\Scripts\activate
```

Install the Python dependencies required by `ml/predict_service.py`.

### 3. Configure MySQL

- Start your MySQL Server.
- Create the database named `agriguard`.
- Configure the required database tables.
- Set your database credentials using environment variables.

On Windows CMD:

```cmd
setx AGRIGUARD_DB_USER "root"
setx AGRIGUARD_DB_PASSWORD "YOUR_MYSQL_PASSWORD"
```

Restart your terminal after setting these variables.

### 4. Start the Machine Learning Service

From the project directory, run:

```bash
python ml/predict_service.py
```

The prediction service runs locally on port `5000`.

### 5. Run the Java Application

Open the project in your Java IDE, configure the MySQL Connector/J library, and run the application entry point:

```text
com.agriguard.Main
```

Make sure the MySQL server and Python prediction service are running before using the application features that depend on them.

## 🧠 Machine Learning

AgriGuard integrates a trained Machine Learning model to estimate agricultural produce spoilage conditions.

The prediction service receives environmental parameters and returns:
- Predicted produce condition
- Prediction probabilities
- Model confidence

## 🎯 Project Objective

The primary objective of AgriGuard is to develop a technology-driven agricultural solution that can help:

- Minimize post-harvest agricultural losses.
- Improve produce storage decisions.
- Support efficient buyer-produce matching.
- Encourage smarter agricultural resource allocation.
- Promote the use of AI and Machine Learning in agriculture.

## 🔮 Future Scope

- IoT-based real-time environmental monitoring.
- Integration with smart storage sensors.
- Cloud-based agricultural data management.
- Mobile application for farmers and buyers.
- Advanced predictive analytics and decision support.

## 👨‍💻 Developer

**Barinder Singh**

B.Tech – Computer Science and Engineering (AI & ML)

GitHub: [@barinder2303](https://github.com/barinder2303)

---
