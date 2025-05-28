# War Management System Documentation

This documentation provides detailed instructions for setting up, configuring, and using the War Management System.

## Table of Contents

1. [System Overview](#system-overview)
2. [Prerequisites](#prerequisites)
3. [Database Setup](#database-setup)
4. [Java Application Setup](#java-application-setup)
5. [Python Visualization Module](#python-visualization-module)
6. [Mission Predictor API](#mission-predictor-api)
7. [System Usage Guide](#system-usage-guide)
8. [Troubleshooting](#troubleshooting)

## System Overview

The War Management System is a comprehensive database and visualization tool designed to manage and analyze military resources, personnel, missions, and equipment. It uses historical World War II data for strategic insights and historical analysis.

The system consists of:

- A JavaFX-based modern GUI application for data management
- MySQL database for data storage
- JavaFX Charts for data visualization
- Optional Python-based data analysis tools
- Machine learning mission prediction API

## Prerequisites

- **Java Development Kit (JDK)**: Version 21 or higher
- **JavaFX**: Version 21 (included with Maven dependencies)
- **MySQL**: Version 8.0 or higher
- **Maven**: For Java dependency management
- **Python** (optional): Version 3.8 or higher for advanced visualization
- **Git** (optional): For version control

You might need [Installation](./installation.md).

## Database Setup

1. **Install MySQL** and start the service.

2. **Create the database**:

   ```sql
   CREATE DATABASE war;
   USE war;
   ```

3. **Import schema**:

   ```bash
   mysql -u USERNAME -p war < Queries.sql
   ```

4. **Database Configuration**:
   Update the database connection properties in `src/com/warManagementGUI/util/DBUtil.java` if necessary:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/war";
   private static final String USER = "root";
   private static final String PASSWORD = "your_password";
   ```

## JavaFX Application Setup

1. **Clone or download the project** to your local machine.

2. **Build the project using Maven**:

   ```bash
   cd warManagement
   mvn clean package
   ```

3. **Run the application using JavaFX Maven Plugin**:

   ```bash
   mvn javafx:run
   ```

   Alternatively, you can run the JavaFX main class directly with the JavaFX modules:

   ```bash
   java --module-path "PATH_TO_JAVAFX_MODULES" --add-modules javafx.controls,javafx.fxml -cp target/classes com.warManagementGUI.WarManagementApp
   ```

   Note: Replace `PATH_TO_JAVAFX_MODULES` with the path to your JavaFX SDK lib directory.

## Python Visualization Module

The Python visualization module provides advanced data analysis capabilities.

1. **Setup Python Environment**:

   ```bash
   cd warManagement
   python -m venv venv
   source venv/bin/activate  # On Windows: venv\Scripts\activate
   ```

2. **Install Dependencies**:

   ```bash
   pip install -r requirements.txt
   ```

3. **Run Visualization Scripts** (examples):
   ```bash
   python visualize_missions.py
   ```

## Mission Predictor API

The Mission Predictor API uses machine learning to predict mission success probabilities.

1. **Setup Environment**:

   ```bash
   cd warManagement/mission_predictor
   python -m venv venv
   source venv/bin/activate  # On Windows: venv\Scripts\activate
   ```

2. **Install Dependencies**:

   ```bash
   pip install -r requirements.txt
   ```

3. **Run the API Server**:

   ```bash
   python mission_predictor_api.py
   ```

4. **API Usage**:
   - Endpoint: `http://localhost:5000/predict`
   - Method: POST
   - Required JSON payload:
     ```json
     {
       "unit_type": 2,
       "personnel_count": 150,
       "equipment_operational_ratio": 0.85,
       "duration_days": 14,
       "location_risk": 3
     }
     ```

## Troubleshooting

### Database Connection Issues

- Verify MySQL service is running
- Check database credentials in `DBUtil.java`
- Ensure database 'war' exists

### JavaFX Application Issues

- Verify JDK version (21 or higher required)
- Check Maven configuration in `pom.xml`
- Verify all JavaFX modules are properly imported
- If you see "Error: JavaFX runtime components are missing", make sure JavaFX modules are correctly specified
- Check that the JavaFX Maven plugin is properly configured

### Python Module Issues

- Check Python version (3.8+ recommended)
- Verify all required packages are installed
- Check virtual environment activation
