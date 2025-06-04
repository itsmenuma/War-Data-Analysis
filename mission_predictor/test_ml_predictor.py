import pandas as pd
import numpy as np
from ml_predictor import MissionPredictor
import logging

logging.basicConfig(level=logging.INFO)

def generate_sample_data(n_samples: int = 1000):
    """Generate synthetic data for testing the models."""
    np.random.seed(42)
    
    # Generate features
    data = {
        'unit_type': np.random.randint(1, 5, n_samples),
        'personnel_count': np.random.randint(50, 1000, n_samples),
        'equipment_operational_ratio': np.random.uniform(0.6, 1.0, n_samples),
        'duration_days': np.random.randint(1, 90, n_samples),
        'location_risk': np.random.randint(1, 5, n_samples),
        'enemy_strength': np.random.randint(1, 5, n_samples),
        'terrain_difficulty': np.random.randint(1, 5, n_samples),
        'weather_condition': np.random.randint(1, 5, n_samples)
    }
    
    # Generate target variables with some realistic relationships
    df = pd.DataFrame(data)
    
    # Success probability (influenced by multiple factors)
    success_prob = (
        0.7 * df['equipment_operational_ratio'] +
        -0.1 * df['location_risk'] +
        -0.1 * df['enemy_strength'] +
        -0.05 * df['terrain_difficulty'] +
        -0.05 * df['weather_condition'] +
        np.random.normal(0, 0.1, n_samples)
    ).clip(0, 1)
    
    # Casualty count (influenced by battle intensity and duration)
    casualty_base = (
        df['personnel_count'] * 
        (0.05 + 0.03 * df['location_risk'] + 
         0.04 * df['enemy_strength'] + 
         0.02 * df['duration_days'] / 30)
    )
    casualties = (casualty_base + np.random.normal(0, casualty_base * 0.1)).astype(int).clip(0)
    
    # Resource needs (influenced by mission parameters)
    resource_base = (
        df['personnel_count'] * 100 +
        df['duration_days'] * 1000 +
        df['location_risk'] * 5000 +
        df['enemy_strength'] * 3000
    )
    resources = (resource_base + np.random.normal(0, resource_base * 0.1)).clip(0)
    
    # Add target variables to dataframe
    df['success_probability'] = success_prob
    df['casualty_count'] = casualties
    df['resource_needs'] = resources
    
    return df

def main():
    # Generate sample data
    logging.info("Generating sample data...")
    data = generate_sample_data()
    
    # Initialize and train predictor
    predictor = MissionPredictor()
    
    # Preprocess data
    logging.info("Preprocessing data...")
    X, y = predictor.preprocess_data(data)
    
    # Train models
    logging.info("Training models...")
    predictor.train_models(X, y)
    
    # Generate and save visualizations
    logging.info("Generating visualizations...")
    predictor.visualize_predictions(X, y)
    
    # Test prediction with sample input
    sample_input = {
        'unit_type': 2,
        'personnel_count': 500,
        'equipment_operational_ratio': 0.85,
        'duration_days': 30,
        'location_risk': 2,
        'enemy_strength': 3,
        'terrain_difficulty': 2,
        'weather_condition': 1
    }
    
    logging.info("Making prediction with sample input...")
    prediction = predictor.predict(sample_input)
    logging.info(f"Prediction results: {prediction}")
    
    # Save models
    logging.info("Saving models...")
    predictor.save_models()

if __name__ == '__main__':
    main()

