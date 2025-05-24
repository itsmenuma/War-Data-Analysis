# Database Schema Documentation

This document outlines the database schema used in the War Management System, including tables, relationships, and important fields.

## Overview

The War Management System uses a MySQL database with multiple interconnected tables to manage military resources, personnel, missions, and equipment.

## Tables

### Personnel

**Description**: Stores information about military personnel.

**Schema**:

```sql
CREATE TABLE Personnel (
    personnel_id INT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    post VARCHAR(50),
    unit_id INT,
    role VARCHAR(50),
    status ENUM('active', 'injured', 'MIA', 'KIA'),
    contact_information VARCHAR(100)
);
```

**Key Fields**:

- `personnel_id`: Unique identifier for personnel
- `status`: Current status (active, injured, MIA, KIA)
- `unit_id`: Foreign key reference to Units table

### Locations

**Description**: Stores information about geographical locations.

**Schema**:

```sql
CREATE TABLE Locations (
    location_id INT PRIMARY KEY,
    name VARCHAR(100),
    coordinates VARCHAR(100)
);
```

**Key Fields**:

- `location_id`: Unique identifier for location
- `coordinates`: Geographic coordinates for the location

### Units

**Description**: Stores information about military units.

**Schema**:

```sql
CREATE TABLE Units (
    unit_id INT PRIMARY KEY,
    unit_name VARCHAR(50),
    unit_type ENUM('infantry', 'cavalry', 'artillery'),
    commander_id INT,
    location_id INT,
    FOREIGN KEY (commander_id) REFERENCES Personnel(personnel_id),
    FOREIGN KEY (location_id) REFERENCES Locations(location_id)
);
```

**Key Fields**:

- `unit_id`: Unique identifier for the unit
- `unit_type`: Type of unit (infantry, cavalry, artillery)
- `commander_id`: Foreign key reference to Personnel table (commander)
- `location_id`: Foreign key reference to Locations table

### Missions

**Description**: Stores information about military missions.

**Schema**:

```sql
CREATE TABLE Missions (
    mission_id INT PRIMARY KEY,
    name VARCHAR(100),
    objective TEXT,
    start_date varchar(20),
    end_date varchar(20),
    status ENUM('planned', 'ongoing', 'completed'),
    location_id INT,
    FOREIGN KEY (location_id) REFERENCES Locations(location_id)
);
```

**Key Fields**:

- `mission_id`: Unique identifier for the mission
- `objective`: Description of the mission objective
- `status`: Current status of the mission (planned, ongoing, completed)
- `location_id`: Foreign key reference to Locations table

### Equipment

**Description**: Stores information about military equipment.

**Schema**:

```sql
CREATE TABLE Equipment (
    equipment_id INT PRIMARY KEY,
    name VARCHAR(100),
    type ENUM('Weapon', 'Vehicle', 'Electronic', 'Other'),
    unit_id INT,
    status ENUM('Operational', 'Maintenance', 'Decommissioned'),
    location_id INT,
    FOREIGN KEY (unit_id) REFERENCES Units(unit_id),
    FOREIGN KEY (location_id) REFERENCES Locations(location_id)
);
```

**Key Fields**:

- `equipment_id`: Unique identifier for the equipment
- `type`: Type of equipment (Weapon, Vehicle, Electronic, Other)
- `status`: Current status (Operational, Maintenance, Decommissioned)
- `unit_id`: Foreign key reference to Units table
- `location_id`: Foreign key reference to Locations table

### Supplies

**Description**: Stores information about military supplies.

**Schema**:

```sql
CREATE TABLE Supplies (
    supply_id INT PRIMARY KEY,
    name VARCHAR(100),
    type VARCHAR(50),
    quantity INT,
    unit_id INT,
    location_id INT,
    status ENUM('Available', 'In Use', 'Out of Stock') DEFAULT 'Available',
    FOREIGN KEY (unit_id) REFERENCES Units(unit_id),
    FOREIGN KEY (location_id) REFERENCES Locations(location_id)
);
```

**Key Fields**:

- `supply_id`: Unique identifier for the supply
- `quantity`: Available quantity of the supply
- `status`: Current status (Available, In Use, Out of Stock)
- `unit_id`: Foreign key reference to Units table
- `location_id`: Foreign key reference to Locations table

## Entity Relationship Diagram

```
Personnel ─┐
           │
           ▼
Locations ◄─── Units ◄─── Equipment
    ▲           ▲
    │           │
    └─── Missions
    │
    ▼
  Supplies
```

## Key Relationships

1. **Personnel to Units**: Personnel are assigned to Units (many-to-one)
2. **Units to Personnel**: Each Unit has a commander from Personnel (one-to-one)
3. **Units to Equipment**: Equipment is assigned to Units (many-to-one)
4. **Units to Supplies**: Supplies are assigned to Units (many-to-one)
5. **Locations to Missions**: Missions take place at Locations (many-to-one)
6. **Locations to Units**: Units are stationed at Locations (many-to-one)
7. **Locations to Equipment**: Equipment is located at Locations (many-to-one)
8. **Locations to Supplies**: Supplies are stored at Locations (many-to-one)
