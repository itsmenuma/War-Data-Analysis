package com.warManagementGUI.Units;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.warManagementGUI.WarManagement;
import com.warManagementGUI.util.AbstractDetailsStage;
import com.warManagementGUI.util.DBUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Units_Interface extends AbstractDetailsStage {

    private final TextField unit_id_txt;
    private final TextField unit_name_txt;
    private final TextField commander_ID_txt;
    private final TextField Location_ID_txt;
    private final TableView<UnitRecord> Units_table;
    private final ComboBox<String> unitTypeComboBox;

    public Units_Interface() {
        super("Units", 773, 529);
        
        // Initialize UI components first
        unit_id_txt = createTextField(174, 124, 86, 20);
        unit_name_txt = createTextField(174, 180, 86, 20);
        commander_ID_txt = createTextField(174, 286, 96, 19);
        Location_ID_txt = createTextField(174, 352, 96, 19);
        
        unitTypeComboBox = new ComboBox<>();
        unitTypeComboBox.getItems().addAll("infantry", "cavalry", "artillery");
        unitTypeComboBox.setValue("infantry");
        
        Units_table = new TableView<>();
        
        // Setup UI components
        setupLabels();
        setupTable();
        setupButtons();
        
        // Load initial data
        initializeTableData();
    }    private void setupLabels() {
        createLabel("Units Information", Font.font("Times New Roman", FontWeight.BOLD, 35), 30, 11, 390, 103);
        createLabel("Unit ID", Font.font("Times New Roman", FontWeight.BOLD, 15), 30, 124, 96, 25);
        createLabel("Unit Name", Font.font("Times New Roman", FontWeight.BOLD, 15), 30, 175, 96, 25);
        createLabel("Unit Type", Font.font("Times New Roman", FontWeight.BOLD, 15), 30, 233, 96, 25);
        createLabel("Commander ID", Font.font("Times New Roman", FontWeight.BOLD, 15), 30, 276, 133, 34);
        createLabel("Location ID", Font.font("Times New Roman", FontWeight.BOLD, 15), 30, 343, 111, 25);
        
        // Position combo box
        unitTypeComboBox.setLayoutX(174);
        unitTypeComboBox.setLayoutY(235);
        unitTypeComboBox.setPrefWidth(86);
        unitTypeComboBox.setPrefHeight(22);
        
        rootPane.getChildren().add(unitTypeComboBox);
    }
private void setupTable() {
    // Setup table columns
    TableColumn<UnitRecord, String> unitIdCol = new TableColumn<>("Unit ID");
    unitIdCol.setCellValueFactory(new PropertyValueFactory<>("unitId"));
    unitIdCol.setPrefWidth(80);
    
    TableColumn<UnitRecord, String> unitNameCol = new TableColumn<>("Unit Name");
    unitNameCol.setCellValueFactory(new PropertyValueFactory<>("unitName"));
    unitNameCol.setPrefWidth(100);
    
    TableColumn<UnitRecord, String> unitTypeCol = new TableColumn<>("Unit Type");
    unitTypeCol.setCellValueFactory(new PropertyValueFactory<>("unitType"));
    unitTypeCol.setPrefWidth(80);
    
    TableColumn<UnitRecord, String> commanderIdCol = new TableColumn<>("Commander ID");
    commanderIdCol.setCellValueFactory(new PropertyValueFactory<>("commanderId"));
    commanderIdCol.setPrefWidth(100);
    
    TableColumn<UnitRecord, String> locationIdCol = new TableColumn<>("Location ID");
    locationIdCol.setCellValueFactory(new PropertyValueFactory<>("locationId"));
    locationIdCol.setPrefWidth(100);
    
    Units_table.getColumns().addAll(unitIdCol, unitNameCol, unitTypeCol, commanderIdCol, locationIdCol);
    Units_table.setLayoutX(324);
    Units_table.setLayoutY(81);
    Units_table.setPrefWidth(404);
    Units_table.setPrefHeight(330);
    
    // Add row selection listener
    Units_table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
            unit_id_txt.setText(newSelection.getUnitId());
            unit_name_txt.setText(newSelection.getUnitName());
            unitTypeComboBox.setValue(newSelection.getUnitType());
            commander_ID_txt.setText(newSelection.getCommanderId());
            Location_ID_txt.setText(newSelection.getLocationId());
        }
    });
    
    rootPane.getChildren().add(Units_table);
}
    
    private void setupButtons() {
        createButton("Add", Font.font("Times New Roman", FontWeight.BOLD, 15), 50, 418, 89, 44, e -> addUnit());
        createButton("Update", Font.font("Times New Roman", FontWeight.BOLD, 15), 149, 418, 89, 44, e -> updateUnit());
        createButton("Delete", Font.font("Times New Roman", FontWeight.BOLD, 15), 248, 418, 89, 44, e -> deleteUnit());
        createButton("Clear", Font.font("Times New Roman", FontWeight.BOLD, 15), 347, 418, 89, 44, e -> clearFields());
        createButton("Analysis", Font.font("Times New Roman", FontWeight.BOLD, 15), 446, 418, 89, 44, e -> showAnalysis());
        createNavButton("Back", WarManagement.class, 545, 418, 89, 44);
    }
    
    private void initializeTableData() {
        refreshTableData();
    }
      private void refreshTableData() {
        ObservableList<UnitRecord> data = FXCollections.observableArrayList();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM units");
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                data.add(new UnitRecord(
                    rs.getString("unit_id"),
                    rs.getString("unit_name"),
                    rs.getString("unit_type"),
                    rs.getString("commander_id"),
                    rs.getString("location_id")
                ));
            }
            Units_table.setItems(data);
        } catch (SQLException ex) {
            showErrorDialog("Error refreshing table data: " + ex.getMessage());
        }
    }      private void clearFields() {
        unit_id_txt.setText("");
        unit_name_txt.setText("");
        unitTypeComboBox.setValue("infantry");
        commander_ID_txt.setText("");
        Location_ID_txt.setText("");
    }
      private void addUnit() {
        try {
            String unitId = unit_id_txt.getText();
            String unitName = unit_name_txt.getText();
            String unitType = unitTypeComboBox.getValue();
            String commanderId = commander_ID_txt.getText();
            String location = Location_ID_txt.getText();
            
            if (unitId.isEmpty() || unitName.isEmpty() || commanderId.isEmpty() || location.isEmpty()) {
                showErrorDialog("Please fill all fields");
                return;
            }
            
            String query = "INSERT INTO units (unit_id, unit_name, unit_type, commander_id, location_id) VALUES (?, ?, ?, ?, ?)";
            
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                
                pstmt.setString(1, unitId);
                pstmt.setString(2, unitName);
                pstmt.setString(3, unitType);
                pstmt.setString(4, commanderId);
                pstmt.setString(5, location);
                
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    showInformationDialog("Unit added successfully");
                    refreshTableData();
                    clearFields();
                } else {
                    showErrorDialog("Failed to add unit");
                }
            }
        } catch (SQLException ex) {
            showErrorDialog("Error adding unit: " + ex.getMessage());
        }
    }
    
    private void updateUnit() {
        try {
            String unitId = unit_id_txt.getText();
            String unitName = unit_name_txt.getText();
            String unitType = unitTypeComboBox.getValue();
            String commanderId = commander_ID_txt.getText();
            String location = Location_ID_txt.getText();
            
            if (unitId.isEmpty()) {
                showErrorDialog("Please select a unit to update");
                return;
            }
            
            String query = "UPDATE units SET unit_name = ?, unit_type = ?, commander_id = ?, location_id = ? WHERE unit_id = ?";
            
            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                
                pstmt.setString(1, unitName);
                pstmt.setString(2, unitType);
                pstmt.setString(3, commanderId);
                pstmt.setString(4, location);
                pstmt.setString(5, unitId);
                
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    showInformationDialog("Unit updated successfully");
                    refreshTableData();
                    clearFields();
                } else {
                    showErrorDialog("Failed to update unit");
                }
            }
        } catch (SQLException ex) {
            showErrorDialog("Error updating unit: " + ex.getMessage());
        }
    }
private void deleteUnit() {
    String unitId = unit_id_txt.getText();
    
    if (unitId.isEmpty()) {
        showErrorDialog("Please select a unit to delete");
        return;
    }
    
    Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
    confirmDialog.setTitle("Confirm Delete");
    confirmDialog.setHeaderText(null);
    confirmDialog.setContentText("Are you sure you want to delete this unit?");
    
    if (confirmDialog.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
        return; // User canceled
    }
    
    String query = "DELETE FROM units WHERE unit_id = ?";
    
    try (Connection conn = DBUtil.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        
        pstmt.setString(1, unitId);
        
        int rowsAffected = pstmt.executeUpdate();
        
        if (rowsAffected > 0) {
            showInformationDialog("Unit deleted successfully");
            refreshTableData();
            clearFields();
        } else {
            showErrorDialog("Failed to delete unit");
        }
    } catch (SQLException ex) {
        showErrorDialog("Error deleting unit: " + ex.getMessage());
    }
}
      private void showAnalysis() {
        try {
            // Show Units by Type chart
            com.warManagementGUI.DataAnalysis.UnitsBarChart.showUnitTypeChart();
        } catch (Exception e) {
            showErrorDialog("Error showing analysis: " + e.getMessage());
        }
    }
}
