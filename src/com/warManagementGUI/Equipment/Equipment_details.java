package com.warManagementGUI.Equipment;

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

public class Equipment_details extends AbstractDetailsStage {
    
    private static final String[] EQUIPMENT_TYPES = {"Weapon", "Vehicle", "Electronic", "Other"};
    private static final String[] STATUS_OPTIONS = {"Operational", "Maintenance", "Decommissioned"};    

    private TextField equipIdTxt, equipNameTxt, unitIdTxt, locationIdTxt;
    private ComboBox<String> equipTypeTxt, statusTxt;
    private TableView<EquipmentRecord> equipmentTable;
    private ObservableList<EquipmentRecord> equipmentData;

    public static void main(String[] args) {
        try {
            new Equipment_details().display();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Application Error");
            alert.setContentText("Error launching application: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public Equipment_details() {
        super("Equipment", 773, 529);
        setupLabels();
        setupTextFields();
        setupComboBox();
        setupTable();
        setupButtons();
        refreshTableData();
    }

    private void setupLabels() {
        createLabel("Equipment Details", TITLE_FONT, 27, 10, 275, 83);
        createLabel("Equipment ID", LABEL_FONT, 27, 90, 155, 25);
        createLabel("Name", LABEL_FONT, 27, 132, 69, 28);
        createLabel("Type", LABEL_FONT, 27, 186, 69, 25);
        createLabel("Unit ID", LABEL_FONT, 27, 223, 86, 30);
        createLabel("Status", LABEL_FONT, 27, 274, 75, 28);
        createLabel("Location Id", LABEL_FONT, 27, 321, 127, 28);
    }

    private void setupTextFields() {
        equipIdTxt = createTextField(192, 93, 86, 20);
        equipNameTxt = createTextField(192, 132, 86, 20);
        unitIdTxt = createTextField(192, 231, 86, 20);
        locationIdTxt = createTextField(192, 328, 96, 20);
    }

    private void setupComboBox() {
        equipTypeTxt = createComboBox(EQUIPMENT_TYPES, 192, 186, 86, 21);
        statusTxt = createComboBox(STATUS_OPTIONS, 192, 280, 86, 21);
    }

    private void setupTable() {
        equipmentData = FXCollections.observableArrayList();
        equipmentTable = new TableView<>(equipmentData);
        equipmentTable.setLayoutX(298);
        equipmentTable.setLayoutY(64);
        equipmentTable.setPrefWidth(451);
        equipmentTable.setPrefHeight(322);
        
        setupTableColumns();
        rootPane.getChildren().add(equipmentTable);
    }
    
    @SuppressWarnings("unchecked")
    private void setupTableColumns() {
        TableColumn<EquipmentRecord, Integer> equipIdCol = new TableColumn<>("Equipment_ID");
        equipIdCol.setCellValueFactory(new PropertyValueFactory<>("equipmentId"));
        equipIdCol.setPrefWidth(90);
        
        TableColumn<EquipmentRecord, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(80);
        
        TableColumn<EquipmentRecord, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("supplyType"));
        typeCol.setPrefWidth(80);
        
        TableColumn<EquipmentRecord, Integer> unitIdCol = new TableColumn<>("Unit ID");
        unitIdCol.setCellValueFactory(new PropertyValueFactory<>("unitId"));
        unitIdCol.setPrefWidth(60);
        
        TableColumn<EquipmentRecord, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(80);
        
        TableColumn<EquipmentRecord, Integer> locationIdCol = new TableColumn<>("Location_ID");
        locationIdCol.setCellValueFactory(new PropertyValueFactory<>("locationId"));
        locationIdCol.setPrefWidth(81);
        
        equipmentTable.getColumns().addAll(equipIdCol, nameCol, typeCol, unitIdCol, statusCol, locationIdCol);
    }

    private void setupButtons() {
        createNavButton("Back to Dashboard", WarManagement.class, 10, 410, 161, 72);
        createButton("Refresh", BUTTON_FONT, TEXT_COLOR, BUTTON_COLOR, 192, 410, 96, 72, e -> refreshTextFields());
        createButton("Delete", BUTTON_FONT, TEXT_COLOR, BUTTON_COLOR, 310, 410, 86, 72, e -> deleteEquipment());
        createButton("Update", BUTTON_FONT, TEXT_COLOR, BUTTON_COLOR, 425, 411, 86, 70, e -> updateEquipment());
        createButton("Insert", BUTTON_FONT, TEXT_COLOR, BUTTON_COLOR, 533, 413, 86, 67, e -> insertEquipment());
        createButton("Analyse", BUTTON_FONT, TEXT_COLOR, BUTTON_COLOR, 647, 410, 102, 72, e -> analyzeEquipment());
    }

    private void refreshTextFields() {
        equipIdTxt.setText(""); 
        equipNameTxt.setText(""); 
        unitIdTxt.setText(""); 
        locationIdTxt.setText("");
    }

    private void refreshTableData() {
        equipmentData.clear();
        String sql = "SELECT * FROM Equipment";
        try(Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()){
            while(rs.next()) {
                equipmentData.add(new EquipmentRecord(
                    rs.getInt("equipment_id"), 
                    rs.getString("name"), 
                    rs.getString("type"), 
                    rs.getInt("unit_id"), 
                    rs.getString("status"), 
                    rs.getInt("location_id")
                ));
            }
        } catch(SQLException e) {
            handleDatabaseError(e, "Error retrieving equipment data");
        }
    }
private void insertEquipment() {
        if(equipIdTxt.getText().isEmpty() || 
           equipNameTxt.getText().isEmpty() || 
           unitIdTxt.getText().isEmpty() || 
           locationIdTxt.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
            return;
        }
        
        // Validate numeric fields
        try {
            Integer.valueOf(equipIdTxt.getText());
            Integer.valueOf(unitIdTxt.getText());
            Integer.valueOf(locationIdTxt.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("ID fields must contain valid numbers");
            alert.showAndWait();
            return;
        }
        
        String sql="INSERT INTO Equipment(equipment_id,name,type,unit_id,status,location_id) VALUES(?,?,?,?,?,?)";
        try(Connection conn = DBUtil.getConnection(); PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, Integer.parseInt(equipIdTxt.getText()));
            p.setString(2, equipNameTxt.getText());
            p.setString(3, equipTypeTxt.getSelectionModel().getSelectedItem());
            p.setInt(4, Integer.parseInt(unitIdTxt.getText()));
            p.setString(5, statusTxt.getSelectionModel().getSelectedItem());
            p.setInt(6, Integer.parseInt(locationIdTxt.getText()));
            
            if(p.executeUpdate() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Equipment inserted successfully.");
                alert.showAndWait();
            }
            refreshTextFields();
            refreshTableData();
        } catch(NumberFormatException e){handleDatabaseError(e,"Invalid number format");}
          catch(SQLException e){handleDatabaseError(e,"Error inserting equipment");}
    }
private void updateEquipment() {
        if(equipIdTxt.getText().isEmpty() || 
           equipNameTxt.getText().isEmpty() || 
           unitIdTxt.getText().isEmpty() || 
           locationIdTxt.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
            return;
        }
        
        // Validate numeric fields
        try {
            Integer.valueOf(equipIdTxt.getText());
            Integer.valueOf(unitIdTxt.getText());
            Integer.valueOf(locationIdTxt.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("ID fields must contain valid numbers");
            alert.showAndWait();
            return;
        }
        
        String sql="UPDATE Equipment SET name=?,type=?,unit_id=?,status=?,location_id=? WHERE equipment_id=?";
        try(Connection conn = DBUtil.getConnection(); PreparedStatement p = conn.prepareStatement(sql)) {
            p.setString(1, equipNameTxt.getText());
            p.setString(2, equipTypeTxt.getSelectionModel().getSelectedItem());
            p.setInt(3, Integer.parseInt(unitIdTxt.getText()));
            p.setString(4, statusTxt.getSelectionModel().getSelectedItem());
            p.setInt(5, Integer.parseInt(locationIdTxt.getText()));
            p.setInt(6, Integer.parseInt(equipIdTxt.getText()));
            
            if(p.executeUpdate() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Equipment updated successfully.");
                alert.showAndWait();
            }
            refreshTextFields();
            refreshTableData();
        } catch(NumberFormatException e){handleDatabaseError(e,"Invalid number format");}
          catch(SQLException e){handleDatabaseError(e,"Error updating equipment");}
    }    private void deleteEquipment() {
        if(equipIdTxt.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter Equipment ID");
            alert.showAndWait();
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Equipment");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Confirm deletion?");
        
        if(confirmAlert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }
        
        String sql="DELETE FROM Equipment WHERE equipment_id=?";
        try(Connection conn = DBUtil.getConnection(); PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, Integer.parseInt(equipIdTxt.getText()));
            
            if(p.executeUpdate() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Equipment deleted successfully.");
                alert.showAndWait();
            }
            refreshTextFields();
            refreshTableData();
        } catch(NumberFormatException e){handleDatabaseError(e,"Invalid number format");}
          catch(SQLException e){handleDatabaseError(e,"Error deleting equipment");}
    }
    	private void analyzeEquipment() {
		try {
			// Show Equipment by Status chart
			com.warManagementGUI.DataAnalysis.EquipmentBarChart.showEquipmentStatusChart();
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Analysis Error");
			alert.setContentText("Error showing analysis: " + e.getMessage());
			alert.showAndWait();
		}
	}
	
	// private void handleDatabaseError(Exception e, String message) {
    //     System.err.println(message + ": " + e.getMessage());
    //     Alert alert = new Alert(Alert.AlertType.ERROR);
    //     alert.setTitle("Database Error");
    //     alert.setHeaderText(null);
    //     alert.setContentText(message + ": " + e.getMessage());
    //     alert.showAndWait();
    // }
    
    // Data model class for TableView
    public static class EquipmentRecord {
        private final Integer equipmentId;
        private final String name;
        private final String type;
        private final Integer unitId;
        private final String status;
        private final Integer locationId;
        
        public EquipmentRecord(Integer equipmentId, String name, String type, Integer unitId, String status, Integer locationId) {
            this.equipmentId = equipmentId;
            this.name = name;
            this.type = type;
            this.unitId = unitId;
            this.status = status;
            this.locationId = locationId;
        }
        
        public Integer getEquipmentId() { return equipmentId; }
        public String getName() { return name; }
        public String getType() { return type; }
        public Integer getUnitId() { return unitId; }
        public String getStatus() { return status; }
        public Integer getLocationId() { return locationId; }
    }
}
