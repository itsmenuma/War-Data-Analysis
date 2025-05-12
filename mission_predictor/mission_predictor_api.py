from flask import Flask, request, jsonify
from tensorflow.keras.models import load_model
import numpy as np
import json

app = Flask(__name__)
model = load_model("mission_success_model.h5")

@app.route('/predict', methods=['POST'])
def predict():
    data = request.json  # Should be a dict with model features
    input_features = np.array([list(data.values())])
    prediction = model.predict(input_features)[0][0]
    return jsonify({'success_probability': float(prediction)})

if __name__ == '__main__':
    app.run(port=5000)
