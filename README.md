**War Management System**

A comprehensive database and visualization tool designed to manage and analyze military resources, personnel, missions, and equipment, using historical World War II data for strategic insights and historical analysis.

---

## Table of Contents

1. [Overview](#overview)
2. [Features](#features)
3. [Database Schema](#database-schema)
4. [Setup & Installation](#setup--installation)

   * [1. MySQL Database](#1-mysql-database)
   * [2. Java Application](#2-java-application)
   * [3. Python Visualization (Optional)](#3-python-visualization-optional)
5. [Usage](#usage)
6. [Contributing](#contributing)
7. [License](#license)

---

## Overview

The War Management System allows you to:

* Track military **personnel**, **units**, **missions**, **equipment**, and **supplies**.
* Visualize data through charts for better analysis of logistics and mission outcomes.

---

## Features

* **Personnel Management**: View and update status (active, injured, MIA, KIA).
* **Unit Management**: Organize by type (infantry, cavalry, artillery) and commander.
* **Mission Management**: Log mission objectives, timelines, and statuses.
* **Equipment Tracking**: Monitor operational status of weapons, vehicles, electronics.
* **Supply Chain**: Manage inventory levels and deployments across locations.
* **Data Visualization**: Generate bar charts and other plots for quick insights.

---

## Database Schema

### Personnel

* `personnel_id` INT (PK)
* `first_name`, `last_name` VARCHAR(50)
* `post`, `role` VARCHAR(50)
* `unit_id` INT
* `status` ENUM('active', 'injured', 'MIA', 'KIA')
* `contact_information` VARCHAR(100)

### Locations

* `location_id` INT (PK)
* `name`, `coordinates` VARCHAR(100)

### Units

* `unit_id` INT (PK)
* `unit_name` VARCHAR(50)
* `unit_type` ENUM('infantry','cavalry','artillery')
* `commander_id`, `location_id` INT

### Missions

* `mission_id` INT (PK)
* `name` VARCHAR(100)
* `objective` TEXT
* `start_date`, `end_date` VARCHAR(20)
* `status` ENUM('planned','ongoing','completed')
* `location_id` INT

### Equipment

* `equipment_id` INT (PK)
* `name` VARCHAR(100)
* `type` ENUM('Weapon','Vehicle','Electronic','Other')
* `unit_id`, `location_id` INT
* `status` ENUM('Operational','Maintenance','Decommissioned')

### Supplies

* `supply_id` INT (PK)
* `name`, `type` VARCHAR(100)
* `quantity` INT
* `unit_id`, `location_id` INT
* `status` ENUM('Available','In Use','Out of Stock')

---

## Setup & Installation

### 1. MySQL Database

1. Install MySQL and start the service.
2. Create the database:

   ```sql
   CREATE DATABASE war;
   USE war;
   ```
3. Import schema and sample data:

   ```bash
   mysql -u USERNAME -p war < schema.sql
   mysql -u USERNAME -p war < data.sql
   ```

### 2. Java Application

1. Open your IDE (e.g., Eclipse, IntelliJ).
2. Import the project as a Maven/Gradle project (if applicable).
3. Ensure the JDBC driver is on the classpath (`mysql-connector-java`).
4. Update `DatabaseHelper.java` with your DB credentials.
5. Run the main class (e.g., `BarChartExample`) to launch the application.

### 3. Python Visualization (Optional)

If you want advanced data analysis and interactive plots:

1. Install Python (>=3.8) and pip.
2. Create a virtual environment:

   ```bash
   python -m venv venv
   source venv/bin/activate  # On Windows: venv\\Scripts\\activate
   ```
3. Install dependencies:

   ```bash
   pip install -r requirements.txt
   ```
4. Run sample script:

   ```bash
   python visualize_missions.py
   ```

---

## Usage

* **Java UI**: Use the GUI to browse and manage records.
* **Python scripts**: Run analysis scripts in the `scripts/` folder.

---

## Contributing

1. Fork the repo.
2. Create a feature branch:

   ```bash
   git checkout -b improve-readme
   ```
3. Commit your changes:

   ```bash
   git commit -m "Improve README with Table of Contents and setup guides"
   ```
4. Push to your fork:

   ```bash
   git push origin improve-readme
   ```
5. Open a Pull Request.

---

## License

This project is licensed under the **Apache License 2.0**. See `LICENSE` for details.

i made some changes in requirements.txt now how to pus
