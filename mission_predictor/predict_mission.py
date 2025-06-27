import pandas as pd
import numpy as np
from ml_predictor import MissionPredictor
import logging
import os
from pathlib import Path

# Logging setup
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

def get_valid_input(prompt, cast_type, min_val, max_val):
    """Reusable input prompt with validation."""
    while True:
        try:
            val = cast_type(input(prompt))
            if min_val <= val <= max_val:
                return val
            print(f"Please enter a value between {min_val} and {max_val}")
        except ValueError:
            print("Invalid input. Please try again.")

def get_user_input():
    """Collect and validate mission parameters from user."""
    print("\nPlease enter mission parameters:")
    print("-" * 30)

    inputs = {
        'unit_type': get_valid_input("Unit Type (1: Infantry, 2: Armor, 3: Artillery, 4: Special Forces): ", int, 1, 4),
        'personnel_count': get_valid_input("Personnel Count (50-1000): ", int, 50, 1000),
        'equipment_operational_ratio': get_valid_input("Equipment Operational Ratio (0.0-1.0): ", float, 0.0, 1.0),
        'duration_days': get_valid_input("Mission Duration in Days (1-90): ", int, 1, 90),
        'location_risk': get_valid_input("Location Risk Level (1: Very Low - 5: Very High): ", int, 1, 5),
        'enemy_strength': get_valid_input("Enemy Strength (1: Very Weak - 5: Very Strong): ", int, 1, 5),
        'terrain_difficulty': get_valid_input("Terrain Difficulty (1: Very Easy - 5: Very Difficult): ", int, 1, 5),
        'weather_condition': get_valid_input("Weather Conditions (1: Very Good - 5: Very Bad): ", int, 1, 5)
    }

    return inputs

def display_prediction(prediction):
    """Print mission prediction output nicely."""
    print("\nMission Prediction Results")
    print("=" * 50)
    print(f"Success Probability: {prediction['success_probability'] * 100:.1f}%")
    print(f"Estimated Casualties: {prediction['estimated_casualties']} personnel")
    print(f"Estimated Resource Needs: {prediction['estimated_resources']:,.2f} units")
    print("=" * 50)

def train_if_needed(model_path: str, data_path: str):
    """Train models if not already saved."""
    if not Path(model_path).exists():
        logging.info("Training new models using mock_mission_data.xlsx...")
        try:
            data = pd.read_excel(data_path)
            predictor = MissionPredictor()
            X, y = predictor.preprocess_data(data)
            predictor.train_models(X, y)
            predictor.save_models()
            logging.info("✅ Models trained and saved successfully.")
        except Exception as e:
            logging.error(f"❌ Failed to train models: {e}")
            raise

def run_cli():
    """Run the command-line interface loop."""
    predictor = MissionPredictor()
    predictor.load_models()

    while True:
        try:
            inputs = get_user_input()
            prediction = predictor.predict(inputs)
            display_prediction(prediction)

            again = input("\nWould you like to make another prediction? (y/n): ").strip().lower()
            if again != 'y':
                print("Thank you! Goodbye.")
                break
        except KeyboardInterrupt:
            print("\n\n❗ Exiting on user request.")
            break
        except Exception as e:
            logging.error(f"Unexpected error: {e}")
            break

def main():
    model_path = 'models/success_model.pkl'
    data_path = 'mock_mission_data.xlsx'

    try:
        train_if_needed(model_path, data_path)
        run_cli()
    except Exception as e:
        print(f"Critical error: {e}")

if __name__ == '__main__':
    main()
