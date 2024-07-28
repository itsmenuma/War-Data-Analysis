# War Management System

## Overview
The War Management System is a comprehensive database and visualization tool designed to manage and analyze military resources, personnel, missions, and equipment. Using historical data from World War II, this system provides insights into various aspects of military logistics, helping in strategic decision-making and historical analysis.

## Features
- **Personnel Management**: Track the details and status of military personnel, including roles, units, and contact information.
- **Unit Management**: Manage and analyze different military units, including their types, commanders, and locations.
- **Mission Management**: Record and visualize mission details, including objectives, status, and associated locations.
- **Equipment Tracking**: Maintain an inventory of military equipment, tracking their status and deployment.
- **Supply Chain Management**: Manage supplies, track their availability, and monitor usage across units.
- **Data Visualization**: Generate bar charts and other visualizations to analyze personnel status and other key metrics.

## Database Schema
The project consists of several interconnected tables:

1. **Personnel**
   - `personnel_id`: INT (Primary Key)
   - `first_name`: VARCHAR(50)
   - `last_name`: VARCHAR(50)
   - `post`: VARCHAR(50)
   - `unit_id`: INT
   - `role`: VARCHAR(50)
   - `status`: ENUM('active', 'injured', 'MIA', 'KIA')
   - `contact_information`: VARCHAR(100)

2. **Locations**
   - `location_id`: INT (Primary Key)
   - `name`: VARCHAR(100)
   - `coordinates`: VARCHAR(100)

3. **Units**
   - `unit_id`: INT (Primary Key)
   - `unit_name`: VARCHAR(50)
   - `unit_type`: ENUM('infantry', 'cavalry', 'artillery')
   - `commander_id`: INT
   - `location_id`: INT

4. **Missions**
   - `mission_id`: INT (Primary Key)
   - `name`: VARCHAR(100)
   - `objective`: TEXT
   - `start_date`: VARCHAR(20)
   - `end_date`: VARCHAR(20)
   - `status`: ENUM('planned', 'ongoing', 'completed')
   - `location_id`: INT

5. **Equipment**
   - `equipment_id`: INT (Primary Key)
   - `name`: VARCHAR(100)
   - `type`: ENUM('Weapon', 'Vehicle', 'Electronic', 'Other')
   - `unit_id`: INT
   - `status`: ENUM('Operational', 'Maintenance', 'Decommissioned')
   - `location_id`: INT

6. **Supplies**
   - `supply_id`: INT (Primary Key)
   - `name`: VARCHAR(100)
   - `type`: VARCHAR(50)
   - `quantity`: INT
   - `unit_id`: INT
   - `location_id`: INT
   - `status`: ENUM('Available', 'In Use', 'Out of Stock')

## Setup and Installation
1. **Database Setup**: 
   - Install MySQL and create a database named `war`.
   - Import the schema and data using the provided SQL scripts.

2. **Java Application**:
   - Import the project into Eclipse or your preferred IDE.
   - Ensure the JDBC driver is included in your classpath.
   - Update the database connection details in `DatabaseHelper.java`.
   - Run the `BarChartExample` class to visualize the data.

## Usage
This system can be used to:
- Analyze military personnel data.
- Track the status and deployment of equipment.
- Visualize the distribution and status of missions.
- Manage and track the supply chain.

## Contributing
Contributions are welcome! Please fork this repository and submit pull requests for any improvements or new features.

## License
This project is open-source and available under the Apache License.

## Contact
Feel free to contact numarahamath@gmail.com
