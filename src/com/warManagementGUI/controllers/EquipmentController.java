package com.warManagementGUI.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
 * Controller for the Equipment management FXML interface
 * Handles CRUD operations for equipment data with modern UI
 */
public class EquipmentController extends BaseController implements Initializable {

    @FXML
    private TextField equipmentIdField;
    @FXML
    private TextField equipmentNameField;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private TextField unitIdField;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private TextField locationIdField;

    @FXML
    private TableView<EquipmentRecord> equipmentTable;
    @FXML
    private TableColumn<EquipmentRecord, Integer> equipmentIdCol;
    @FXML
    private TableColumn<EquipmentRecord, String> nameCol;
    @FXML
    private TableColumn<EquipmentRecord, String> typeCol;
    @FXML
    private TableColumn<EquipmentRecord, Integer> unitIdCol;
    @FXML
    private TableColumn<EquipmentRecord, String> statusCol;
    @FXML
    private TableColumn<EquipmentRecord, Integer> locationIdCol;

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

    private ObservableList<EquipmentRecord> equipmentData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize theme functionality
        initializeTheme();

        // Initialize combo boxes
        typeComboBox.setItems(FXCollections.observableArrayList("Weapon", "Vehicle", "Electronic", "Other"));
        typeComboBox.setValue("Weapon");

        statusComboBox.setItems(FXCollections.observableArrayList("Operational", "Maintenance", "Decommissioned"));
        statusComboBox.setValue("Operational");

        // Set up table columns
        setupTableColumns();

        // Load equipment data
        loadEquipmentData();

        // Set up table selection listener
        setupTableSelectionListener();
    }

    private void setupTableColumns() {
        equipmentIdCol.setCellValueFactory(new PropertyValueFactory<>("equipmentId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        unitIdCol.setCellValueFactory(new PropertyValueFactory<>("unitId"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        locationIdCol.setCellValueFactory(new PropertyValueFactory<>("locationId"));

        equipmentTable.setItems(equipmentData);
    }

    private void setupTableSelectionListener() {
        equipmentTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        populateFields(newValue);
                    }
                });
    }

    private void populateFields(EquipmentRecord equipment) {
        equipmentIdField.setText(String.valueOf(equipment.getEquipmentId()));
        equipmentNameField.setText(equipment.getName());
        typeComboBox.setValue(equipment.getType());
        unitIdField.setText(String.valueOf(equipment.getUnitId()));
        statusComboBox.setValue(equipment.getStatus());
        locationIdField.setText(String.valueOf(equipment.getLocationId()));
    }

    @FXML
    private void clearFields() {
        equipmentIdField.clear();
        equipmentNameField.clear();
        typeComboBox.setValue("Weapon");
        unitIdField.clear();
        statusComboBox.setValue("Operational");
        locationIdField.clear();
        equipmentTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void addEquipment() {
        if (!validateFields()) {
            return;
        }

        String sql = "INSERT INTO Equipment(equipment_id, name, type, unit_id, status, location_id) VALUES(?,?,?,?,?,?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(equipmentIdField.getText().trim()));
            pstmt.setString(2, equipmentNameField.getText().trim());
            pstmt.setString(3, typeComboBox.getValue());
            pstmt.setInt(4, Integer.parseInt(unitIdField.getText().trim()));
            pstmt.setString(5, statusComboBox.getValue());
            pstmt.setInt(6, Integer.parseInt(locationIdField.getText().trim()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                showSuccessAlert("Equipment added successfully!");
                clearFields();
                loadEquipmentData();
            } else {
                showErrorAlert("Failed to add equipment.");
            }

        } catch (SQLException e) {
            showErrorAlert("Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            showErrorAlert("Please enter valid numeric values for ID fields.");
        }
    }

    @FXML
    private void updateEquipment() {
        if (equipmentIdField.getText().trim().isEmpty()) {
            showWarningAlert("Please select equipment to update or enter an Equipment ID.");
            return;
        }

        if (!validateFields()) {
            return;
        }

        String sql = "UPDATE Equipment SET name=?, type=?, unit_id=?, status=?, location_id=? WHERE equipment_id=?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, equipmentNameField.getText().trim());
            pstmt.setString(2, typeComboBox.getValue());
            pstmt.setInt(3, Integer.parseInt(unitIdField.getText().trim()));
            pstmt.setString(4, statusComboBox.getValue());
            pstmt.setInt(5, Integer.parseInt(locationIdField.getText().trim()));
            pstmt.setInt(6, Integer.parseInt(equipmentIdField.getText().trim()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                showSuccessAlert("Equipment updated successfully!");
                clearFields();
                loadEquipmentData();
            } else {
                showErrorAlert("Failed to update equipment. Equipment ID not found.");
            }

        } catch (SQLException e) {
            showErrorAlert("Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            showErrorAlert("Please enter valid numeric values for ID fields.");
        }
    }

    @FXML
    private void deleteEquipment() {
        if (equipmentIdField.getText().trim().isEmpty()) {
            showWarningAlert("Please select equipment to delete or enter an Equipment ID.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Equipment");
        confirmAlert.setContentText("Are you sure you want to delete this equipment? This action cannot be undone.");

        if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        String sql = "DELETE FROM Equipment WHERE equipment_id=?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(equipmentIdField.getText().trim()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                showSuccessAlert("Equipment deleted successfully!");
                clearFields();
                loadEquipmentData();
            } else {
                showErrorAlert("Failed to delete equipment. Equipment ID not found.");
            }

        } catch (SQLException e) {
            showErrorAlert("Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            showErrorAlert("Please enter a valid numeric Equipment ID.");
        }
    }

    @FXML
    private void showAnalytics() {
        try {
            // Show Equipment analytics chart
            com.warManagementGUI.DataAnalysis.EquipmentBarChart.showEquipmentStatusChart();
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
            // Apply the current theme instead of always using light theme
            themeManager.applyThemeToScene(scene);
            stage.setScene(scene);
        } catch (IOException e) {
            showErrorAlert("Error loading dashboard: " + e.getMessage());
        }
    }

    @FXML
    private void refreshData() {
        loadEquipmentData();
        showSuccessAlert("Data refreshed successfully!");
    }

    @FXML
    private void exportData() {
        // This could be implemented to export data to CSV or other formats
        showInfoAlert("Export functionality will be available soon!");
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadEquipmentData() {
        equipmentData.clear();
        String sql = "SELECT * FROM Equipment ORDER BY equipment_id";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                EquipmentRecord equipment = new EquipmentRecord(
                        rs.getInt("equipment_id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getInt("unit_id"),
                        rs.getString("status"),
                        rs.getInt("location_id"));
                equipmentData.add(equipment);
            }

        } catch (SQLException e) {
            showErrorAlert("Error loading equipment data: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        if (equipmentIdField.getText().trim().isEmpty()) {
            errors.append("Equipment ID is required.\n");
        } else {
            try {
                Integer.valueOf(equipmentIdField.getText().trim());
            } catch (NumberFormatException e) {
                errors.append("Equipment ID must be a valid number.\n");
            }
        }

        if (equipmentNameField.getText().trim().isEmpty()) {
            errors.append("Equipment Name is required.\n");
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

    // Data model class for TableView
    public static class EquipmentRecord {
        private final Integer equipmentId;
        private final String name;
        private final String type;
        private final Integer unitId;
        private final String status;
        private final Integer locationId;

        public EquipmentRecord(Integer equipmentId, String name, String type, Integer unitId, String status,
                Integer locationId) {
            this.equipmentId = equipmentId;
            this.name = name;
            this.type = type;
            this.unitId = unitId;
            this.status = status;
            this.locationId = locationId;
        }

        public Integer getEquipmentId() {
            return equipmentId;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public Integer getUnitId() {
            return unitId;
        }

        public String getStatus() {
            return status;
        }

        public Integer getLocationId() {
            return locationId;
        }
    }
}
