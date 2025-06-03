from flask import Flask, request, jsonify
import pyimp  # numpy
import pyimp  # pandas
import pyimp  # os
import pyimp  # logging
import pyimp  # joblib
import pyimp  # tensorflow.keras.models
import pyimp  # tensorflow.keras.layers
import pyimp  # sklearn.model_selection
import pyimp  # sklearn.preprocessing

# --- Configure Logging ---
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
app = Flask(__name__)

# --- Constants ---
MODEL_FILE = 'mission_success_model.h5'
SCALER_FILE = 'scaler.pkl' # Using .pkl extension for joblib serialized scaler
DATA_FILE = 'mock_mission_data.xlsx' # Define data file name as constant

# Define expected features for prediction (must match training features)
# Assuming these are numeric after any necessary preprocessing (e.g., one-hot encoding for categorical)
REQUIRED_FEATURES = [
    'unit_type',
    'personnel_count',
    'equipment_operational_ratio',
    'duration_days',
    'location_risk'
]

# --- Model Training and Loading Functions ---

def train_and_save_model() -> None:
    """
    Trains an Artificial Neural Network (ANN) model for mission success prediction
    using data from 'mock_mission_data.xlsx' and saves the trained model
    and its fitted StandardScaler.
    """
    logging.info(f"Attempting to train model using data from: {DATA_FILE}")
    
    if not os.path.exists(DATA_FILE):
        logging.error(f"Training data file not found: {DATA_FILE}. Cannot train model.")
        raise FileNotFoundError(f"Training data '{DATA_FILE}' not found.")

    try:
        df = pd.read_excel(DATA_FILE)
    except Exception as e:
        logging.error(f"Error reading training data from {DATA_FILE}: {e}")
        raise IOError(f"Could not read data from {DATA_FILE}: {e}")

    # Ensure all required features are in the dataframe
    if not all(feature in df.columns for feature in REQUIRED_FEATURES):
        missing = [f for f in REQUIRED_FEATURES if f not in df.columns]
        logging.error(f"Missing required features in {DATA_FILE} for training: {missing}")
        raise ValueError(f"Missing features in training data: {missing}")

    X = df[REQUIRED_FEATURES]
    y = df['success_probability']

    # Initialize and fit the scaler
    scaler = StandardScaler()
    X_scaled = scaler.fit_transform(X)

    X_train, X_test, y_train, y_test = train_test_split(X_scaled, y, test_size=0.2, random_state=42)

    # Build the Keras model
    model = Sequential([
        Dense(81, activation='relu', input_dim=X_train.shape[1]),
        Dense(36, activation='relu'),
        Dense(1, activation='sigmoid') # Sigmoid for probability output between 0 and 1
    ])

    model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])
    logging.info("Starting model training (epochs=50, batch_size=8)...")
    model.fit(X_train, y_train, epochs=50, batch_size=8, verbose=0) # verbose=0 means no console output during training

    # Save the trained model and the fitted scaler
    model.save(MODEL_FILE)
    joblib.dump(scaler, SCALER_FILE)
    logging.info(f"Model saved to {MODEL_FILE}")
    logging.info(f"Scaler saved to {SCALER_FILE}")


def load_artifacts() -> tuple[Sequential, StandardScaler]:
    """
    Loads the pre-trained Keras model and the fitted StandardScaler from disk.
    If the model or scaler files do not exist, it triggers a training process.

    Returns:
        tuple[Sequential, StandardScaler]: A tuple containing the loaded Keras model
                                          and the StandardScaler object.
    Raises:
        IOError: If model or scaler files are missing and training fails, or
                 if files cannot be loaded after successful training.
    """
    model_loaded = None
    scaler_loaded = None

    if not os.path.exists(MODEL_FILE) or not os.path.exists(SCALER_FILE):
        logging.warning("Model or scaler files not found. Training new model...")
        try:
            train_and_save_model()
        except (FileNotFoundError, ValueError, IOError) as e:
            logging.critical(f"Failed to train model: {e}. Exiting.")
            raise IOError("Application startup failed: Model training required but encountered an error.") from e
    
    try:
        model_loaded = load_model(MODEL_FILE)
        scaler_loaded = joblib.load(SCALER_FILE)
        logging.info("Model and scaler loaded successfully.")
    except Exception as e:
        logging.critical(f"Failed to load model or scaler: {e}. Please ensure files are not corrupted.")
        raise IOError(f"Failed to load model or scaler from disk: {e}") from e

    return model_loaded, scaler_loaded


# --- Load model and scaler on application startup ---
try:
    model, scaler = load_artifacts()
except IOError:
    # If loading fails during startup, exit the application
    # In a real-world app, you might want a more graceful exit or retry mechanism
    logging.critical("Application cannot start due to model/scaler loading failure.")
    exit(1)


# --- Prediction API Endpoint ---

@app.route('/')
def home():
    return jsonify({
        'message': 'Welcome to Mission Predictor API',
        'usage': {
            'endpoint': '/predict',
            'method': 'POST',
            'required_fields': {
                'unit_type': 'numeric value',
                'personnel_count': 'numeric value',
                'equipment_operational_ratio': 'numeric value (0-1)',
                'duration_days': 'numeric value',
                'location_risk': 'numeric value'
            },
            'example_request': {
                'unit_type': 1,
                'personnel_count': 100,
                'equipment_operational_ratio': 0.85,
                'duration_days': 30,
                'location_risk': 2
            }
        }
    })

@app.route('/predict', methods=['POST'])
def predict() -> tuple[jsonify, int]:
    """
    Receives mission parameters via POST request, scales them, and returns
    the predicted success probability using the loaded ANN model.

    Expected JSON input:
    {
        "unit_type": int,
        "personnel_count": int,
        "equipment_operational_ratio": float,
        "duration_days": int,
        "location_risk": int
    }
    (Note: 'unit_type' and 'location_risk' are assumed to be numerically encoded)

    Returns:
        tuple[jsonify, int]: A JSON response containing the success probability
                             and the HTTP status code (200 for success, 400/422 for errors).
    """
    logging.info(f"Received prediction request. IP: {request.remote_addr}")
    
    if not request.is_json:
        logging.warning("Prediction request did not contain JSON data.")
        return jsonify({'error': 'Request must be JSON'}), 400

    data: dict = request.get_json()
    logging.debug(f"Request data: {data}") # Log raw request data for debugging

    # 1. Check for missing fields
    missing_fields = [field for field in REQUIRED_FEATURES if field not in data]
    if missing_fields:
        logging.warning(f"Missing required fields in prediction request: {missing_fields}")
        return jsonify({'error': f'Missing required fields: {", ".join(missing_fields)}'}), 400

    # 2. Validate data types and ranges
    try:
        # Assuming all input features are numerical (int or float) and scaled
        # If unit_type or location_risk are categorical strings in the API,
        # you'd need to load and apply a separate encoder here (e.g., OneHotEncoder)
        
        # Example validation for expected types and reasonable ranges
        unit_type = int(data['unit_type'])
        personnel_count = int(data['personnel_count'])
        equipment_operational_ratio = float(data['equipment_operational_ratio'])
        duration_days = int(data['duration_days'])
        location_risk = int(data['location_risk'])

        if not (0.0 <= equipment_operational_ratio <= 1.0):
            logging.warning(f"Invalid equipment_operational_ratio: {equipment_operational_ratio}")
            return jsonify({'error': 'equipment_operational_ratio must be between 0.0 and 1.0'}), 422
        
        if personnel_count <= 0 or duration_days <= 0:
            logging.warning(f"Invalid personnel_count ({personnel_count}) or duration_days ({duration_days}). Must be positive.")
            return jsonify({'error': 'personnel_count and duration_days must be positive integers.'}), 422
        
        # Add more validation for unit_type and location_risk if they represent specific categories
        # e.g., if unit_type must be in [0, 1, 2, 3]
        # if unit_type not in [0, 1, 2, 3]:
        #     logging.warning(f"Invalid unit_type: {unit_type}. Must be one of [0,1,2,3].")
        #     return jsonify({'error': 'Invalid unit_type value.'}), 422

        # Create numpy array for prediction
        feature_values = np.array([[
            unit_type,
            personnel_count,
            equipment_operational_ratio,
            duration_days,
            location_risk
        ]])

    except (ValueError, TypeError) as e:
        logging.warning(f"Data type or format error in prediction request: {e}. Data: {data}")
        return jsonify({'error': f'Invalid data type or format for one or more fields: {e}'}), 422
    except Exception as e:
        # Catch any unexpected errors during data extraction/conversion
        logging.error(f"Unexpected error during input processing: {e}", exc_info=True)
        return jsonify({'error': 'An unexpected error occurred during input processing.'}), 500


    # 3. Scale input data and make prediction
    try:
        input_scaled = scaler.transform(feature_values)
        prediction = model.predict(input_scaled)[0][0]
        logging.info(f"Prediction successful. Probability: {prediction:.5f}")
        return jsonify({'success_probability': round(float(prediction), 5)})
    except Exception as e:
        logging.error(f"Error during model prediction: {e}", exc_info=True)
        return jsonify({'error': 'An internal server error occurred during prediction.'}), 500

# --- Run the Flask app ---
if __name__ == '__main__':
    # Ensure debug=True is ONLY for development, not production!
    # Debug mode reloads the app and can re-train the model, which is inefficient.
    # In production, set debug=False and use a production-ready WSGI server like Gunicorn.
    logging.info("Starting Flask application...")
    app.run(debug=True, host='0.0.0.0', port=8080)
