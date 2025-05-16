package com.warManagementGUI.Personnel;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.warManagementGUI.WarManagement;
import com.warManagementGUI.util.AbstractDetailsFrame;
import com.warManagementGUI.util.DBUtil;

public class Personnel_Remove_Details extends AbstractDetailsFrame {

    private static final long serialVersionUID = 1L;
    private JTextField Personnel_ID_txt;
    private JTextField Fname_txt;
    private JTextField Unit_ID_txt;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            new Personnel_Remove_Details().display();
        } catch (Exception e) {
            System.err.println("Error launching application: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error launching application: " + e.getMessage(),
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Personnel_Remove_Details() {
        super("Remove Personnel", 773, 529);
        setupLabels();
        setupTextFields();
        setupButtons();
    }

    // Setup labels
    private void setupLabels() {
        createLabel("Remove Personnel Details", 
                   new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50), 
                   Color.WHITE, 21, 10, 593, 123);
        
        createLabel("Personnel ID", 
                   new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), 
                   Color.WHITE, 33, 154, 133, 50);
        
        createLabel("First Name", 
                   new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), 
                   Color.WHITE, 33, 226, 119, 45);
        
        createLabel("Unit ID", 
                   new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), 
                   Color.WHITE, 33, 310, 77, 45);
    }
    
    // Setup text fields
    private void setupTextFields() {
        Personnel_ID_txt = createTextField(199, 172, 96, 19, 10);
        Fname_txt = createTextField(199, 241, 96, 19, 10);
        Unit_ID_txt = createTextField(199, 310, 96, 19, 10);
    }
    
    // Setup buttons
    private void setupButtons() {
        createButton("Back to Dashboard", 
                     new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20),
                     21, 397, 213, 50,
                     e -> navigateToDashboard());
        
        createButton("Reset", 
                     new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20),
                     334, 397, 133, 50,
                     e -> resetTextFields());
        
        createButton("Remove", 
                     new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20),
                     558, 397, 133, 50,
                     e -> removeDetails());
    }

    // Navigate to dashboard
    private void navigateToDashboard() {
        new WarManagement().display();
        dispose();
    }

    // Helper method to reset text fields
    private void resetTextFields() {
        Personnel_ID_txt.setText("");
        Fname_txt.setText("");
        Unit_ID_txt.setText("");
    }

    // Helper method to remove personnel details
    private void removeDetails() {
        String personnelId = Personnel_ID_txt.getText();
        String firstName = Fname_txt.getText();
        String unitId = Unit_ID_txt.getText();

        if (personnelId.isEmpty() && firstName.isEmpty() && unitId.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter at least one field to identify personnel to remove.", 
                "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String sql = buildDeleteQuery(personnelId, firstName, unitId);
            executeDeleteQuery(sql, personnelId, firstName, unitId);
        } catch (SQLException ex) {
            System.err.println("Database error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Build SQL delete query based on provided fields
    private String buildDeleteQuery(String personnelId, String firstName, String unitId) {
        StringBuilder sql = new StringBuilder("DELETE FROM Personnel WHERE 1=1");
        if (!personnelId.isEmpty()) sql.append(" AND Personnel_id = ?");
        if (!firstName.isEmpty()) sql.append(" AND First_name = ?");
        if (!unitId.isEmpty()) sql.append(" AND Unit_Id = ?");
        return sql.toString();
    }
    
    // Execute the delete query with parameters
    private void executeDeleteQuery(String sql, String personnelId, String firstName, String unitId) 
            throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setQueryParameters(pstmt, personnelId, firstName, unitId);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Personnel details removed successfully.");
                navigateToPersonnelDetails();
            } else {
                JOptionPane.showMessageDialog(this, "No matching personnel found.");
            }
        }
    }
    
    // Set parameters for the prepared statement
    private void setQueryParameters(PreparedStatement pstmt, String personnelId, 
                                   String firstName, String unitId) throws SQLException {
        int index = 1;
        if (!personnelId.isEmpty()) pstmt.setString(index++, personnelId);
        if (!firstName.isEmpty()) pstmt.setString(index++, firstName);
        if (!unitId.isEmpty()) pstmt.setString(index++, unitId);
    }
    
    // Navigate to personnel details screen
    private void navigateToPersonnelDetails() {
        new Personnel_details().display();
        dispose();
    }
}
