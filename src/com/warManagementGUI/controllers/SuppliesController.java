package com.warManagementGUI.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.warManagementGUI.Supply.SupplyRecord;
import com.warManagementGUI.util.DBUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Controller for the Supplies management FXML interface
 * Handles CRUD operations for supply data with modern UI
 */
public class SuppliesController extends BaseController implements Initializable {

    @FXML
    private TextField supplyIdField;
    @FXML
    private TextField supplyNameField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField unitIdField;
    @FXML
    private TextField locationIdField;
    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private TableView<SupplyRecord> suppliesTable;
    @FXML
    private TableColumn<SupplyRecord, String> supplyIdCol;
    @FXML
    private TableColumn<SupplyRecord, String> nameCol;
    @FXML
    private TableColumn<SupplyRecord, String> typeCol;
    @FXML
    private TableColumn<SupplyRecord, String> quantityCol;
    @FXML
    private TableColumn<SupplyRecord, String> unitIdCol;
    @FXML
    private TableColumn<SupplyRecord, String> locationIdCol;
    @FXML
    private TableColumn<SupplyRecord, String> statusCol;

    @FXML
    private Button clearBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button analyticsBtn;
    @FXML
    private Button backBtn;

    private ObservableList<SupplyRecord> supplyData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeTheme();

        statusComboBox.setItems(FXCollections.observableArrayList("Available", "In Use", "Out of Stock"));
        statusComboBox.setValue("Available");

        setupTableColumns();

        loadSupplyData();

        setupTableSelectionListener();
    }

    private void setupTableColumns() {
        supplyIdCol.setCellValueFactory(new PropertyValueFactory<>("supplyId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitIdCol.setCellValueFactory(new PropertyValueFactory<>("unitId"));
        locationIdCol.setCellValueFactory(new PropertyValueFactory<>("locationId"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        suppliesTable.setItems(supplyData);
    }

    private void setupTableSelectionListener() {
        suppliesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        populateFields(newValue);
                    }
                });
    }

    private void populateFields(SupplyRecord supply) {
        supplyIdField.setText(supply.getSupplyId());
        supplyNameField.setText(supply.getName());
        typeField.setText(supply.getType());
        quantityField.setText(supply.getQuantity());
        unitIdField.setText(supply.getUnitId());
        locationIdField.setText(supply.getLocationId());
        statusComboBox.setValue(supply.getStatus());
    }

    @FXML
    private void clearFields() {
        supplyIdField.clear();
        supplyNameField.clear();
        typeField.clear();
        quantityField.clear();
        unitIdField.clear();
        locationIdField.clear();
        statusComboBox.setValue("Available");
        suppliesTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void addSupply() {
        if (!validateFields()) {
            return;
        }

        String sql = "INSERT INTO Supplies (supply_id, name, type, quantity, unit_id, location_id, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(supplyIdField.getText().trim()));
            pstmt.setString(2, supplyNameField.getText().trim());
            pstmt.setString(3, typeField.getText().trim());
            pstmt.setInt(4, Integer.parseInt(quantityField.getText().trim()));
            pstmt.setInt(5, Integer.parseInt(unitIdField.getText().trim()));
            pstmt.setInt(6, Integer.parseInt(locationIdField.getText().trim()));
            pstmt.setString(7, statusComboBox.getValue());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                showSuccessAlert("Supply added successfully!");
                clearFields();
                loadSupplyData();
            } else {
                showErrorAlert("Failed to add supply.");
            }

        } catch (SQLException e) {
            showErrorAlert("Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            showErrorAlert("Please enter valid numeric values for ID and Quantity fields.");
        }
    }

    @FXML
    private void updateSupply() {
        if (supplyIdField.getText().trim().isEmpty()) {
            showWarningAlert("Please select a supply to update or enter a Supply ID.");
            return;
        }

        if (!validateFields()) {
            return;
        }

        String sql = "UPDATE Supplies SET name = ?, type = ?, quantity = ?, unit_id = ?, location_id = ?, status = ? WHERE supply_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, supplyNameField.getText().trim());
            pstmt.setString(2, typeField.getText().trim());
            pstmt.setInt(3, Integer.parseInt(quantityField.getText().trim()));
            pstmt.setInt(4, Integer.parseInt(unitIdField.getText().trim()));
            pstmt.setInt(5, Integer.parseInt(locationIdField.getText().trim()));
            pstmt.setString(6, statusComboBox.getValue());
            pstmt.setInt(7, Integer.parseInt(supplyIdField.getText().trim()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                showSuccessAlert("Supply updated successfully!");
                clearFields();
                loadSupplyData();
            } else {
                showErrorAlert("Failed to update supply. Supply ID not found.");
            }

        } catch (SQLException e) {
            showErrorAlert("Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            showErrorAlert("Please enter valid numeric values for ID and Quantity fields.");
        }
    }

    @FXML
    private void deleteSupply() {
        if (supplyIdField.getText().trim().isEmpty()) {
            showWarningAlert("Please select a supply to delete or enter a Supply ID.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Supply");
        confirmAlert.setContentText("Are you sure you want to delete this supply? This action cannot be undone.");

        if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        String sql = "DELETE FROM Supplies WHERE supply_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.valueOf(supplyIdField.getText().trim()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                showSuccessAlert("Supply deleted successfully!");
                clearFields();
                loadSupplyData();
            } else {
                showErrorAlert("Failed to delete supply. Supply ID not found.");
            }

        } catch (SQLException e) {
            showErrorAlert("Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            showErrorAlert("Please enter a valid numeric Supply ID.");
        }
    }

    @FXML
    private void showAnalytics() {
        try {

            com.warManagementGUI.DataAnalysis.SuppliesBarChart.showSupplyStatusChart();
        } catch (Exception e) {
            showErrorAlert("Error showing analytics: " + e.getMessage());
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/warManagementGUI/fxml/Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backBtn.getScene().getWindow();
            Scene scene = new Scene(root);

            themeManager.applyThemeToScene(scene);
            stage.setScene(scene);

        } catch (IOException e) {
            showErrorAlert("Error loading dashboard: " + e.getMessage());
        }
    }

    private void loadSupplyData() {
        supplyData.clear();
        String sql = "SELECT supply_id, name, type, quantity, unit_id, location_id, status FROM Supplies ORDER BY supply_id";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                SupplyRecord supply = new SupplyRecord(
                        rs.getString("supply_id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("quantity"),
                        rs.getString("unit_id"),
                        rs.getString("location_id"),
                        rs.getString("status"));
                supplyData.add(supply);
            }

        } catch (SQLException e) {
            showErrorAlert("Error loading supply data: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        if (supplyIdField.getText().trim().isEmpty()) {
            errors.append("Supply ID is required.\n");
        } else {
            try {
                Integer.valueOf(supplyIdField.getText().trim());
            } catch (NumberFormatException e) {
                errors.append("Supply ID must be a valid number.\n");
            }
        }

        if (supplyNameField.getText().trim().isEmpty()) {
            errors.append("Supply Name is required.\n");
        }

        if (typeField.getText().trim().isEmpty()) {
            errors.append("Type is required.\n");
        }

        if (quantityField.getText().trim().isEmpty()) {
            errors.append("Quantity is required.\n");
        } else {
            try {
                int qty = Integer.parseInt(quantityField.getText().trim());
                if (qty < 0) {
                    errors.append("Quantity must be a positive number.\n");
                }
            } catch (NumberFormatException e) {
                errors.append("Quantity must be a valid number.\n");
            }
        }

        if (unitIdField.getText().trim().isEmpty()) {
            errors.append("Unit ID is required.\n");
        } else {
            try {
                Integer.valueOf(unitIdField.getText().trim());
            } catch (NumberFormatException e) {
                errors.append("Unit ID must be a valid number.\n");
            }
        }

        if (locationIdField.getText().trim().isEmpty()) {
            errors.append("Location ID is required.\n");
        } else {
            try {
                Integer.valueOf(locationIdField.getText().trim());
            } catch (NumberFormatException e) {
                errors.append("Location ID must be a valid number.\n");
            }
        }

        if (errors.length() > 0) {
            showWarningAlert("Please correct the following errors:\n\n" + errors.toString());
            return false;
        }

        return true;
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operation Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarningAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Input Validation");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void refreshData() {
        loadSupplyData();
        showSuccessAlert("Data refreshed successfully!");
    }

    @FXML
    private void exportData() {
        showInfoAlert("Export functionality will be available soon!");
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
