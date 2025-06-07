package com.warManagementGUI.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.warManagementGUI.DataAnalysis.PersonnelBarChart;
import com.warManagementGUI.models.Permission;
import com.warManagementGUI.util.DBUtil;

import javafx.beans.property.SimpleStringProperty;
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
 * Controller for Personnel Management interface
 */
public class PersonnelController extends BaseController implements Initializable {

    // User info elements
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label userRoleLabel;
    @FXML
    private Button logoutBtn;

    @FXML
    private TextField personnelIdField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField postField;
    @FXML
    private TextField unitIdField;
    @FXML
    private TextField roleField;
    @FXML
    private TextField contactField;
    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private TableView<PersonnelRecord> personnelTable;
    @FXML
    private TableColumn<PersonnelRecord, String> idColumn;
    @FXML
    private TableColumn<PersonnelRecord, String> firstNameColumn;
    @FXML
    private TableColumn<PersonnelRecord, String> lastNameColumn;
    @FXML
    private TableColumn<PersonnelRecord, String> postColumn;
    @FXML
    private TableColumn<PersonnelRecord, String> unitColumn;
    @FXML
    private TableColumn<PersonnelRecord, String> roleColumn;
    @FXML
    private TableColumn<PersonnelRecord, String> contactColumn;
    @FXML
    private TableColumn<PersonnelRecord, String> statusColumn;
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
    @SuppressWarnings("unused")
    private Button backBtn;
    @FXML
    @SuppressWarnings("unused")
    private Button refreshBtn;
    @FXML
    private Button exportBtn;

    private final ObservableList<PersonnelRecord> personnelData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupStatusComboBox();
        setupTableColumns();
        setupTableSelection();
        loadPersonnelData();
        initializeTheme();
        configureUIBasedOnPermissions();
        updateUserInfo();
    }

    private void updateUserInfo() {
        if (authService.isLoggedIn()) {
            if (welcomeLabel != null) {
                welcomeLabel.setText("Welcome, " + authService.getCurrentUserFullName());
            }
            if (userRoleLabel != null) {
                userRoleLabel.setText("Role: " + authService.getCurrentUser().getRole().getDisplayName());
            }
        }
    }

    private void setupStatusComboBox() {
        statusComboBox.getItems().addAll("active", "injured", "MIA", "KIA");
        statusComboBox.setValue("active");
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("personnelId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        postColumn.setCellValueFactory(new PropertyValueFactory<>("post"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unitId"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        personnelTable.setItems(personnelData);
    }

    @Override
    protected void configureUIBasedOnPermissions() {
        configureButtonVisibility(addBtn, Permission.WRITE_DATA);
        configureButtonVisibility(updateBtn, Permission.WRITE_DATA);
        configureButtonVisibility(deleteBtn, Permission.DELETE_DATA);
        configureButtonVisibility(exportBtn, Permission.EXPORT_DATA);

        // Hide analytics button if user doesn't have permission
        if (!hasPermission(Permission.ANALYZE_DATA)) {
            if (personnelTable.getParent() != null) {
                // Find and hide analytics button if it exists
                javafx.scene.Node analyticsBtn = personnelTable.getParent().lookup("#analyticsBtn");
                if (analyticsBtn != null) {
                    analyticsBtn.setVisible(false);
                }
            }
        }
    }

    private void setupTableSelection() {
        personnelTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });
    }

    private void populateFields(PersonnelRecord record) {
        personnelIdField.setText(record.getPersonnelId());
        firstNameField.setText(record.getFirstName());
        lastNameField.setText(record.getLastName());
        postField.setText(record.getpost());
        unitIdField.setText(record.getUnitId());
        roleField.setText(record.getRole());
        contactField.setText(record.getContact());
        statusComboBox.setValue(record.getStatus());
    }

    @FXML
    @SuppressWarnings("unused")
    private void addPersonnel() {
        if (!requirePermission(Permission.WRITE_DATA)) {
            return;
        }

        if (validateFields()) {
            String sql = "INSERT INTO personnel (personnel_id, first_name, last_name, post, unit_id, role, contact_information, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);) {

                stmt.setString(1, personnelIdField.getText());
                stmt.setString(2, firstNameField.getText());
                stmt.setString(3, lastNameField.getText());
                stmt.setString(4, postField.getText());
                stmt.setString(5, unitIdField.getText());
                stmt.setString(6, roleField.getText());
                stmt.setString(7, contactField.getText());
                stmt.setString(8, statusComboBox.getValue());

                int result = stmt.executeUpdate();
                if (result > 0) {
                    showSuccess("Personnel added successfully!");
                    clearFields();
                    loadPersonnelData();
                }
            } catch (SQLException e) {
                showError("Error adding personnel: " + e.getMessage());
            }
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void updatePersonnel() {
        if (!requirePermission(Permission.WRITE_DATA)) {
            return;
        }

        PersonnelRecord selected = personnelTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a personnel record to update.");
            return;
        }

        if (validateFields()) {
            String sql = "UPDATE personnel SET first_name=?, last_name=?, post=?, unit_id=?, role=?, contact_information=?, status=? WHERE personnel_id=?";
            try (Connection conn = DBUtil.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql);) {

                stmt.setString(1, firstNameField.getText());
                stmt.setString(2, lastNameField.getText());
                stmt.setString(3, postField.getText());
                stmt.setString(4, unitIdField.getText());
                stmt.setString(5, roleField.getText());
                stmt.setString(6, contactField.getText());
                stmt.setString(7, statusComboBox.getValue());
                stmt.setString(8, personnelIdField.getText());

                int result = stmt.executeUpdate();
                if (result > 0) {
                    showSuccess("Personnel updated successfully!");
                    loadPersonnelData();
                }
            } catch (SQLException e) {
                showError("Error updating personnel: " + e.getMessage());
            }
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void deletePersonnel() {
        if (!requirePermission(Permission.DELETE_DATA)) {
            return;
        }

        PersonnelRecord selected = personnelTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a personnel record to delete.");
            return;
        }

        if (showConfirmation("Are you sure you want to delete this personnel record?")) {
            try (Connection conn = DBUtil.getConnection()) {
                String sql = "DELETE FROM personnel WHERE personnel_id=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, selected.getPersonnelId());

                int result = stmt.executeUpdate();
                if (result > 0) {
                    showSuccess("Personnel deleted successfully!");
                    clearFields();
                    loadPersonnelData();
                }
            } catch (SQLException e) {
                showError("Error deleting personnel: " + e.getMessage());
            }
        }
    }

    @FXML
    private void clearFields() {
        personnelIdField.clear();
        firstNameField.clear();
        lastNameField.clear();
        postField.clear();
        unitIdField.clear();
        roleField.clear();
        contactField.clear();
        statusComboBox.setValue("active");
        personnelTable.getSelectionModel().clearSelection();
    }

    @FXML
    @SuppressWarnings("unused")
    private void refreshData() {
        loadPersonnelData();
        showSuccess("Data refreshed successfully!");
    }

    @FXML
    @SuppressWarnings("unused")
    private void showAnalytics() {
        if (!requirePermission(Permission.ANALYZE_DATA)) {
            return;
        }

        try {
            PersonnelBarChart.showPersonnelStatusChart();
        } catch (Exception e) {
            showError("Error showing analytics: " + e.getMessage());
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void exportData() {
        if (!requirePermission(Permission.EXPORT_DATA)) {
            return;
        }

        showInfo("Export functionality will be available soon!");
    }

    /**
     * Navigate back to dashboard
     */
    @FXML
    @SuppressWarnings("unused")
    private void goBack() {
        try {
            navigateToScene("/com/warManagementGUI/fxml/Dashboard.fxml", "War Management System - Dashboard", backBtn);
        } catch (IOException e) {
            showError("Error loading dashboard: " + e.getMessage());
        }
    }

    /**
     * Handle logout - return to login screen
     */
    @FXML
    @SuppressWarnings("unused") // Used by FXML
    private void handleLogout() {
        try {
            // Logout from authentication service
            authService.logout();

            // Load login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/warManagementGUI/fxml/Login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 400, 600);
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("War Data Analysis System - Login");
            stage.setResizable(false);
            stage.centerOnScreen();

        } catch (IOException e) {
            showError("Failed to load login screen: " + e.getMessage());
        }
    }

    private void loadPersonnelData() {
        if (!hasPermission(Permission.READ_DATA)) {
            showError("You don't have permission to view personnel data.");
            return;
        }

        personnelData.clear();
        String sql = "SELECT * FROM personnel";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                personnelData.add(new PersonnelRecord(
                        rs.getString("personnel_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("post"),
                        rs.getString("unit_id"),
                        rs.getString("role"),
                        rs.getString("contact_information"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            showError("Error loading personnel data: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (personnelIdField.getText().trim().isEmpty()) {
            showError("Personnel ID is required!");
            return false;
        }
        if (firstNameField.getText().trim().isEmpty()) {
            showError("First Name is required!");
            return false;
        }
        if (lastNameField.getText().trim().isEmpty()) {
            showError("Last Name is required!");
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

    /**
     * Data model class for Personnel records
     */
    public static class PersonnelRecord {
        private final SimpleStringProperty personnelId;
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty post;
        private final SimpleStringProperty unitId;
        private final SimpleStringProperty role;
        private final SimpleStringProperty contact;
        private final SimpleStringProperty status;

        public PersonnelRecord(String personnelId, String firstName, String lastName,
                String post, String unitId, String role, String contact, String status) {
            this.personnelId = new SimpleStringProperty(personnelId);
            this.firstName = new SimpleStringProperty(firstName);
            this.lastName = new SimpleStringProperty(lastName);
            this.post = new SimpleStringProperty(post);
            this.unitId = new SimpleStringProperty(unitId);
            this.role = new SimpleStringProperty(role);
            this.contact = new SimpleStringProperty(contact);
            this.status = new SimpleStringProperty(status);
        }

        public String getPersonnelId() {
            return personnelId.get();
        }

        public String getFirstName() {
            return firstName.get();
        }

        public String getLastName() {
            return lastName.get();
        }

        public String getpost() {
            return post.get();
        }

        public String getUnitId() {
            return unitId.get();
        }

        public String getRole() {
            return role.get();
        }

        public String getContact() {
            return contact.get();
        }

        public String getStatus() {
            return status.get();
        }

        public SimpleStringProperty personnelIdProperty() {
            return personnelId;
        }

        public SimpleStringProperty firstNameProperty() {
            return firstName;
        }

        public SimpleStringProperty lastNameProperty() {
            return lastName;
        }

        public SimpleStringProperty postProperty() {
            return post;
        }

        public SimpleStringProperty unitIdProperty() {
            return unitId;
        }

        public SimpleStringProperty roleProperty() {
            return role;
        }

        public SimpleStringProperty contactProperty() {
            return contact;
        }

        public SimpleStringProperty statusProperty() {
            return status;
        }
    }
}
