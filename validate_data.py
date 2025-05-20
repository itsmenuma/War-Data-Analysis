import os

# Dynamically build absolute paths
base_path = os.path.dirname(os.path.abspath(__file__))
csv_files = [
    os.path.join(base_path, "data", "personnel.csv"),
    os.path.join(base_path, "data", "units.csv"),
    os.path.join(base_path, "data", "locations.csv"),
    os.path.join(base_path, "data", "equipment.csv"),
    os.path.join(base_path, "data", "supplies.csv"),
    os.path.join(base_path, "data", "missions.csv")
]

def validate_csv(file_path):
    if not os.path.exists(file_path):
        print(f"Error: {file_path} does not exist.")
    else:
        print(f"{file_path} is OK.")

for file in csv_files:
    validate_csv(file)
