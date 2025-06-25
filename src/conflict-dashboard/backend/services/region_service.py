import pandas as pd

def get_conflicts_by_region():
    # Replace 'data/conflicts.csv' with actual dataset path if needed
    df = pd.read_csv('data/conflicts.csv')
    # Aggregate conflict counts by region
    conflicts_by_region = df.groupby('region')['conflict_count'].sum().reset_index()
    return conflicts_by_region.to_dict(orient='records')
