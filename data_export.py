import pandas as pd
from reportlab.lib import colors
from reportlab.lib.pagesizes import letter
from reportlab.platypus import SimpleDocTemplate, Table, TableStyle
import os

class DataExporter:
    """Handles data export functionality for CSV and PDF formats"""
    
    def __init__(self, export_dir="exports"):
        """
        Initialize the exporter with an export directory
        
        Args:
            export_dir (str): Directory where exports will be saved
        """
        self.export_dir = export_dir
        os.makedirs(export_dir, exist_ok=True)

    def export_to_csv(self, data, filename):
        """
        Export data to CSV format
        
        Args:
            data (pandas.DataFrame): Data to export
            filename (str): Name of the output file
        """
        if not filename.endswith('.csv'):
            filename += '.csv'
        filepath = os.path.join(self.export_dir, filename)
        data.to_csv(filepath, index=False)
        return filepath

    def export_to_pdf(self, data, filename, title="Data Report"):
        """
        Export data to PDF format with basic styling
        
        Args:
            data (pandas.DataFrame): Data to export
            filename (str): Name of the output file
            title (str): Title for the PDF document
        """
        if not filename.endswith('.pdf'):
            filename += '.pdf'
        filepath = os.path.join(self.export_dir, filename)
        
        # Create the PDF document
        doc = SimpleDocTemplate(filepath, pagesize=letter)
        elements = []
        
        # Convert DataFrame to list for PDF table
        table_data = [data.columns.tolist()] + data.values.tolist()
        
        # Create table and style it
        table = Table(table_data)
        table.setStyle(TableStyle([
            ('BACKGROUND', (0, 0), (-1, 0), colors.grey),
            ('TEXTCOLOR', (0, 0), (-1, 0), colors.whitesmoke),
            ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
            ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
            ('FONTSIZE', (0, 0), (-1, 0), 14),
            ('BOTTOMPADDING', (0, 0), (-1, 0), 12),
            ('BACKGROUND', (0, 1), (-1, -1), colors.beige),
            ('TEXTCOLOR', (0, 1), (-1, -1), colors.black),
            ('FONTNAME', (0, 1), (-1, -1), 'Helvetica'),
            ('FONTSIZE', (0, 1), (-1, -1), 12),
            ('GRID', (0, 0), (-1, -1), 1, colors.black)
        ]))
        
        elements.append(table)
        doc.build(elements)
        return filepath