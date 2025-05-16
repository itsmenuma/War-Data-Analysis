package com.warManagementGUI.Units;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.warManagementGUI.DataAnalysis.UnitsBarChart;
import com.warManagementGUI.WarManagement;
import com.warManagementGUI.util.AbstractDetailsFrame;
import com.warManagementGUI.util.DBUtil;

public class Units_Interface extends AbstractDetailsFrame {

    private static final long serialVersionUID = 1L;
    private JTextField unit_id_txt;
    private JTextField unit_name_txt;
    private JTextField commander_ID_txt;
    private JTextField Location_ID_txt;
    private JTable Units_table;
    private JComboBox<String> unitTypeComboBox;
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            new Units_Interface().display();
        } catch (Exception e) {
            System.err.println("Error launching application: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error launching application: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Units_Interface() {
        super("Units", 773, 529);
        
        // Setup UI components
        setupLabels();
        setupTextFields();
        setupComboBox();
        setupTable();
        setupButtons();
        
        // Load initial data
        initializeTableData();
    }

    private void setupLabels() {
        createLabel("Units Information", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50), 30, 11, 390, 103);
        createLabel("Unit ID", 30, 124, 96, 25);
        createLabel("Unit Name", 30, 175, 96, 25);
        createLabel("Unit Type", 30, 233, 96, 25);
        createLabel("Commander ID", 30, 276, 133, 34);
        createLabel("Location ID", 30, 343, 111, 25);
    }
    
    private void setupTextFields() {
        unit_id_txt = createTextField(174, 124, 86, 20);
        unit_id_txt.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 10));
        
        unit_name_txt = createTextField(174, 180, 86, 20);
        unit_name_txt.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 10));
        
        commander_ID_txt = createTextField(174, 286, 96, 19);
        commander_ID_txt.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 10));
        
        Location_ID_txt = createTextField(174, 352, 96, 19);
        Location_ID_txt.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 10));
    }
    
    private void setupComboBox() {
        unitTypeComboBox = new JComboBox<>();
        unitTypeComboBox.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 10));
        unitTypeComboBox.setBackground(Color.WHITE);
        unitTypeComboBox.setModel(new DefaultComboBoxModel(new String[] {"Infantry", "Cavalry", "Artillery", "Armored", "Air Force", "Naval"}));
        unitTypeComboBox.setBounds(174, 235, 86, 22);
        contentPane.add(unitTypeComboBox);
    }
    
    private void setupTable() {
        Units_table = new JTable();
        Units_table.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 10));
        Units_table.setForeground(new Color(0, 0, 0));
        Units_table.setBackground(new Color(255, 255, 255));
        
        JScrollPane scrollPane = new JScrollPane(Units_table);
        scrollPane.setBounds(324, 81, 404, 330);
        contentPane.add(scrollPane);
        
        Units_table.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {
                "Unit ID", "Unit Name", "Unit Type", "Commander ID", "Location"
            }
        ));
    }
    
    private void setupButtons() {
        createButton("Add", 50, 418, 89, 44, e -> addUnit());
        createButton("Update", 149, 418, 89, 44, e -> updateUnit());
        createButton("Delete", 248, 418, 89, 44, e -> deleteUnit());
        createButton("Clear", 347, 418, 89, 44, e -> clearFields());
        createButton("Analysis", 446, 418, 89, 44, e -> showAnalysis());
        createButton("Back", 545, 418, 89, 44, e -> goBack());
    }
    
    private void initializeTableData() {
        refreshTableData();
    }
    
    private void refreshTableData() {
        DefaultTableModel model = (DefaultTableModel) Units_table.getModel();
        model.setRowCount(0);
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM units");
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("unit_id"),
                    rs.getString("unit_name"),
                    rs.getString("unit_type"),
                    rs.getString("commander_id"),
                    rs.getString("location_id")
                    // rs.getString("status")
                });
            }
        } catch (SQLException ex) {
            handleDatabaseError(ex, "Error refreshing table data");
        }
    }
    
    private void clearFields() {
        unit_id_txt.setText("");
        unit_name_txt.setText("");
        unitTypeComboBox.setSelectedIndex(0);
        commander_ID_txt.setText("");
        Location_ID_txt.setText("");
    }
    
    private void addUnit() {
        try {
            String unitId = unit_id_txt.getText();
            String unitName = unit_name_txt.getText();
            String unitType = unitTypeComboBox.getSelectedItem().toString();
            String commanderId = commander_ID_txt.getText();
            String location = Location_ID_txt.getText();
            
            if (unitId.isEmpty() || unitName.isEmpty() || commanderId.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String query = "INSERT INTO units (unit_id, unit_name, unit_type, commander_id, location, status) VALUES (?, ?, ?, ?, ?, 'Active')";
            
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                
                pstmt.setString(1, unitId);
                pstmt.setString(2, unitName);
                pstmt.setString(3, unitType);
                pstmt.setString(4, commanderId);
                pstmt.setString(5, location);
                
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Unit added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshTableData();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add unit", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            handleDatabaseError(ex, "Error adding unit");
        }
    }
    
    private void updateUnit() {
        try {
            String unitId = unit_id_txt.getText();
            String unitName = unit_name_txt.getText();
            String unitType = unitTypeComboBox.getSelectedItem().toString();
            String commanderId = commander_ID_txt.getText();
            String location = Location_ID_txt.getText();
            
            if (unitId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a unit to update", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String query = "UPDATE units SET unit_name = ?, unit_type = ?, commander_id = ?, location = ? WHERE unit_id = ?";
            
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                
                pstmt.setString(1, unitName);
                pstmt.setString(2, unitType);
                pstmt.setString(3, commanderId);
                pstmt.setString(4, location);
                pstmt.setString(5, unitId);
                
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Unit updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshTableData();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update unit", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            handleDatabaseError(ex, "Error updating unit");
        }
    }
    
    private void deleteUnit() {
        try {
            String unitId = unit_id_txt.getText();
            
            if (unitId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a unit to delete", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this unit?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                String query = "DELETE FROM units WHERE unit_id = ?";
                
                try (Connection conn = DBUtil.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(query)) {
                    
                    pstmt.setString(1, unitId);
                    
                    int rowsAffected = pstmt.executeUpdate();
                    
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Unit deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshTableData();
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete unit", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException ex) {
            handleDatabaseError(ex, "Error deleting unit");
        }
    }
    
    private void showAnalysis() {
        UnitsBarChart.showUnitTypeChart();
    }
    
    private void goBack() {
        WarManagement dashboard = new WarManagement();
        dashboard.setVisible(true);
        dispose();
    }
}
