Queries: -


Tables Created: -

Personnel Table
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
Location table: 
			CREATE TABLE Locations (
    location_id INT PRIMARY KEY,
    name VARCHAR(100),
    coordinates VARCHAR(100)
);


 Units Table:
			CREATE TABLE Units ( 
unit_id INT PRIMARY KEY, 
unit_name VARCHAR(50), 
unit_type ENUM('infantry', 'cavalry', 'artillery'), 
commander_id INT, 
location_id INT, 
FOREIGN KEY (commander_id) REFERENCES Personnel(personnel_id),
FOREIGN KEY (location_id) REFERENCES Locations(location_id)
);


Missions table:
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

Equipment Table: 
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

Supplies Table
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
