INSERT INTO Personnel (personnel_id, first_name, last_name, post, unit_id, role, status, contact_information) VALUES
(1, 'John', 'Doe', 'Captain', 1, 'Commander', 'active', 'john.doe@military.org'),
(2, 'Sarah', 'Connor', 'Lieutenant', 1, 'Platoon Leader', 'active', 's.connor@military.org'),
(3, 'Tom', 'Hardy', 'Sergeant', 2, 'Squad Leader', 'injured', 't.hardy@military.org'),
(4, 'Emily', 'Clark', 'Private', 2, 'Infantry', 'active', 'emily.clark@military.org'),
(5, 'Mike', 'Jordan', 'Corporal', 3, 'Technician', 'active', 'm.jordan@military.org'),
(6, 'Anna', 'Smith', 'Lieutenant', 3, 'Communications', 'MIA', 'anna.smith@military.org'),
(7, 'Robert', 'Lee', 'Captain', 4, 'Artillery Commander', 'KIA', 'r.lee@military.org'),
(8, 'Jake', 'Peralta', 'Private', 4, 'Artillery', 'active', 'jake.p@military.org'),
(9, 'Sophia', 'Turner', 'Sergeant', 1, 'Logistics', 'active', 's.turner@military.org'),
(10, 'David', 'Miller', 'Private', 2, 'Infantry', 'active', 'd.miller@military.org');

INSERT INTO Locations (location_id, name, coordinates) VALUES
(1, 'Base Alpha', '35.6895 N, 139.6917 E'),
(2, 'Camp Bravo', '34.0522 N, 118.2437 W'),
(3, 'Fort Charlie', '51.5074 N, 0.1278 W'),
(4, 'Outpost Delta', '40.7128 N, 74.0060 W'),
(5, 'Site Echo', '48.8566 N, 2.3522 E'),
(6, 'Zone Foxtrot', '52.5200 N, 13.4050 E'),
(7, 'Point Golf', '41.9028 N, 12.4964 E'),
(8, 'Sector Hotel', '55.7558 N, 37.6176 E'),
(9, 'Checkpoint India', '37.7749 N, 122.4194 W'),
(10, 'Base Juliet', '28.6139 N, 77.2090 E');

INSERT INTO Units (unit_id, unit_name, unit_type, commander_id, location_id) VALUES
(1, 'Alpha Infantry', 'infantry', 1, 1),
(2, 'Bravo Infantry', 'infantry', 2, 2),
(3, 'Echo Engineers', 'cavalry', 5, 3),
(4, 'Delta Artillery', 'artillery', 7, 4),
(5, 'Charlie Support', 'cavalry', 6, 5),
(6, 'Foxtrot Scouts', 'infantry', 9, 6),
(7, 'Hotel Bomb Squad', 'artillery', 8, 7),
(8, 'India Logistics', 'cavalry', 3, 8),
(9, 'Juliet Rapid Response', 'infantry', 4, 9),
(10, 'Kilo Command', 'artillery', 10, 10);

INSERT INTO Missions (mission_id, name, objective, start_date, end_date, status, location_id) VALUES
(1, 'Operation Dawn', 'Secure enemy stronghold', '2025-01-10', '2025-01-20', 'completed', 1),
(2, 'Night Watch', 'Patrol perimeter', '2025-02-01', '2025-02-10', 'completed', 2),
(3, 'Iron Shield', 'Defensive mission', '2025-03-01', '2025-03-15', 'ongoing', 3),
(4, 'Silent Strike', 'Infiltration operation', '2025-03-20', '2025-03-30', 'planned', 4),
(5, 'Desert Eagle', 'Rescue hostages', '2025-04-01', '2025-04-05', 'completed', 5),
(6, 'Frozen Path', 'Arctic exploration', '2025-04-10', '2025-04-20', 'ongoing', 6),
(7, 'Storm Breaker', 'Supply line sabotage', '2025-04-22', '2025-05-01', 'planned', 7),
(8, 'Crimson Tide', 'Amphibious assault', '2025-05-01', '2025-05-10', 'planned', 8),
(9, 'Thunder Strike', 'Airstrike coordination', '2025-05-11', '2025-05-15', 'ongoing', 9),
(10, 'Final Stand', 'Last line defense', '2025-05-20', '2025-05-30', 'planned', 10);

INSERT INTO Equipment (equipment_id, name, type, unit_id, status, location_id) VALUES
(1, 'M16 Rifle', 'Weapon', 1, 'Operational', 1),
(2, 'Humvee', 'Vehicle', 2, 'Maintenance', 2),
(3, 'Radio Set', 'Electronic', 3, 'Operational', 3),
(4, 'Howitzer', 'Weapon', 4, 'Operational', 4),
(5, 'Night Vision Goggles', 'Electronic', 5, 'Operational', 5),
(6, 'Tank T-90', 'Vehicle', 4, 'Maintenance', 4),
(7, 'Drone Recon X1', 'Electronic', 6, 'Operational', 6),
(8, 'Mortar', 'Weapon', 7, 'Decommissioned', 7),
(9, 'Satellite Phone', 'Electronic', 8, 'Operational', 8),
(10, 'Bulletproof Vest', 'Other', 9, 'Operational', 9);

INSERT INTO Supplies (supply_id, name, type, quantity, unit_id, location_id, status) VALUES
(1, 'Ammunition Box', 'Ammo', 500, 1, 1, 'Available'),
(2, 'First Aid Kit', 'Medical', 50, 2, 2, 'Available'),
(3, 'Rations Pack', 'Food', 300, 3, 3, 'In Use'),
(4, 'Fuel Canisters', 'Fuel', 100, 4, 4, 'Available'),
(5, 'Water Bottles', 'Water', 1000, 5, 5, 'Available'),
(6, 'Repair Kits', 'Tools', 30, 6, 6, 'In Use'),
(7, 'Camouflage Net', 'Cover', 10, 7, 7, 'Available'),
(8, 'Tents', 'Shelter', 20, 8, 8, 'Out of Stock'),
(9, 'Medical Syringes', 'Medical', 150, 9, 9, 'Available'),
(10, 'Batteries', 'Electronic', 200, 10, 10, 'Available');
