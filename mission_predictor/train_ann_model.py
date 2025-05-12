from flask import Flask, request, jsonify
import numpy as np
import pandas as pd
import os
from tensorflow.keras.models import load_model, Sequential
from tensorflow.keras.layers import Dense
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler

app = Flask(__name__)

MODEL_FILE = 'mission_success_model.h5'

def train_and_save_model():
    df = pd.read_excel('mock_mission_data.xlsx')

    X = df[['unit_type', 'personnel_count', 'equipment_operational_ratio', 'duration_days', 'location_risk']]
    y = df['success_probability']

    scaler = StandardScaler()
    X_scaled = scaler.fit_transform(X)

    X_train, X_test, y_train, y_test = train_test_split(X_scaled, y, test_size=0.2, random_state=42)

    model = Sequential([
        Dense(64, activation='relu', input_dim=X_train.shape[1]),
        Dense(32, activation='relu'),
        Dense(1, activation='sigmoid')
    ])

    model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])
    model.fit(X_train, y_train, epochs=50, batch_size=8, verbose=0)

    model.save(MODEL_FILE)
    np.save('scaler_mean.npy', scaler.mean_)
    np.save('scaler_scale.npy', scaler.scale_)

def load_scaler():
    scaler = StandardScaler()
    scaler.mean_ = np.load('scaler_mean.npy')
    scaler.scale_ = np.load('scaler_scale.npy')
    return scaler

# Train and save model only once
if not os.path.exists(MODEL_FILE):
    train_and_save_model()

model = load_model(MODEL_FILE)
scaler = load_scaler()

@app.route('/predict', methods=['POST'])
def predict():
    try:
        data = request.get_json()
        required_fields = ['unit_type', 'personnel_count', 'equipment_operational_ratio', 'duration_days', 'location_risk']

        if not all(field in data for field in required_fields):
            return jsonify({'error': 'Missing required fields'}), 400

        input_data = np.array([[data['unit_type'], data['personnel_count'], data['equipment_operational_ratio'],
                                data['duration_days'], data['location_risk']]])
        input_scaled = scaler.transform(input_data)
        prediction = model.predict(input_scaled)[0][0]

        return jsonify({'success_probability': round(float(prediction), 5)})

    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True)
