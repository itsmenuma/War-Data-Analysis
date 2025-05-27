**War Management System**

A comprehensive database and visualization tool designed to manage and analyze military resources, personnel, missions, and equipment, using historical World War II data for strategic insights and historical analysis.

---

## Table of Contents

1. [Overview](#overview)
2. [Features](#features)
3. [Database Schema](#database-schema)
4. [Setup & Installation](#setup--installation)

   - [1. MySQL Database](#1-mysql-database)
   - [2. Java Application](#2-java-application)
   - [3. Python Visualization (Optional)](#3-python-visualization-optional)

5. [Usage](#usage)
6. [Contributing](#contributing)
7. [License](#license)

---

## Overview

The War Management System allows you to:

- Track military **personnel**, **units**, **missions**, **equipment**, and **supplies**.
- Visualize data through charts for better analysis of logistics and mission outcomes.

## Technologies Used

- **Backend**: Java 21
- **Frontend**: JavaFX 21
- **Database**: MySQL 8.0
- **Build Tool**: Maven
- **Visualization**: JavaFX Charts
- **Analytics**: Python (NumPy, Pandas, Matplotlib)
- **Machine Learning**: TensorFlow, Scikit-learn

---

## Screenshots

![War Management Dashboard](/wm_1.png)

_Main dashboard of the War Management System_

---

## Features

- **Personnel Management**: View and update status (active, injured, MIA, KIA).
- **Unit Management**: Organize by type (infantry, cavalry, artillery) and commander.
- **Mission Management**: Log mission objectives, timelines, and statuses.
- **Equipment Tracking**: Monitor operational status of weapons, vehicles, electronics.
- **Supply Chain**: Manage inventory levels and deployments across locations.
- **Data Visualization**: Generate bar charts and other plots for quick insights.

---

## Database Schema

Refer [Database Schema](docs/database-schema.md).

---

## Setup & Installation

Refer [Setup Guide](docs/setup.md).

## Usage

- **JavaFX UI**: Use the modern GUI interface to browse and manage records.
- **Python scripts**: Run analysis scripts in the `scripts/` folder.

---

## Contributing

Refer [CONTRIBUTING.md](./CONTRIBUTING.md).

## License

This project is licensed under the **Apache License 2.0**. See `LICENSE` for details.
