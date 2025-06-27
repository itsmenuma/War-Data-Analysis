from flask import Flask, request, jsonify
import numpy as np
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import LogisticRegression
from sklearn.tree import DecisionTreeRegressor, DecisionTreeClassifier
from sklearn.metrics import accuracy_score, mean_squared_error, r2_score
import matplotlib.pyplot as plt
import seaborn as sns
from typing import Tuple, Dict, List
import joblib
import logging
import os

# Configure logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

class MissionPredictor:
    def __init__(self):
        self.success_model = None
        self.casualty_model = None
        self.resource_model = None
        self.scaler = StandardScaler()
        
    def preprocess_data(self, data: pd.DataFrame) -> Tuple[pd.DataFrame, pd.DataFrame]:
        """Preprocess the input data for model training."""
        # Define features for prediction
        features = [
            'unit_type',
            'personnel_count',
            'equipment_operational_ratio',
            'duration_days',
            'location_risk',
            'enemy_strength',
            'terrain_difficulty',
            'weather_condition'
        ]
        
        # Ensure all required features are present
        missing_cols = [col for col in features if col not in data.columns]
        if missing_cols:
            raise ValueError(f"Missing required columns: {missing_cols}")
            
        X = data[features]
        y_success = data['success_probability']
        y_casualties = data['casualty_count']
        y_resources = data['resource_needs']
        
        # Scale features
        X_scaled = self.scaler.fit_transform(X)
        X_scaled = pd.DataFrame(X_scaled, columns=features)
        
        return X_scaled, pd.DataFrame({
            'success': y_success,
            'casualties': y_casualties,
            'resources': y_resources
        })
    
    def train_models(self, X: pd.DataFrame, y: pd.DataFrame) -> None:
        """Train all prediction models."""
        # Split data
        X_train, X_test, y_train, y_test = train_test_split(
            X, y, test_size=0.2, random_state=42
        )
        
        # Train success probability model (using Decision Tree for regression)
        self.success_model = DecisionTreeRegressor(random_state=42)
        self.success_model.fit(X_train, y_train['success'])
        
        # Train casualty prediction model (Decision Tree)
        self.casualty_model = DecisionTreeRegressor(random_state=42)
        self.casualty_model.fit(X_train, y_train['casualties'])
        
        # Train resource needs model (Decision Tree)
        self.resource_model = DecisionTreeRegressor(random_state=42)
        self.resource_model.fit(X_train, y_train['resources'])
        
        # Log model performance
        self._evaluate_models(X_test, y_test)
    
    def _evaluate_models(self, X_test: pd.DataFrame, y_test: pd.DataFrame) -> None:
        """Evaluate and log model performance."""
        # Success model evaluation
        success_pred = self.success_model.predict(X_test)
        success_mse = mean_squared_error(y_test['success'], success_pred)
        success_r2 = r2_score(y_test['success'], success_pred)
        logging.info(f"Success model MSE: {success_mse:.4f}, R2: {success_r2:.4f}")
        
        # Casualty model evaluation
        casualty_pred = self.casualty_model.predict(X_test)
        casualty_mse = mean_squared_error(y_test['casualties'], casualty_pred)
        casualty_r2 = r2_score(y_test['casualties'], casualty_pred)
        logging.info(f"Casualty model MSE: {casualty_mse:.4f}, R2: {casualty_r2:.4f}")
        
        # Resource model evaluation
        resource_pred = self.resource_model.predict(X_test)
        resource_mse = mean_squared_error(y_test['resources'], resource_pred)
        resource_r2 = r2_score(y_test['resources'], resource_pred)
        logging.info(f"Resource model MSE: {resource_mse:.4f}, R2: {resource_r2:.4f}")
    
    def predict(self, input_data: Dict) -> Dict:
        """Make predictions for all target variables."""
        # Convert input to DataFrame and scale
        input_df = pd.DataFrame([input_data])
        input_scaled = self.scaler.transform(input_df)
        
        # Make predictions
        success_prob = self.success_model.predict(input_scaled)[0]
        casualty_pred = self.casualty_model.predict(input_scaled)[0]
        resource_pred = self.resource_model.predict(input_scaled)[0]
        
        return {
            'success_probability': float(success_prob),
            'estimated_casualties': int(casualty_pred),
            'estimated_resources': float(resource_pred)
        }
    
    def visualize_predictions(self, X: pd.DataFrame, y: pd.DataFrame) -> None:
        """Create visualizations for model predictions."""
        # Create prediction vs actual plots
        fig, axes = plt.subplots(2, 2, figsize=(15, 15))
        fig.suptitle('Model Predictions Analysis')
        
        # Success probability distribution
        success_probs = self.success_model.predict(X)
        sns.histplot(success_probs, ax=axes[0, 0], bins=30)
        axes[0, 0].set_title('Distribution of Success Probabilities')
        axes[0, 0].set_xlabel('Success Probability')
        
        # Casualty predictions vs actual
        casualty_pred = self.casualty_model.predict(X)
        axes[0, 1].scatter(y['casualties'], casualty_pred, alpha=0.5)
        axes[0, 1].plot([y['casualties'].min(), y['casualties'].max()], 
                       [y['casualties'].min(), y['casualties'].max()], 'r--')
        axes[0, 1].set_title('Predicted vs Actual Casualties')
        axes[0, 1].set_xlabel('Actual Casualties')
        axes[0, 1].set_ylabel('Predicted Casualties')
        
        # Resource predictions vs actual
        resource_pred = self.resource_model.predict(X)
        axes[1, 0].scatter(y['resources'], resource_pred, alpha=0.5)
        axes[1, 0].plot([y['resources'].min(), y['resources'].max()], 
                        [y['resources'].min(), y['resources'].max()], 'r--')
        axes[1, 0].set_title('Predicted vs Actual Resource Needs')
        axes[1, 0].set_xlabel('Actual Resources')
        axes[1, 0].set_ylabel('Predicted Resources')
        
        # Feature importance for success prediction
        importance = pd.DataFrame({
            'feature': X.columns,
            'importance': self.success_model.feature_importances_
        })
        importance = importance.sort_values('importance', ascending=True)
        sns.barplot(data=importance, y='feature', x='importance', ax=axes[1, 1])
        axes[1, 1].set_title('Feature Importance for Success Prediction')
        
        plt.tight_layout()
        plt.savefig('prediction_analysis.png')
        plt.close()
        
    def save_models(self, path: str = 'models/') -> None:
        """Save trained models and scaler."""
        os.makedirs(path, exist_ok=True)
        joblib.dump(self.success_model, f'{path}success_model.pkl')
        joblib.dump(self.casualty_model, f'{path}casualty_model.pkl')
        joblib.dump(self.resource_model, f'{path}resource_model.pkl')
        joblib.dump(self.scaler, f'{path}scaler.pkl')
        logging.info(f"Models saved to {path}")
    
    def load_models(self, path: str = 'models/') -> None:
        """Load trained models and scaler."""
        self.success_model = joblib.load(f'{path}success_model.pkl')
        self.casualty_model = joblib.load(f'{path}casualty_model.pkl')
        self.resource_model = joblib.load(f'{path}resource_model.pkl')
        self.scaler = joblib.load(f'{path}scaler.pkl')
        logging.info(f"Models loaded from {path}")

