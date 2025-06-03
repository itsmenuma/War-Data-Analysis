import pandas as pd
import numpy as np
from ml_predictor import MissionPredictor
import logging
import os

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

def get_user_input():
    print("\nPlease enter mission parameters:")
    print("-" * 30)
    
    inputs = {}
    
    # Unit type (1-4: Infantry, Armor, Artillery, Special Forces)
    while True:
        try:
            unit_type = int(input("Unit Type (1: Infantry, 2: Armor, 3: Artillery, 4: Special Forces): "))
            if 1 <= unit_type <= 4:
                inputs['unit_type'] = unit_type
                break
            print("Please enter a number between 1 and 4")
        except ValueError:
            print("Please enter a valid number")
    
    # Personnel count
    while True:
        try:
            count = int(input("Personnel Count (50-1000): "))
            if 50 <= count <= 1000:
                inputs['personnel_count'] = count
                break
            print("Please enter a number between 50 and 1000")
        except ValueError:
            print("Please enter a valid number")
    
    # Equipment operational ratio
    while True:
        try:
            ratio = float(input("Equipment Operational Ratio (0.0-1.0): "))
            if 0 <= ratio <= 1:
                inputs['equipment_operational_ratio'] = ratio
                break
            print("Please enter a number between 0.0 and 1.0")
        except ValueError:
            print("Please enter a valid number")
    
    # Mission duration
    while True:
        try:
            duration = int(input("Mission Duration in Days (1-90): "))
            if 1 <= duration <= 90:
                inputs['duration_days'] = duration
                break
            print("Please enter a number between 1 and 90")
        except ValueError:
            print("Please enter a valid number")
    
    # Location risk (1-5: Low to High)
    while True:
        try:
            risk = int(input("Location Risk Level (1: Very Low, 2: Low, 3: Medium, 4: High, 5: Very High): "))
            if 1 <= risk <= 5:
                inputs['location_risk'] = risk
                break
            print("Please enter a number between 1 and 5")
        except ValueError:
            print("Please enter a valid number")
    
    # Enemy strength (1-5: Weak to Strong)
    while True:
        try:
            strength = int(input("Enemy Strength (1: Very Weak, 2: Weak, 3: Medium, 4: Strong, 5: Very Strong): "))
            if 1 <= strength <= 5:
                inputs['enemy_strength'] = strength
                break
            print("Please enter a number between 1 and 5")
        except ValueError:
            print("Please enter a valid number")
    
    # Terrain difficulty (1-5: Easy to Difficult)
    while True:
        try:
            terrain = int(input("Terrain Difficulty (1: Very Easy, 2: Easy, 3: Medium, 4: Difficult, 5: Very Difficult): "))
            if 1 <= terrain <= 5:
                inputs['terrain_difficulty'] = terrain
                break
            print("Please enter a number between 1 and 5")
        except ValueError:
            print("Please enter a valid number")
    
    # Weather condition (1-5: Favorable to Severe)
    while True:
        try:
            weather = int(input("Weather Conditions (1: Very Good, 2: Good, 3: Medium, 4: Bad, 5: Very Bad): "))
            if 1 <= weather <= 5:
                inputs['weather_condition'] = weather
                break
            print("Please enter a number between 1 and 5")
        except ValueError:
            print("Please enter a valid number")
    
    return inputs

def display_prediction(prediction):
    print("\nMission Prediction Results")
    print("=" * 50)
    print(f"Success Probability: {prediction['success_probability']*100:.1f}%")
    print(f"Estimated Casualties: {prediction['estimated_casualties']} personnel")
    print(f"Estimated Resource Needs: {prediction['estimated_resources']:,.2f} units")
    print("=" * 50)

def main():
    # Check if model files exist
    if not os.path.exists('models/success_model.pkl'):
        print("Training new models using mock_mission_data.xlsx...")
        try:
            data = pd.read_excel('mock_mission_data.xlsx')
            predictor = MissionPredictor()
            X, y = predictor.preprocess_data(data)
            predictor.train_models(X, y)
            predictor.save_models()
            print("Models trained successfully!")
        except Exception as e:
            print(f"Error training models: {e}")
            return
    
    # Load existing models
    predictor = MissionPredictor()
    predictor.load_models()
    
    while True:
        # Get user input
        inputs = get_user_input()
        
        # Make prediction
        prediction = predictor.predict(inputs)
        
        # Display results
        display_prediction(prediction)
        
        # Ask if user wants to make another prediction
        again = input("\nWould you like to make another prediction? (y/n): ")
        if again.lower() != 'y':
            break

if __name__ == '__main__':
    main()

