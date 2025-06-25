from flask import Flask, jsonify
from flask_cors import CORS
from services.conflict_service import get_conflicts_by_year
from services.region_service import get_conflicts_by_region

app = Flask(__name__)
CORS(app)  # Enable CORS for frontend access

@app.route('/api/conflicts/by-year', methods=['GET'])
def conflicts_by_year():
    try:
        data = get_conflicts_by_year()
        return jsonify(data)
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/api/conflicts/by-region', methods=['GET'])
def conflicts_by_region():
    try:
        data = get_conflicts_by_region()
        return jsonify(data)
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True, port=5000)  # Adjust port if needed
