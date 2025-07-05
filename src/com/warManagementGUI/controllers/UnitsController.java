package com.warManagementGUI.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.warManagementGUI.models.Permission;
import com.warManagementGUI.records.UnitRecord;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Controller for Units Management interface
 */
public class UnitsController extends BaseController implements Initializable {

    @FXML
    private Label welcomeLabel;
    @FXML
    private Label userRoleLabel;
    @FXML
    private Button logoutBtn;

    @FXML
    private TextField unitIdField;
    @FXML
    private TextField unitNameField;
    @FXML
    private ComboBox<String> unitTypeComboBox;
    @FXML
    private TextField commanderIdField;
    @FXML
    private TextField locationIdField;

    @FXML
    private TableView<UnitRecord> unitsTable;
    @FXML
    private TableColumn<UnitRecord, String> unitIdColumn;
    @FXML
    private TableColumn<UnitRecord, String> unitNameColumn;
    @FXML
    private TableColumn<UnitRecord, String> unitTypeColumn;
    @FXML
    private TableColumn<UnitRecord, String> commanderColumn;
    @FXML
    private TableColumn<UnitRecord, String> locationColumn;

    @FXML
    private Button addBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    @SuppressWarnings("unused")
    private Button clearBtn;
    @FXML
    private Button analyticsBtn;
    @FXML
    private Button backBtn;
    @FXML
    @SuppressWarnings("unused")
    private Button refreshBtn;
    @FXML
    private Button exportBtn;

    private final ObservableList<UnitRecord> unitsData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeTheme();

        setupUnitTypeComboBox();
        setupTableColumns();
        setupTableSelection();
        loadUnitsData();
        configureUIBasedOnPermissions();
        updateUserInfo();
    }

    /**
     * Handle logout button click
     */
    @FXML
    @SuppressWarnings("unused")
    private void handleLogout() {
        authService.logout();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/warManagementGUI/fxml/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Failed to load login screen: " + e.getMessage());
        }
    }

    /**
     * Update user information display
     */
    private void updateUserInfo() {
        if (authService.getCurrentUser() != null) {
            welcomeLabel.setText("Welcome, " + authService.getCurrentUser().getUsername());
            userRoleLabel.setText("Role: " + authService.getCurrentUser().getRole().toString());
        }
    }

    private void setupUnitTypeComboBox() {
        unitTypeComboBox.getItems().addAll("infantry", "cavalry", "artillery", "logistics", "reconnaissance",
                "special forces");
        unitTypeComboBox.setValue("infantry");
    }

    private void setupTableColumns() {
        unitIdColumn.setCellValueFactory(new PropertyValueFactory<>("unitId"));
        unitNameColumn.setCellValueFactory(new PropertyValueFactory<>("unitName"));
        unitTypeColumn.setCellValueFactory(new PropertyValueFactory<>("unitType"));
        commanderColumn.setCellValueFactory(new PropertyValueFactory<>("commanderId"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("locationId"));

        unitsTable.setItems(unitsData);
    }

    @Override
    protected void configureUIBasedOnPermissions() {
        configureButtonVisibility(addBtn, Permission.WRITE_DATA);
        configureButtonVisibility(updateBtn, Permission.WRITE_DATA);
        configureButtonVisibility(deleteBtn, Permission.DELETE_DATA);
        configureButtonVisibility(exportBtn, Permission.EXPORT_DATA);
        configureButtonVisibility(analyticsBtn, Permission.ANALYZE_DATA);
    }

    private void setupTableSelection() {
        unitsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });
    }

    private void populateFields(UnitRecord record) {
        unitIdField.setText(record.getUnitId());
        unitNameField.setText(record.getUnitName());
        unitTypeComboBox.setValue(record.getUnitType());
        commanderIdField.setText(record.getCommanderId());
        locationIdField.setText(record.getLocationId());
    }

    @FXML
    @SuppressWarnings("unused")
    private void addUnit() {
        if (!requirePermission(Permission.WRITE_DATA)) {
            return;
        }

        if (validateFields()) {
            try (Connection conn = DBUtil.getConnection()) {
                String sql = "INSERT INTO units (unit_id, unit_name, unit_type, commander_id, location_id) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setString(1, unitIdField.getText());
                stmt.setString(2, unitNameField.getText());
                stmt.setString(3, unitTypeComboBox.getValue());
                stmt.setString(4, commanderIdField.getText());
                stmt.setString(5, locationIdField.getText());

                int result = stmt.executeUpdate();
                if (result > 0) {
                    showSuccess("Unit added successfully!");
                    clearFields();
                    loadUnitsData();
                }
            } catch (SQLException e) {
                showError("Error adding unit: " + e.getMessage());
            }
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void updateUnit() {
        if (!requirePermission(Permission.WRITE_DATA)) {
            return;
        }

        UnitRecord selected = unitsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a unit record to update.");
            return;
        }

        if (validateFields()) {
            try (Connection conn = DBUtil.getConnection()) {
                String sql = "UPDATE units SET unit_name=?, unit_type=?, commander_id=?, location_id=? WHERE unit_id=?";
                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setString(1, unitNameField.getText());
                stmt.setString(2, unitTypeComboBox.getValue());
                stmt.setString(3, commanderIdField.getText());
                stmt.setString(4, locationIdField.getText());
                stmt.setString(5, unitIdField.getText());

                int result = stmt.executeUpdate();
                if (result > 0) {
                    showSuccess("Unit updated successfully!");
                    loadUnitsData();
                }
            } catch (SQLException e) {
                showError("Error updating unit: " + e.getMessage());
            }
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void deleteUnit() {
        if (!requirePermission(Permission.DELETE_DATA)) {
            return;
        }

        UnitRecord selected = unitsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a unit record to delete.");
            return;
        }

        if (showConfirmation("Are you sure you want to delete this unit record?")) {
            try (Connection conn = DBUtil.getConnection()) {
                String sql = "DELETE FROM units WHERE unit_id=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, selected.getUnitId());

                int result = stmt.executeUpdate();
                if (result > 0) {
                    showSuccess("Unit deleted successfully!");
                    clearFields();
                    loadUnitsData();
                }
            } catch (SQLException e) {
                showError("Error deleting unit: " + e.getMessage());
            }
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void clearFields() {
        unitIdField.clear();
        unitNameField.clear();
        unitTypeComboBox.setValue("infantry");
        commanderIdField.clear();
        locationIdField.clear();
        unitsTable.getSelectionModel().clearSelection();
    }

    @FXML
    @SuppressWarnings("unused")
    private void showAnalytics() {
        if (!requirePermission(Permission.ANALYZE_DATA)) {
            return;
        }

        try {

            com.warManagementGUI.DataAnalysis.UnitsBarChart.showUnitTypeChart();
            showSuccess("Unit analytics displayed!");
        } catch (Exception e) {
            showError("Error showing analytics: " + e.getMessage());
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void refreshData() {
        loadUnitsData();
        showSuccess("Data refreshed successfully!");
    }

    @FXML
    @SuppressWarnings("unused")
    private void exportData() {
        if (!requirePermission(Permission.EXPORT_DATA)) {
            return;
        }

        showInfo("Export functionality will be available soon!");
    }

    @FXML
    @SuppressWarnings("unused")
    private void goBack() {
        try {
            navigateToScene("/com/warManagementGUI/fxml/Dashboard.fxml", "War Management System - Dashboard", backBtn);
        } catch (IOException e) {
            showError("Error navigating back: " + e.getMessage());
        }
    }

    private void loadUnitsData() {
        if (!requirePermission(Permission.READ_DATA)) {
            return;
        }

        unitsData.clear();
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM units";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                unitsData.add(new UnitRecord(
                        rs.getString("unit_id"),
                        rs.getString("unit_name"),
                        rs.getString("unit_type"),
                        rs.getString("commander_id"),
                        rs.getString("location_id")));
            }
        } catch (SQLException e) {
            showError("Error loading units data: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (unitIdField.getText().trim().isEmpty()) {
            showError("Unit ID is required!");
            return false;
        }
        if (unitNameField.getText().trim().isEmpty()) {
            showError("Unit Name is required!");
            return false;
        }
        if (unitTypeComboBox.getValue() == null) {
            showError("Unit Type is required!");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().orElse(null) == javafx.scene.control.ButtonType.OK;
    }
}
