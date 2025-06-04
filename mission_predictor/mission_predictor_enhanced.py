import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.model_selection import train_test_split, KFold, cross_val_score
from sklearn.preprocessing import StandardScaler
from sklearn.feature_selection import mutual_info_regression
from sklearn.metrics import r2_score, mean_squared_error
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense, Dropout, BatchNormalization
from tensorflow.keras.callbacks import EarlyStopping
from tensorflow.keras.optimizers import Adam, RMSprop
from tensorflow.keras.regularizers import l1_l2
import os

# Set random seeds for reproducibility
np.random.seed(42)
import tensorflow as tf
tf.random.set_seed(42)

class EnhancedMissionPredictor:
    def __init__(self, data_file='mock_mission_data.xlsx'):
        self.data_file = data_file
        self.models = {}
        self.histories = {}
        self.scaler = None
        self.best_model_name = None
        
    def load_and_analyze_data(self):
        """Load and analyze the dataset with feature importance"""
        print("Loading and analyzing data...")
        self.df = pd.read_excel(self.data_file)
        
        # Create visualization directory
        os.makedirs('visualizations', exist_ok=True)
        
        # Analyze feature importance
        self._analyze_feature_importance()
        
        # Create feature interactions
        self._create_feature_interactions()
        
        return self.df
    
    def _analyze_feature_importance(self):
        """Analyze feature importance using mutual information"""
        X = self.df.drop('success_probability', axis=1)
        y = self.df['success_probability']
        
        # Calculate mutual information scores
        mi_scores = mutual_info_regression(X, y)
        importance_df = pd.DataFrame({'Feature': X.columns, 'Importance': mi_scores})
        importance_df = importance_df.sort_values('Importance', ascending=False)
        
        # Plot feature importance
        plt.figure(figsize=(10, 6))
        sns.barplot(data=importance_df, x='Importance', y='Feature')
        plt.title('Feature Importance (Mutual Information)')
        plt.tight_layout()
        plt.savefig('visualizations/feature_importance.png')
        plt.close()
        
        print("\nFeature Importance:")
        print(importance_df)
        
    def _create_feature_interactions(self):
        """Create interaction features"""
        self.df['personnel_equipment'] = self.df['personnel_count'] * self.df['equipment_operational_ratio']
        self.df['risk_duration'] = self.df['location_risk'] * self.df['duration_days']
        self.df['unit_personnel'] = self.df['unit_type'] * self.df['personnel_count']
        
    def prepare_data(self, test_size=0.2):
        """Prepare data for training with feature engineering"""
        print("\nPreparing data for training...")
        
        # Select features including engineered ones
        feature_cols = [
            'unit_type', 'personnel_count', 'equipment_operational_ratio',
            'duration_days', 'location_risk', 'personnel_equipment',
            'risk_duration', 'unit_personnel'
        ]
        
        X = self.df[feature_cols]
        y = self.df['success_probability']
        
        # Split data
        X_train, X_test, y_train, y_test = train_test_split(
            X, y, test_size=test_size, random_state=42
        )
        
        # Scale features
        self.scaler = StandardScaler()
        X_train_scaled = self.scaler.fit_transform(X_train)
        X_test_scaled = self.scaler.transform(X_test)
        
        return X_train_scaled, X_test_scaled, y_train, y_test
    
    def _create_model_variants(self):
        """Create different model architectures"""
        models = {}
        
        # Simple model
        models['simple'] = Sequential([
            Dense(16, activation='relu', input_dim=8),
            Dense(8, activation='relu'),
            Dense(1, activation='sigmoid')
        ])
        
        # Deep model with regularization
        models['deep_regular'] = Sequential([
            Dense(32, activation='relu', input_dim=8, kernel_regularizer=l1_l2(l1=1e-5, l2=1e-4)),
            BatchNormalization(),
            Dropout(0.2),
            Dense(16, activation='relu', kernel_regularizer=l1_l2(l1=1e-5, l2=1e-4)),
            BatchNormalization(),
            Dense(8, activation='relu'),
            Dense(1, activation='sigmoid')
        ])
        
        # Wide model
        models['wide'] = Sequential([
            Dense(64, activation='relu', input_dim=8),
            BatchNormalization(),
            Dense(32, activation='relu'),
            Dense(1, activation='sigmoid')
        ])
        
        return models
    
    def train_and_evaluate_models(self, X_train, X_test, y_train, y_test):
        """Train and evaluate different model variants"""
        print("\nTraining and evaluating different models...")
        
        models = self._create_model_variants()
        results = {}
        
        for name, model in models.items():
            print(f"\nTraining {name} model...")
            
            # Compile model
            optimizer = Adam(learning_rate=0.001) if name == 'deep_regular' else RMSprop(learning_rate=0.001)
            model.compile(optimizer=optimizer, loss='mse', metrics=['mae'])
            
            # Train model
            early_stopping = EarlyStopping(
                monitor='val_loss',
                patience=10,
                restore_best_weights=True
            )
            
            history = model.fit(
                X_train, y_train,
                epochs=100,
                batch_size=16,
                validation_data=(X_test, y_test),
                callbacks=[early_stopping],
                verbose=1
            )
            
            # Evaluate model
            y_pred = model.predict(X_test)
            mse = mean_squared_error(y_test, y_pred)
            r2 = r2_score(y_test, y_pred)
            
            results[name] = {
                'mse': mse,
                'r2': r2,
                'model': model,
                'history': history
            }
            
            print(f"{name} model - MSE: {mse:.4f}, R²: {r2:.4f}")
        
        # Select best model
        self.best_model_name = min(results.items(), key=lambda x: x[1]['mse'])[0]
        self.model = results[self.best_model_name]['model']
        print(f"\nBest model: {self.best_model_name}")
        
        # Plot comparison
        self._plot_model_comparison(results)
        
        return results
    
    def _plot_model_comparison(self, results):
        """Plot comparison of model performances"""
        plt.figure(figsize=(12, 6))
        
        x = np.arange(len(results))
        width = 0.35
        
        mse_values = [v['mse'] for v in results.values()]
        r2_values = [v['r2'] for v in results.values()]
        
        plt.bar(x - width/2, mse_values, width, label='MSE')
        plt.bar(x + width/2, r2_values, width, label='R²')
        
        plt.xlabel('Model')
        plt.ylabel('Score')
        plt.title('Model Comparison')
        plt.xticks(x, results.keys())
        plt.legend()
        
        plt.tight_layout()
        plt.savefig('visualizations/model_comparison.png')
        plt.close()
    
    def save_model(self, model_path='mission_success_model.h5'):
        """Save the best model"""
        print(f"\nSaving best model ({self.best_model_name}) to {model_path}...")
        self.model.save(model_path)
        
        # Save scaler parameters
        np.save('scaler_mean.npy', self.scaler.mean_)
        np.save('scaler_scale.npy', self.scaler.scale_)
        print("Model and scaler parameters saved successfully")
    
    def predict(self, input_data):
        """Make predictions using the best model"""
        if isinstance(input_data, dict):
            # Create interaction features
            input_data['personnel_equipment'] = input_data['personnel_count'] * input_data['equipment_operational_ratio']
            input_data['risk_duration'] = input_data['location_risk'] * input_data['duration_days']
            input_data['unit_personnel'] = input_data['unit_type'] * input_data['personnel_count']
            
            # Convert to DataFrame with correct feature order
            input_data = pd.DataFrame([input_data])[[
                'unit_type', 'personnel_count', 'equipment_operational_ratio',
                'duration_days', 'location_risk', 'personnel_equipment',
                'risk_duration', 'unit_personnel'
            ]]
        
        # Scale the input data
        input_scaled = self.scaler.transform(input_data)
        
        # Make prediction
        prediction = self.model.predict(input_scaled)
        return float(prediction[0][0])

def main():
    # Initialize predictor
    predictor = EnhancedMissionPredictor()
    
    # Load and analyze data
    predictor.load_and_analyze_data()
    
    # Prepare data
    X_train, X_test, y_train, y_test = predictor.prepare_data()
    
    # Train and evaluate models
    results = predictor.train_and_evaluate_models(X_train, X_test, y_train, y_test)
    
    # Save best model
    predictor.save_model()
    
    # Example prediction
    sample_mission = {
        'unit_type': 1,
        'personnel_count': 200,
        'equipment_operational_ratio': 0.85,
        'duration_days': 30,
        'location_risk': 2
    }
    
    prediction = predictor.predict(sample_mission)
    print(f"\nSample Mission Success Probability: {prediction:.4f}")

if __name__ == '__main__':
    main()

