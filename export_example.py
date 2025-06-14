import pandas as pd
from data_export import DataExporter

def main():
    # Create a sample DataFrame (replace with your actual data)
    sample_data = pd.DataFrame({
        'Personnel ID': ['P001', 'P002', 'P003'],
        'Name': ['John Doe', 'Jane Smith', 'Bob Johnson'],
        'Unit': ['Alpha', 'Beta', 'Charlie'],
        'Status': ['Active', 'Active', 'Reserve']
    })

    # Initialize the exporter
    exporter = DataExporter()

    # Export to CSV
    csv_path = exporter.export_to_csv(sample_data, 'personnel_report')
    print(f"CSV exported to: {csv_path}")

    # Export to PDF
    pdf_path = exporter.export_to_pdf(sample_data, 'personnel_report', 'Personnel Status Report')
    print(f"PDF exported to: {pdf_path}")

if __name__ == "__main__":
    main()