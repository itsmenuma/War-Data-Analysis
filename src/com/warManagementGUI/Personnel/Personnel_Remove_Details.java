package com.warManagementGUI.Personnel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.warManagementGUI.WarManagement;
import com.warManagementGUI.util.AbstractDetailsStage;
import com.warManagementGUI.util.DBUtil;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class Personnel_Remove_Details extends AbstractDetailsStage {
    
    private TextField Personnel_ID_txt;
    private TextField Fname_txt;
    private TextField Unit_ID_txt;


    public Personnel_Remove_Details() {
        super("Remove Personnel", 773, 529);
        setupLabels();
        setupTextFields();
        setupButtons();
    }

    // Setup labels
    private void setupLabels() {
        createLabel("Remove Personnel Details", TITLE_FONT, 21, 10, 593, 123);
        createLabel("Personnel ID", FIELD_FONT, 33, 154, 133, 50);
        createLabel("First Name", FIELD_FONT, 33, 226, 119, 45);
        createLabel("Unit ID", FIELD_FONT, 33, 310, 77, 45);
    }
    
    // Setup text fields
    private void setupTextFields() {
        Personnel_ID_txt = createTextField(199, 172, 96, 19);
        Fname_txt = createTextField(199, 241, 96, 19);
        Unit_ID_txt = createTextField(199, 310, 96, 19);
    }
    
    // Setup buttons
    private void setupButtons() {
        createNavButton("Back to Dashboard", WarManagement.class, 21, 397, 213, 50);
        createButton("Reset", FIELD_FONT, TEXT_COLOR, BG_COLOR, 334, 397, 133, 50, e -> resetTextFields());
        createButton("Remove", FIELD_FONT, TEXT_COLOR, BG_COLOR, 558, 397, 133, 50, e -> removeDetails());
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
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Required");
            alert.setHeaderText(null);
            alert.setContentText("Please enter at least one field to identify personnel to remove.");
            alert.showAndWait();
            return;
        }
        
        try {
            String sql = buildDeleteQuery(personnelId, firstName, unitId);
            executeDeleteQuery(sql, personnelId, firstName, unitId);
        } catch (SQLException ex) {
            handleDatabaseError(ex, "Failed to remove personnel details.");
        }
    }
    
    // Build SQL delete query based on provided fields
    private String buildDeleteQuery(String personnelId, String firstName, String unitId) {
        StringBuilder sql = new StringBuilder("DELETE FROM Personnel WHERE 1=1");
        if (!personnelId.isEmpty()) sql.append(" AND Personnel_ID = ?");
        if (!firstName.isEmpty()) sql.append(" AND Fname = ?");
        if (!unitId.isEmpty()) sql.append(" AND Unit_ID = ?");
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
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Personnel details removed successfully.");
                alert.showAndWait();
                navigateToPersonnelDetails();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No Match");
                alert.setHeaderText(null);
                alert.setContentText("No matching personnel found.");
                alert.showAndWait();
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
