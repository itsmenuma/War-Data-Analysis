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



def validate_csv(file_path: str) -> bool:
    """
    Validates if a given CSV file exists, is not empty, and is readable.

    Args:
        file_path (str): The absolute path to the CSV file.

    Returns:
        bool: True if the file is valid, False otherwise.
    """
    if not os.path.exists(file_path):
        print(f"Error: {file_path} does not exist.")
        return False

    if os.path.getsize(file_path) == 0:
        print(f"Error: {file_path} is empty.")
        return False

    if not os.access(file_path, os.R_OK):
        print(f"Error: No read permissions for {file_path}.")
        return False

    try:
        with open(file_path, 'r', newline='') as f:
            # Attempt to read the first line or use csv.reader to check format
            csv.reader(f).__next__() # Try to read the first row to validate CSV format
        print(f"{file_path} is OK.")
        return True
    except csv.Error as e:
        print(f"Error: {file_path} has a malformed CSV format: {e}")
        return False
    except Exception as e:
        print(f"Error: Could not process {file_path}: {e}")
        return False




if __name__ == "__main__":
    print("--- Validating Data Files ---")
    all_present = True
    for file in csv_files:
        if not validate_csv(file):
            all_present = False

    if all_present:
        print("\nAll essential data files are present.")
    else:
        print("\nWarning: Some essential data files are missing. Please check the errors above.")