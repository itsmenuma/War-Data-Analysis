package com.warManagementGUI.Supply;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.warManagementGUI.WarManagement;
import com.warManagementGUI.util.AbstractDetailsStage;
import com.warManagementGUI.util.DBUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Supply_details extends AbstractDetailsStage {

    private final TextField supply_ID_txt;
	private final TextField supply_name_txt;
	private final TextField supply_type_txt;
	private final TextField Quantity_txt;
	private final TextField Unit_ID_txt;
	private final TextField loc_ID_txt;
	private final TableView<SupplyRecord> Supply_table;
	private final ComboBox<String> Status_txt;

	public Supply_details() {
        super("Supplies", 773, 529);
         
        // Create title label
        createLabel("Supply Details", TITLE_FONT, 10, 11, 222, 51);
		createLabel("Supply ID", LABEL_FONT, 10, 72, 112, 44);
		createLabel("Supply Name", LABEL_FONT, 10, 130, 112, 13);
		createLabel("Supply Type", LABEL_FONT, 10, 164, 112, 44);

		supply_ID_txt = createTextField(152, 87, 96, 19, 10);
		supply_name_txt = createTextField(152, 129, 96, 19, 10);
		supply_type_txt = createTextField(152, 179, 96, 19, 10);
		Status_txt = createComboBox(new String[]{"Available", "In Use", "Out of Stock"}, 152, 364, 96, 21);

		createButton("Refresh", BUTTON_FONT, 183, 429, 82, 51, e -> refreshTextFields());
		createNavButton("Back to Dashboard", WarManagement.class, 10, 429, 202, 51);

		createButton("Delete", BUTTON_FONT, 486, 429, 101, 51, e -> deleteSupply());
		createButton("Insert", BUTTON_FONT, 275, 429, 82, 51, e -> insertSupply());
		createButton("Update", BUTTON_FONT, 367, 429, 96, 51, e -> updateSupply());

		createLabel("Quantity", LABEL_FONT, 10, 218, 82, 31);
		Quantity_txt = createTextField(152, 226, 96, 19, 10);

		createLabel("Unit ID", LABEL_FONT, 10, 259, 89, 33);
		createLabel("Location ID", LABEL_FONT, 10, 316, 110, 31);
		createLabel("Status", LABEL_FONT, 10, 357, 89, 31);

		Unit_ID_txt = createTextField(152, 268, 96, 19, 10);
		loc_ID_txt = createTextField(152, 324, 96, 19, 10);

		// Create table
		Supply_table = new TableView<>();
		Supply_table.setLayoutX(278);
		Supply_table.setLayoutY(23);
		Supply_table.setPrefWidth(471);
		Supply_table.setPrefHeight(379);
		Supply_table.setStyle("-fx-background-color: black; -fx-text-fill: white;");
		rootPane.getChildren().add(Supply_table);
		
		// Setup table columns
		TableColumn<SupplyRecord, String> supplyIdCol = new TableColumn<>("Supply_ID");
		supplyIdCol.setCellValueFactory(new PropertyValueFactory<>("supplyId"));
		supplyIdCol.setPrefWidth(80);
		
		TableColumn<SupplyRecord, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameCol.setPrefWidth(80);
		
		TableColumn<SupplyRecord, String> typeCol = new TableColumn<>("Type");
 		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		typeCol.setPrefWidth(80);
		
		TableColumn<SupplyRecord, String> quantityCol = new TableColumn<>("Quantity");
		quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		quantityCol.setPrefWidth(80);
		
		TableColumn<SupplyRecord, String> unitIdCol = new TableColumn<>("Unit_ID");
		unitIdCol.setCellValueFactory(new PropertyValueFactory<>("unitId"));
		unitIdCol.setPrefWidth(70);
		
		TableColumn<SupplyRecord, String> locationIdCol = new TableColumn<>("Location_ID");
		locationIdCol.setCellValueFactory(new PropertyValueFactory<>("locationId"));
		locationIdCol.setPrefWidth(90);
		
		TableColumn<SupplyRecord, String> statusCol = new TableColumn<>("Status");
		statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
		statusCol.setPrefWidth(80);
		
		Supply_table.getColumns().addAll(supplyIdCol, nameCol, typeCol, quantityCol, unitIdCol, locationIdCol, statusCol);

		createButton("Analyse", Font.font("Times New Roman", FontWeight.BOLD, 15), 605, 428, 132, 53, e -> analyseSupply());
		loadSupplyData();
	}
	
	protected void updateSupply() {
		// Check if supply_ID_txt is empty
	    if (supply_ID_txt.getText().isEmpty()) {
	        showErrorDialog("Please enter a Supply ID to update.");
	        return;
	    }

	    String updateQuery = "UPDATE Supplies SET name = ?, type = ?, quantity = ?, unit_id = ?, location_id = ?, status = ? WHERE supply_id = ?";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

 			pstmt.setString(1, supply_name_txt.getText());    // supply "name"
 			pstmt.setString(2, supply_type_txt.getText());   // supply "type"
	        pstmt.setInt(3, Integer.parseInt(Quantity_txt.getText()));
	        pstmt.setInt(4, Integer.parseInt(Unit_ID_txt.getText()));
	        pstmt.setInt(5, Integer.parseInt(loc_ID_txt.getText()));
	        // Convert selected item to String explicitly
	        String status = Status_txt.getValue();
	        pstmt.setString(6, status);

	        pstmt.setInt(7, Integer.parseInt(supply_ID_txt.getText()));

	        int rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected > 0) {
	            showInformationDialog("Supply updated successfully.");
	            refreshTextFields();
	            loadSupplyData(); // Refresh table after update
	        } else {
	            showErrorDialog("Failed to update supply. Supply ID not found.");
	        }

	    } catch (SQLException ex) {
	        handleDatabaseError(ex, "Failed to update supply details.");
	    } catch (NumberFormatException ex) {
	        handleDatabaseError(ex, "Please enter valid numeric values for ID, Quantity, Unit ID, and Location ID.");
	    }
	}
	
	protected void deleteSupply() {
		 // Check if supply_ID_txt is empty
	    if (supply_ID_txt.getText().isEmpty()) {
	        showErrorDialog("Please enter a Supply ID to delete.");
	        return;
	    }

	    String deleteQuery = "DELETE FROM Supplies WHERE supply_id = ?";

	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

	        pstmt.setInt(1, Integer.parseInt(supply_ID_txt.getText()));

	        int rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected > 0) {
	            showInformationDialog("Supply deleted successfully.");
	            refreshTextFields();
	            loadSupplyData(); // Refresh table after deletion
	        } else {
	            showErrorDialog("Failed to delete supply. Supply ID not found.");
	        }

	    } catch (NumberFormatException ex) {
	        showErrorDialog("Please enter a valid numeric Supply ID.");
	    } catch (SQLException ex) {
	        System.err.println("Database error: " + ex.getMessage());
			showErrorDialog("Database error: " + ex.getMessage());
	    }
	}

	protected void insertSupply() {
		 String insertQuery = "INSERT INTO Supplies (supply_id, name, type, quantity, unit_id, location_id, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

		    try (Connection conn = DBUtil.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

		        pstmt.setInt(1, Integer.parseInt(supply_ID_txt.getText()));
		        pstmt.setString(2, supply_name_txt.getText());
		        pstmt.setString(3, supply_type_txt.getText());
		        pstmt.setInt(4, Integer.parseInt(Quantity_txt.getText()));
		        pstmt.setInt(5, Integer.parseInt(Unit_ID_txt.getText()));
		        pstmt.setInt(6, Integer.parseInt(loc_ID_txt.getText()));
	        // Convert selected item to String explicitly
	        String status = Status_txt.getValue();
	        pstmt.setString(7, status);

		        int rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected > 0) {
	            showInformationDialog("Supply inserted successfully.");
	            refreshTextFields();
	            loadSupplyData(); // Refresh table after insertion
	        } else {
	            showErrorDialog("Failed to insert supply.");
	        }

	    } catch (SQLException ex) {
	        System.err.println("Database error: " + ex.getMessage());
                showErrorDialog("Database error: " + ex.getMessage());
	    } catch (NumberFormatException ex) {
	        showErrorDialog("Please enter valid numeric values for ID, Quantity, Unit ID, and Location ID.");
	    }
	}
	
	private void loadSupplyData() {
		 String query = "SELECT supply_id, name, type, quantity, unit_id, location_id, status FROM Supplies";
	        
	        try (Connection conn = DBUtil.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(query);
	             ResultSet rs = pstmt.executeQuery()) {

	            // Clear existing table data
	            ObservableList<SupplyRecord> data = FXCollections.observableArrayList();

	            // Iterate through the result set and populate the table
	            while (rs.next()) {
	                String supply_id = rs.getString("supply_id");
	                String name = rs.getString("name");
	                String type = rs.getString("type");
	                String quantity = rs.getString("quantity");
	                String unit_id = rs.getString("unit_id");
	                String location_id = rs.getString("location_id");
	                String status = rs.getString("status");

	                // Create SupplyRecord and add to data
	                SupplyRecord record = new SupplyRecord(supply_id, name, type, quantity, unit_id, location_id, status);
	                data.add(record);
	            }
	            
	            Supply_table.setItems(data);

	        } catch (SQLException ex) {
	            System.err.println("Database error: " + ex.getMessage());
                showErrorDialog("Database error: " + ex.getMessage());
	        }
	}

	protected void refreshTextFields() {
		supply_ID_txt.setText("");
		supply_name_txt.setText("");
		supply_type_txt.setText("");
		Quantity_txt.setText("");
		Unit_ID_txt.setText("");
		loc_ID_txt.setText("");
	}
	
	private void analyseSupply() {
		try {
			// Show Supply by Status chart
			com.warManagementGUI.DataAnalysis.SuppliesBarChart.showSupplyStatusChart();
		} catch (Exception e) {
			showErrorDialog("Error showing analysis: " + e.getMessage());
		}
	}
}
