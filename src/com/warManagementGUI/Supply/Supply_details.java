package com.warManagementGUI.Supply;

import java.awt.Color;
import java.awt.Font;
import java.io.Serial;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.warManagementGUI.DataAnalysis.SuppliesBarChart;
import com.warManagementGUI.WarManagement;
import com.warManagementGUI.util.AbstractDetailsFrame;
import com.warManagementGUI.util.DBUtil;

public class Supply_details extends AbstractDetailsFrame {

	@Serial
	private static final long serialVersionUID = 1L;
    private final JTextField supply_ID_txt;
	private final JTextField supply_name_txt;
	private final JTextField Name_txt;
	private final JTextField Quantity_txt;
	private final JTextField Unit_ID_txt;
	private final JTextField loc_ID_txt;
	private final JTable Supply_table;
	private final JComboBox<String>Status_txt;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
        try {
            new Supply_details().display();
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(),
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

	/**
	 * Create the frame.
	 */

	public Supply_details() {
        super("Supplies", 773, 529);
         
        // ...existing UI setup code...
        createLabel("Supply Details", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 35), 10, 11, 222, 51);

		createLabel("Supply ID", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), 10, 72, 112, 44);

		createLabel("Supply type", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), 10, 164, 112, 44);

		supply_ID_txt = createTextField(152, 87, 96, 19, 10);

		supply_name_txt = createTextField(152, 179, 96, 19, 10);

		Status_txt = createComboBox(new String[]{"Available", "In Use", "Out of Stock"}, 152, 364, 96, 21);

		createButton("Refresh", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15), 183, 429, 82, 51, e -> refreshTextFields());

		createButton("Back to Dashboard", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15), 10, 429, 163, 51, e -> {
			new WarManagement().setVisible(true);
			dispose();
		});

		createButton("Delete", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15), 486, 429, 101, 51, e -> deleteSupply());

		createButton("Insert", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15), 275, 429, 82, 51, e -> insertSupply());

		createButton("Update", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15), 367, 429, 96, 51, e -> updateSupply());

		createLabel("Name", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), 10, 130, 112, 13);

		Name_txt = createTextField(152, 129, 96, 19, 10);

		createLabel("Quantity", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), 10, 218, 82, 31);

		Quantity_txt = createTextField(152, 226, 96, 19, 10);

		createLabel("Unit ID", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), 10, 259, 89, 33);

		createLabel("Location ID", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), 10, 316, 110, 31);

		createLabel("Status", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), 10, 357, 89, 31);

		Unit_ID_txt = createTextField(152, 268, 96, 19, 10);

		loc_ID_txt = createTextField(152, 324, 96, 19, 10);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(278, 23, 471, 379);
		contentPane.add(scrollPane);
		
		Supply_table = new JTable();
		scrollPane.setViewportView(Supply_table);
		Supply_table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Supply_ID", "Name", "Type", "Quantity", "Unit_ID", "Location_ID", "Status"
			}
		));
		Supply_table.setForeground(new Color(255, 255, 255));
		Supply_table.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 10));
		Supply_table.setBackground(new Color(0, 0, 0));

		createButton("Analyse", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15), 605, 428, 132, 53, e -> analyseSupply());
		loadSupplyData();
	}

	protected void updateSupply() {
		// Check if supply_ID_txt is empty
	    if (supply_ID_txt.getText().isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Please enter a Supply ID to update.", "Error", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    String updateQuery = "UPDATE Supplies SET name = ?, type = ?, quantity = ?, unit_id = ?, location_id = ?, status = ? WHERE supply_id = ?";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

	        pstmt.setString(1, supply_name_txt.getText());
	        pstmt.setString(2, Name_txt.getText());
	        pstmt.setInt(3, Integer.parseInt(Quantity_txt.getText()));
	        pstmt.setInt(4, Integer.parseInt(Unit_ID_txt.getText()));
	        pstmt.setInt(5, Integer.parseInt(loc_ID_txt.getText()));

	        // Convert selected item to String explicitly
	        String status = (String) Status_txt.getSelectedItem();
	        pstmt.setString(6, status);

	        pstmt.setInt(7, Integer.parseInt(supply_ID_txt.getText()));

	        int rowsAffected = pstmt.executeUpdate();

	        if (rowsAffected > 0) {
	            JOptionPane.showMessageDialog(this, "Supply updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
	            refreshTextFields();
	            loadSupplyData(); // Refresh table after update
	        } else {
	            JOptionPane.showMessageDialog(this, "Failed to update supply. Supply ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
	        }

	    } catch (SQLException ex) {
	        System.err.println("Database error: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
	    } catch (NumberFormatException ex) {
	        JOptionPane.showMessageDialog(this, "Please enter valid numeric values for ID, Quantity, Unit ID, and Location ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

	protected void deleteSupply() {
		 // Check if supply_ID_txt is empty
	    if (supply_ID_txt.getText().isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Please enter a Supply ID to delete.", "Error", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    String deleteQuery = "DELETE FROM Supplies WHERE supply_id = ?";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

	        pstmt.setInt(1, Integer.parseInt(supply_ID_txt.getText()));

	        int rowsAffected = pstmt.executeUpdate();

	        if (rowsAffected > 0) {
	            JOptionPane.showMessageDialog(this, "Supply deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
	            refreshTextFields();
	            loadSupplyData(); // Refresh table after deletion
	        } else {
	            JOptionPane.showMessageDialog(this, "Failed to delete supply. Supply ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
	        }

	    } catch (NumberFormatException ex) {
	        JOptionPane.showMessageDialog(this, "Please enter a valid numeric Supply ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
	    } catch (SQLException ex) {
	        System.err.println("Database error: " + ex.getMessage());
			JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

	protected void insertSupply() {
		 String insertQuery = "INSERT INTO Supplies (supply_id, name, type, quantity, unit_id, location_id, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

		    try (Connection conn = DBUtil.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

		        pstmt.setInt(1, Integer.parseInt(supply_ID_txt.getText()));
		        pstmt.setString(2, supply_name_txt.getText());
		        pstmt.setString(3, Name_txt.getText());
		        pstmt.setInt(4, Integer.parseInt(Quantity_txt.getText()));
		        pstmt.setInt(5, Integer.parseInt(Unit_ID_txt.getText()));
		        pstmt.setInt(6, Integer.parseInt(loc_ID_txt.getText()));

		        // Convert selected item to String explicitly
		        String status = (String) Status_txt.getSelectedItem();
		        pstmt.setString(7, status);

		        int rowsAffected = pstmt.executeUpdate();

		        if (rowsAffected > 0) {
		            JOptionPane.showMessageDialog(this, "Supply inserted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
		            refreshTextFields();
		            loadSupplyData(); // Refresh table after insertion
		        } else {
		            JOptionPane.showMessageDialog(this, "Failed to insert supply.", "Error", JOptionPane.ERROR_MESSAGE);
		        }

		    } catch (SQLException ex) {
		        System.err.println("Database error: " + ex.getMessage());
                		JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
		    } catch (NumberFormatException ex) {
		        JOptionPane.showMessageDialog(this, "Please enter valid numeric values for ID, Quantity, Unit ID, and Location ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
		    }
	}

	private void loadSupplyData() {
		 String query = "SELECT supply_id, name, type, quantity, unit_id, location_id, status FROM Supplies";
	        
	        try (Connection conn = DBUtil.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(query);
	             ResultSet rs = pstmt.executeQuery()) {

	            // Clear existing table data
	            DefaultTableModel model = (DefaultTableModel) Supply_table.getModel();
	            model.setRowCount(0);

	            // Iterate through the result set and populate the table
	            while (rs.next()) {
	                int supply_id = rs.getInt("supply_id");
	                String name = rs.getString("name");
	                String type = rs.getString("type");
	                int quantity = rs.getInt("quantity");
	                int unit_id = rs.getInt("unit_id");
	                int location_id = rs.getInt("location_id");
	                String status = rs.getString("status");

	                // Add row to table model
	                Object[] row = { supply_id, name, type, quantity, unit_id, location_id, status };
	                model.addRow(row);
	            }

	        } catch (SQLException ex) {
	            System.err.println("Database error: " + ex.getMessage());
                JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
	        }
	}

	protected void refreshTextFields() {
		supply_ID_txt.setText("");
		supply_name_txt.setText("");
		Name_txt.setText("");
		Quantity_txt.setText("");
		Unit_ID_txt.setText("");
		loc_ID_txt.setText("");
	}

    protected void analyseSupply() {
        SuppliesBarChart.showSupplyStatusChart();
        dispose();
    }
}
