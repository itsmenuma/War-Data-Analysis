import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense, Dropout
from tensorflow.keras.callbacks import EarlyStopping
from sklearn.metrics import r2_score, mean_squared_error
import os

# Set random seeds for reproducibility
np.random.seed(42)
import tensorflow as tf
tf.random.set_seed(42)

class MissionPredictor:
    def __init__(self, data_file='mock_mission_data.xlsx'):
        self.data_file = data_file
        self.model = None
        self.scaler = None
        self.history = None
        
    def load_and_analyze_data(self):
        """Load and analyze the dataset"""
        print("Loading and analyzing data...")
        self.df = pd.read_excel(self.data_file)
        
        # Print basic statistics
        print("\nDataset Shape:", self.df.shape)
        print("\nFeature Statistics:")
        print(self.df.describe())
        
        # Create visualization directory
        os.makedirs('visualizations', exist_ok=True)
        
        # Plot distributions
        self._plot_distributions()
        
        # Plot correlation matrix
        self._plot_correlation_matrix()
        
        return self.df
    
    def _plot_distributions(self):
        """Plot distribution of all features"""
        fig, axes = plt.subplots(2, 3, figsize=(15, 10))
        axes = axes.ravel()
        
        for idx, col in enumerate(self.df.columns):
            sns.histplot(data=self.df, x=col, ax=axes[idx])
            axes[idx].set_title(f'Distribution of {col}')
        
        plt.tight_layout()
        plt.savefig('visualizations/feature_distributions.png')
        plt.close()
        
    def _plot_correlation_matrix(self):
        """Plot correlation matrix of features"""
        plt.figure(figsize=(10, 8))
        sns.heatmap(self.df.corr(), annot=True, cmap='coolwarm')
        plt.title('Feature Correlation Matrix')
        plt.savefig('visualizations/correlation_matrix.png')
        plt.close()
    
    def prepare_data(self, test_size=0.2):
        """Prepare data for training"""
        print("\nPreparing data for training...")
        
        # Split features and target
        X = self.df.drop('success_probability', axis=1)
        y = self.df['success_probability']
        
        # Split into train and test sets
        X_train, X_test, y_train, y_test = train_test_split(
            X, y, test_size=test_size, random_state=42
        )
        
        # Scale features
        self.scaler = StandardScaler()
        X_train_scaled = self.scaler.fit_transform(X_train)
        X_test_scaled = self.scaler.transform(X_test)
        
        print("Training set shape:", X_train_scaled.shape)
        print("Test set shape:", X_test_scaled.shape)
        
        return X_train_scaled, X_test_scaled, y_train, y_test
    
    def build_model(self):
        """Build the neural network model"""
        print("\nBuilding neural network model...")
        
        self.model = Sequential([
            Dense(64, activation='relu', input_dim=5),
            Dropout(0.2),
            Dense(32, activation='relu'),
            Dropout(0.1),
            Dense(16, activation='relu'),
            Dense(1, activation='sigmoid')
        ])
        
        self.model.compile(
            optimizer='adam',
            loss='mean_squared_error',
            metrics=['mae']
        )
        
        print(self.model.summary())
        return self.model
    
    def train_model(self, X_train, X_test, y_train, y_test, epochs=100, batch_size=32):
        """Train the model with early stopping"""
        print("\nTraining model...")
        
        early_stopping = EarlyStopping(
            monitor='val_loss',
            patience=10,
            restore_best_weights=True
        )
        
        self.history = self.model.fit(
            X_train, y_train,
            epochs=epochs,
            batch_size=batch_size,
            validation_data=(X_test, y_test),
            callbacks=[early_stopping],
            verbose=1
        )
        
        self._plot_training_history()
        return self.history
    
    def _plot_training_history(self):
        """Plot training history"""
        plt.figure(figsize=(12, 4))
        
        plt.subplot(1, 2, 1)
        plt.plot(self.history.history['loss'], label='Training Loss')
        plt.plot(self.history.history['val_loss'], label='Validation Loss')
        plt.title('Model Loss')
        plt.xlabel('Epoch')
        plt.ylabel('Loss')
        plt.legend()
        
        plt.subplot(1, 2, 2)
        plt.plot(self.history.history['mae'], label='Training MAE')
        plt.plot(self.history.history['val_mae'], label='Validation MAE')
        plt.title('Model MAE')
        plt.xlabel('Epoch')
        plt.ylabel('MAE')
        plt.legend()
        
        plt.tight_layout()
        plt.savefig('visualizations/training_history.png')
        plt.close()
    
    def evaluate_model(self, X_test, y_test):
        """Evaluate model performance"""
        print("\nEvaluating model performance...")
        
        # Make predictions
        y_pred = self.model.predict(X_test)
        
        # Calculate metrics
        mse = mean_squared_error(y_test, y_pred)
        rmse = np.sqrt(mse)
        r2 = r2_score(y_test, y_pred)
        
        print(f"Root Mean Squared Error: {rmse:.4f}")
        print(f"R² Score: {r2:.4f}")
        
        # Plot predictions vs actual
        plt.figure(figsize=(8, 6))
        plt.scatter(y_test, y_pred, alpha=0.5)
        plt.plot([y_test.min(), y_test.max()], [y_test.min(), y_test.max()], 'r--', lw=2)
        plt.xlabel('Actual Success Probability')
        plt.ylabel('Predicted Success Probability')
        plt.title('Predicted vs Actual Success Probability')
        plt.savefig('visualizations/predictions_vs_actual.png')
        plt.close()
        
        return {'rmse': rmse, 'r2': r2}
    
    def save_model(self, model_path='mission_success_model.h5'):
        """Save the trained model"""
        print(f"\nSaving model to {model_path}...")
        self.model.save(model_path)
        
        # Save scaler parameters
        np.save('scaler_mean.npy', self.scaler.mean_)
        np.save('scaler_scale.npy', self.scaler.scale_)
        print("Model and scaler parameters saved successfully")
    
    def predict(self, input_data):
        """Make predictions for new data"""
        # Ensure input_data is 2D
        if isinstance(input_data, dict):
            input_data = pd.DataFrame([input_data])
        
        # Scale the input data
        input_scaled = self.scaler.transform(input_data)
        
        # Make prediction
        prediction = self.model.predict(input_scaled)
        return float(prediction[0][0])

def main():
    # Initialize predictor
    predictor = MissionPredictor()
    
    # Load and analyze data
    predictor.load_and_analyze_data()
    
    # Prepare data
    X_train, X_test, y_train, y_test = predictor.prepare_data()
    
    # Build and train model
    predictor.build_model()
    predictor.train_model(X_train, X_test, y_train, y_test)
    
    # Evaluate model
    metrics = predictor.evaluate_model(X_test, y_test)
    
    # Save model
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

