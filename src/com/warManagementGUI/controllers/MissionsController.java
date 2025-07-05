package com.warManagementGUI.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.Set;

import com.warManagementGUI.components.FilterControls;
import com.warManagementGUI.models.Permission;
import com.warManagementGUI.records.MissionRecord;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller for the Missions management FXML interface
 * Handles CRUD operations for mission data with modern UI
 */
public class MissionsController extends BaseController implements Initializable {

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
    private TableFilterUtil<MissionRecord> filterUtil;

    @FXML
    private TextField missionIdField;
    @FXML
    private TextField missionNameField;
    @FXML
    private TextArea objectiveField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private TextField locationIdField;

    @FXML
    private TableView<MissionRecord> missionsTable;
    @FXML
    private TableColumn<MissionRecord, String> missionIdCol;
    @FXML
    private TableColumn<MissionRecord, String> nameCol;
    @FXML
    private TableColumn<MissionRecord, String> objectiveCol;
    @FXML
    private TableColumn<MissionRecord, String> startDateCol;
    @FXML
    private TableColumn<MissionRecord, String> endDateCol;
    @FXML
    private TableColumn<MissionRecord, String> statusCol;
    @FXML
    private TableColumn<MissionRecord, String> locationIdCol;
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

    private final ObservableList<MissionRecord> missionData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeTheme();

        statusComboBox.setItems(FXCollections.observableArrayList("planned", "ongoing", "completed"));
        statusComboBox.setValue("planned");

        setupTableColumns();
        setupFilterControls();
        setupFilterListeners();

        loadMissionData();

        setupTableSelectionListener();

        configureUIBasedOnPermissions();
        updateUserInfo();
    }

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
        missionIdCol.setCellValueFactory(new PropertyValueFactory<>("missionId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        objectiveCol.setCellValueFactory(new PropertyValueFactory<>("objective"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        locationIdCol.setCellValueFactory(new PropertyValueFactory<>("locationId"));

        // Initialize filter utility
        filterUtil = new TableFilterUtil<>(missionData);
        missionsTable.setItems(filterUtil.getFilteredData());
    }

    private void setupFilterControls() {
        // Create filter controls with custom labels
        filterControls = new FilterControls("Status", "Search missions by name, ID, or objective...");

        // Setup filter options
        setupStatusFilterOptions();
        setupLocationFilterOptions();

        // Insert filter controls at the top of the main container
        if (mainContainer != null) {
            // Insert filter controls after the header (index 1), not at the beginning
            mainContainer.getChildren().add(1, filterControls);
        }
    }

    private void setupStatusFilterOptions() {
        Set<String> statuses = TableFilterUtil.extractUniqueValues(missionData,
                mission -> mission.getStatus());
        TableFilterUtil.setupComboBoxFilter(filterControls.getPrimaryFilter(), statuses);
        TableFilterUtil.setupComboBoxFilter(filterControls.getStatusFilter(), statuses);
    }

    private void setupLocationFilterOptions() {
        Set<String> locations = TableFilterUtil.extractUniqueValues(missionData,
                mission -> mission.getLocationId());
        TableFilterUtil.setupComboBoxFilter(filterControls.getLocationFilter(), locations);
    }

    private void setupFilterListeners() {
        // Keyword search
        filterControls.getSearchField().textProperty().addListener((obs, oldVal, newVal) -> {
            filterUtil.setKeywordFilter(mission -> TableFilterUtil.matchesKeyword(newVal,
                    mission.getMissionId(),
                    mission.getName(),
                    mission.getObjective(),
                    mission.getLocationId()));
            updateResultsLabel();
        });

        // Status filter
        filterControls.getPrimaryFilter().valueProperty().addListener((obs, oldVal, newVal) -> {
            filterUtil.setStatusFilter(mission -> TableFilterUtil.matchesFilter(newVal, mission.getStatus()));
            updateResultsLabel();
        });

        // Location filter
        filterControls.getLocationFilter().valueProperty().addListener((obs, oldVal, newVal) -> {
            filterUtil.setLocationFilter(mission -> TableFilterUtil.matchesFilter(newVal, mission.getLocationId()));
            updateResultsLabel();
        });

        // Date range filter
        filterControls.getStartDatePicker().valueProperty().addListener((obs, oldVal, newVal) -> {
            updateDateFilter();
        });

        filterControls.getEndDatePicker().valueProperty().addListener((obs, oldVal, newVal) -> {
            updateDateFilter();
        });

        // Clear filters button
        filterControls.getClearFiltersButton().setOnAction(e -> {
            filterControls.getSearchField().clear();
            filterControls.getPrimaryFilter().setValue("All");
            filterControls.getStatusFilter().setValue("All");
            filterControls.getLocationFilter().setValue("All");
            filterControls.getStartDatePicker().setValue(null);
            filterControls.getEndDatePicker().setValue(null);
            filterUtil.clearAllFilters();
            updateResultsLabel();
        });
    }

    private void updateDateFilter() {
        LocalDate startDate = filterControls.getStartDatePicker().getValue();
        LocalDate endDate = filterControls.getEndDatePicker().getValue();

        filterUtil.setDateFilter(mission -> {
            if (startDate == null && endDate == null)
                return true;

            try {
                LocalDate missionStartDate = LocalDate.parse(mission.getStartDate(), dateFormatter);
                LocalDate missionEndDate = mission.getEndDate() != null && !mission.getEndDate().isEmpty()
                        ? LocalDate.parse(mission.getEndDate(), dateFormatter)
                        : null;

                if (startDate != null && missionEndDate != null && missionEndDate.isBefore(startDate))
                    return false;
                if (endDate != null && missionStartDate.isAfter(endDate))
                    return false;

                return true;
            } catch (DateTimeParseException e) {
                return true; // Include items with invalid dates in results
            }
        });
        updateResultsLabel();
    }

    private void updateResultsLabel() {
        if (filterControls != null) {
            filterControls.updateResultsLabel(filterUtil.getFilteredCount(), filterUtil.getTotalCount());
        }
    }

    private void refreshFilterOptions() {
        setupStatusFilterOptions();
        setupLocationFilterOptions();
        updateResultsLabel();
    }

    private void setupTableSelectionListener() {
        missionsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        populateFields(newValue);
                    }
                });
    }

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private void populateFields(MissionRecord mission) {
        missionIdField.setText(mission.getMissionId());
        missionNameField.setText(mission.getName());
        objectiveField.setText(mission.getObjective());

        try {
            if (mission.getStartDate() != null && !mission.getStartDate().isEmpty()) {
                LocalDate startDate = LocalDate.parse(mission.getStartDate(), dateFormatter);
                startDatePicker.setValue(startDate);
            } else {
                startDatePicker.setValue(null);
            }

            if (mission.getEndDate() != null && !mission.getEndDate().isEmpty()) {
                LocalDate endDate = LocalDate.parse(mission.getEndDate(), dateFormatter);
                endDatePicker.setValue(endDate);
            } else {
                endDatePicker.setValue(null);
            }
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
            startDatePicker.setValue(null);
            endDatePicker.setValue(null);
        }

        statusComboBox.setValue(mission.getStatus());
        locationIdField.setText(mission.getLocationId());
    }

    @FXML
    private void clearFields() {
        missionIdField.clear();
        missionNameField.clear();
        objectiveField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        statusComboBox.setValue("planned");
        locationIdField.clear();
        missionsTable.getSelectionModel().clearSelection();
    }

    @FXML
    @SuppressWarnings("unused")
    private void addMission() {
        if (!requirePermission(Permission.WRITE_DATA)) {
            return;
        }

        if (!validateFields()) {
            return;
        }

        String sql = "INSERT INTO missions(mission_id, name, objective, start_date, end_date, status, location_id) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, missionIdField.getText().trim());
            pstmt.setString(2, missionNameField.getText().trim());
            pstmt.setString(3, objectiveField.getText().trim());

            String startDateStr = startDatePicker.getValue() != null ? startDatePicker.getValue().format(dateFormatter)
                    : "";
            String endDateStr = endDatePicker.getValue() != null ? endDatePicker.getValue().format(dateFormatter) : "";

            pstmt.setString(4, startDateStr);
            pstmt.setString(5, endDateStr);
            pstmt.setString(6, statusComboBox.getValue());
            pstmt.setString(7, locationIdField.getText().trim());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                showSuccessAlert("Mission added successfully!");
                clearFields();
                loadMissionData();
            } else {
                showErrorAlert("Failed to add mission.");
            }

        } catch (SQLException e) {
            showErrorAlert("Database error: " + e.getMessage());
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void updateMission() {
        if (!requirePermission(Permission.WRITE_DATA)) {
            return;
        }

        if (missionIdField.getText().trim().isEmpty()) {
            showWarningAlert("Please select a mission to update or enter a Mission ID.");
            return;
        }

        if (!validateFields()) {
            return;
        }

        String sql = "UPDATE missions SET name=?, objective=?, start_date=?, end_date=?, status=?, location_id=? WHERE mission_id=?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, missionNameField.getText().trim());
            pstmt.setString(2, objectiveField.getText().trim());

            String startDateStr = startDatePicker.getValue() != null ? startDatePicker.getValue().format(dateFormatter)
                    : "";
            String endDateStr = endDatePicker.getValue() != null ? endDatePicker.getValue().format(dateFormatter) : "";

            pstmt.setString(3, startDateStr);
            pstmt.setString(4, endDateStr);
            pstmt.setString(5, statusComboBox.getValue());
            pstmt.setString(6, locationIdField.getText().trim());
            pstmt.setString(7, missionIdField.getText().trim());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                showSuccessAlert("Mission updated successfully!");
                clearFields();
                loadMissionData();
            } else {
                showErrorAlert("Failed to update mission. Mission ID not found.");
            }

        } catch (SQLException e) {
            showErrorAlert("Database error: " + e.getMessage());
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void deleteMission() {
        if (!requirePermission(Permission.DELETE_DATA)) {
            return;
        }

        if (missionIdField.getText().trim().isEmpty()) {
            showWarningAlert("Please select a mission to delete or enter a Mission ID.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Mission");
        confirmAlert.setContentText("Are you sure you want to delete this mission? This action cannot be undone.");

        if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        String sql = "DELETE FROM missions WHERE mission_id=?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, missionIdField.getText().trim());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                showSuccessAlert("Mission deleted successfully!");
                clearFields();
                loadMissionData();
            } else {
                showErrorAlert("Failed to delete mission. Mission ID not found.");
            }

        } catch (SQLException e) {
            showErrorAlert("Database error: " + e.getMessage());
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void showAnalytics() {
        if (!requirePermission(Permission.ANALYZE_DATA)) {
            return;
        }

        try {
            com.warManagementGUI.DataAnalysis.MissionsBarChart.showMissionStatusChart();
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

    private void loadMissionData() {
        if (!requirePermission(Permission.READ_DATA)) {
            return;
        }

        missionData.clear();
        String sql = "SELECT * FROM missions ORDER BY mission_id";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                MissionRecord mission = new MissionRecord(
                        rs.getString("mission_id"),
                        rs.getString("name"),
                        rs.getString("objective"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("status"),
                        rs.getString("location_id"));
                missionData.add(mission);
            }

            // Refresh filter options after loading data
            if (filterControls != null) {
                refreshFilterOptions();
            }

        } catch (SQLException e) {
            showErrorAlert("Error loading mission data: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        if (missionIdField.getText().trim().isEmpty()) {
            errors.append("Mission ID is required.\n");
        }

        if (missionNameField.getText().trim().isEmpty()) {
            errors.append("Mission Name is required.\n");
        }

        if (objectiveField.getText().trim().isEmpty()) {
            errors.append("Objective is required.\n");
        }

        if (startDatePicker.getValue() == null) {
            errors.append("Start Date is required.\n");
        }

        if (locationIdField.getText().trim().isEmpty()) {
            errors.append("Location ID is required.\n");
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
    @SuppressWarnings("unused")
    private void refreshData() {
        loadMissionData();
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
}
