package com.warManagementGUI.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Set;

import com.warManagementGUI.components.FilterControls;
import com.warManagementGUI.models.Permission;
import com.warManagementGUI.records.EquipmentRecord;
import com.warManagementGUI.util.DBUtil;
import com.warManagementGUI.util.TableFilterUtil;

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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller for the Equipment management FXML interface
 * Handles CRUD operations for equipment data with modern UI
 */
public class EquipmentController extends BaseController implements Initializable {

    public BorderPane mainBorderPane;
    public Button refreshBtn;
    public Button exportBtn;
    // User info fields
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label userRoleLabel;
    @FXML
    private Button logoutBtn;
    @FXML
    private VBox mainContainer; // Container for filter controls

    // Filter components
    private FilterControls filterControls;
    private TableFilterUtil<EquipmentRecord> filterUtil;

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
    @SuppressWarnings("unused")
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

    private final ObservableList<EquipmentRecord> equipmentData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeTheme();

        typeComboBox.setItems(FXCollections.observableArrayList("Weapon", "Vehicle", "Electronic", "Other"));
        typeComboBox.setValue("Weapon");

        statusComboBox.setItems(FXCollections.observableArrayList("Operational", "Maintenance", "Decommissioned"));
        statusComboBox.setValue("Operational");

        setupTableColumns();
        setupFilterControls();
        setupFilterListeners();

        loadEquipmentData();

        setupTableSelectionListener();

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
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load login screen: " + e.getMessage());
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

    @Override
    protected void configureUIBasedOnPermissions() {
        configureButtonVisibility(addBtn, Permission.WRITE_DATA);
        configureButtonVisibility(updateBtn, Permission.WRITE_DATA);
        configureButtonVisibility(deleteBtn, Permission.DELETE_DATA);
        configureButtonVisibility(analyticsBtn, Permission.ANALYZE_DATA);
    }

    private void setupTableColumns() {
        equipmentIdCol.setCellValueFactory(new PropertyValueFactory<>("equipmentId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        unitIdCol.setCellValueFactory(new PropertyValueFactory<>("unitId"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        locationIdCol.setCellValueFactory(new PropertyValueFactory<>("locationId"));

        // Initialize filter utility
        filterUtil = new TableFilterUtil<>(equipmentData);
        equipmentTable.setItems(filterUtil.getFilteredData());
    }

    private void setupFilterControls() {
        // Create filter controls with custom labels
        filterControls = new FilterControls("Type", "Search equipment by name, ID, or unit...");

        // Setup filter options
        setupTypeFilter();
        setupStatusFilterOptions();
        setupLocationFilterOptions();

        // Insert filter controls at the top of the main container
        if (mainContainer != null) {
            // Insert filter controls after the header (index 1), not at the beginning
            mainContainer.getChildren().add(1, filterControls);
        }
    }

    private void setupTypeFilter() {
        Set<String> types = TableFilterUtil.extractUniqueValues(equipmentData,
                equipment -> equipment.getType());
        TableFilterUtil.setupComboBoxFilter(filterControls.getPrimaryFilter(), types);
    }

    private void setupStatusFilterOptions() {
        Set<String> statuses = TableFilterUtil.extractUniqueValues(equipmentData,
                equipment -> equipment.getStatus());
        TableFilterUtil.setupComboBoxFilter(filterControls.getStatusFilter(), statuses);
    }

    private void setupLocationFilterOptions() {
        Set<String> locations = TableFilterUtil.extractUniqueValues(equipmentData,
                equipment -> String.valueOf(equipment.getLocationId()));
        TableFilterUtil.setupComboBoxFilter(filterControls.getLocationFilter(), locations);
    }

    private void setupFilterListeners() {
        // Keyword search
        filterControls.getSearchField().textProperty().addListener((obs, oldVal, newVal) -> {
            filterUtil.setKeywordFilter(equipment -> TableFilterUtil.matchesKeyword(newVal,
                    String.valueOf(equipment.getEquipmentId()),
                    equipment.getName(),
                    equipment.getType(),
                    String.valueOf(equipment.getUnitId())));
            updateResultsLabel();
        });

        // Type filter
        filterControls.getPrimaryFilter().valueProperty().addListener((obs, oldVal, newVal) -> {
            filterUtil.setCategoryFilter(equipment -> TableFilterUtil.matchesFilter(newVal, equipment.getType()));
            updateResultsLabel();
        });

        // Status filter
        filterControls.getStatusFilter().valueProperty().addListener((obs, oldVal, newVal) -> {
            filterUtil.setStatusFilter(equipment -> TableFilterUtil.matchesFilter(newVal, equipment.getStatus()));
            updateResultsLabel();
        });

        // Location filter
        filterControls.getLocationFilter().valueProperty().addListener((obs, oldVal, newVal) -> {
            filterUtil.setLocationFilter(
                    equipment -> TableFilterUtil.matchesFilter(newVal, String.valueOf(equipment.getLocationId())));
            updateResultsLabel();
        });

        // Clear filters button
        filterControls.getClearFiltersButton().setOnAction(e -> {
            filterControls.getSearchField().clear();
            filterControls.getPrimaryFilter().setValue("All");
            filterControls.getStatusFilter().setValue("All");
            filterControls.getLocationFilter().setValue("All");
            filterUtil.clearAllFilters();
            updateResultsLabel();
        });
    }

    private void updateResultsLabel() {
        if (filterControls != null) {
            filterControls.updateResultsLabel(filterUtil.getFilteredCount(), filterUtil.getTotalCount());
        }
    }

    private void refreshFilterOptions() {
        setupTypeFilter();
        setupStatusFilterOptions();
        setupLocationFilterOptions();
        updateResultsLabel();
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
    @SuppressWarnings("unused")
    private void addEquipment() {
        if (!requirePermission(Permission.WRITE_DATA)) {
            return;
        }

        if (validateFields()) {
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
    @SuppressWarnings("unused")
    private void updateEquipment() {
        if (!requirePermission(Permission.WRITE_DATA)) {
            return;
        }

        if (equipmentIdField.getText().trim().isEmpty()) {
            showWarningAlert("Please select equipment to update or enter an Equipment ID.");
            return;
        }

        if (validateFields()) {
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
    @SuppressWarnings("unused")
    private void deleteEquipment() {
        if (!requirePermission(Permission.DELETE_DATA)) {
            return;
        }

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
    @SuppressWarnings("unused")
    private void showAnalytics() {
        if (!requirePermission(Permission.ANALYZE_DATA)) {
            return;
        }

        try {

            com.warManagementGUI.DataAnalysis.EquipmentBarChart.showEquipmentStatusChart();
        } catch (Exception e) {
            showErrorAlert("Error showing analytics: " + e.getMessage());
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void goBack() {
        try {
            navigateToScene("/com/warManagementGUI/fxml/Dashboard.fxml", "War Management System - Dashboard", backBtn);
        } catch (IOException e) {
            showErrorAlert("Error loading dashboard: " + e.getMessage());
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void refreshData() {
        loadEquipmentData();
        showSuccessAlert("Data refreshed successfully!");
    }

    @FXML
    @SuppressWarnings("unused")
    private void exportData() {
        if (!requirePermission(Permission.EXPORT_DATA)) {
            return;
        }

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
        if (!requirePermission(Permission.READ_DATA)) {
            return;
        }

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

            // Refresh filter options after loading data
            if (filterControls != null) {
                refreshFilterOptions();
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

        if (!errors.isEmpty()) {
            showWarningAlert("Please correct the following errors:\n\n" + errors);
            return true;
        }

        return false;
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
}
