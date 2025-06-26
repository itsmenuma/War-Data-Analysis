import pandas as pd

def get_conflicts_by_year():
    # Replace 'data/conflicts.csv' with actual dataset path if needed
    df = pd.read_csv('data/conflicts.csv')
    # Aggregate conflict counts by year
    conflicts_by_year = df.groupby('year')['conflict_count'].sum().reset_index()
    return conflicts_by_year.to_dict(orient='records')
