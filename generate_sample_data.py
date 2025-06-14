import csv
import os

def create_sample_data():
    base_path = os.path.dirname(os.path.abspath(__file__))
    data_dir = os.path.join(base_path, "data")

    # Sample data for each CSV file
    data = {
        'personnel.csv': [
            ['id', 'name', 'role', 'unit_id', 'contact'],
            ['P001', 'John Smith', 'Paramedic', 'U001', '555-0101'],
            ['P002', 'Sarah Johnson', 'EMT', 'U001', '555-0102'],
            ['P003', 'Mike Brown', 'Driver', 'U002', '555-0103']
        ],
        'units.csv': [
            ['unit_id', 'name', 'type', 'location_id', 'status'],
            ['U001', 'Alpha Team', 'Emergency', 'L001', 'Active'],
            ['U002', 'Beta Team', 'Transport', 'L002', 'Active'],
            ['U003', 'Gamma Team', 'Support', 'L003', 'Standby']
        ],
        'locations.csv': [
            ['location_id', 'name', 'address', 'coordinates', 'type'],
            ['L001', 'Central Hospital', '123 Main St', '40.7128,-74.0060', 'Hospital'],
            ['L002', 'North Station', '456 Oak Ave', '40.7589,-73.9851', 'Station'],
            ['L003', 'South Base', '789 Pine Rd', '40.7549,-73.9840', 'Base']
        ],
        'equipment.csv': [
            ['equipment_id', 'name', 'type', 'unit_id', 'status'],
            ['E001', 'Ambulance 1', 'Vehicle', 'U001', 'Operational'],
            ['E002', 'Defibrillator', 'Medical', 'U001', 'Active'],
            ['E003', 'Stretcher', 'Medical', 'U002', 'Active']
        ],
        'supplies.csv': [
            ['supply_id', 'name', 'quantity', 'unit_id', 'status'],
            ['S001', 'Bandages', '500', 'U001', 'In Stock'],
            ['S002', 'Medication Kit', '50', 'U002', 'Low Stock'],
            ['S003', 'Oxygen Tanks', '20', 'U003', 'In Stock']
        ]
    }

    # Create and write to CSV files
    for filename, rows in data.items():
        filepath = os.path.join(data_dir, filename)
        with open(filepath, 'w', newline='') as f:
            writer = csv.writer(f)
            writer.writerows(rows)
        print(f"Generated {filename}")

if __name__ == "__main__":
    print("Generating sample data files...")
    create_sample_data()
    print("\nData generation complete. Run validate_data.py to verify.")