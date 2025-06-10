package com.warManagementGUI.Personnel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.warManagementGUI.WarManagement;
import com.warManagementGUI.util.AbstractDetailsStage;
import com.warManagementGUI.util.DBUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Personnel_details extends AbstractDetailsStage {
    private final TableView<PersonnelRecord> personnelTable;
    private final ObservableList<PersonnelRecord> personnelData;

    public Personnel_details() {
        super("Personnel Details", 773, 529);
        createLabel("Personnel Details", TITLE_FONT, 29, 11, 394, 58);
        createNavButton("Back to Dashboard", WarManagement.class, 33, 383, 202, 73);
        createNavButton("Log out", Login.class, 590, 385, 134, 64);

        // Initialize table data
        personnelData = FXCollections.observableArrayList();

        // Create TableView
        personnelTable = new TableView<>(personnelData);
        personnelTable.setLayoutX(39);
        personnelTable.setLayoutY(79);
        personnelTable.setPrefWidth(697);
        personnelTable.setPrefHeight(274);

        setupTableColumns();
        rootPane.getChildren().add(personnelTable);

        loadPersonnelData();
    }

    private void setupTableColumns() {
        TableColumn<PersonnelRecord, String> personnelIdCol = new TableColumn<>("Personnel ID");
        personnelIdCol.setCellValueFactory(new PropertyValueFactory<>("personnelId"));
        personnelIdCol.setPrefWidth(90);

        TableColumn<PersonnelRecord, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstNameCol.setPrefWidth(90);

        TableColumn<PersonnelRecord, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastNameCol.setPrefWidth(90);

        TableColumn<PersonnelRecord, String> postCol = new TableColumn<>("Post");
        postCol.setCellValueFactory(new PropertyValueFactory<>("post"));
        postCol.setPrefWidth(80);

        TableColumn<PersonnelRecord, String> unitIdCol = new TableColumn<>("Unit ID");
        unitIdCol.setCellValueFactory(new PropertyValueFactory<>("unitId"));
        unitIdCol.setPrefWidth(70);

        TableColumn<PersonnelRecord, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setPrefWidth(80);

        TableColumn<PersonnelRecord, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(70);

        TableColumn<PersonnelRecord, String> contactCol = new TableColumn<>("Contact Info");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactInformation"));
        contactCol.setPrefWidth(117);

        personnelTable.getColumns().add(personnelIdCol);
        personnelTable.getColumns().add(firstNameCol);
        personnelTable.getColumns().add(lastNameCol);
        personnelTable.getColumns().add(postCol);
        personnelTable.getColumns().add(unitIdCol);
        personnelTable.getColumns().add(roleCol);
        personnelTable.getColumns().add(statusCol);
        personnelTable.getColumns().add(contactCol);
    }

    private void loadPersonnelData() {
        try (Connection conn = DBUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Personnel")) {

            while (rs.next()) {
                String personnelId = rs.getString("Personnel_id");
                String firstName = rs.getString("First_name");
                String lastName = rs.getString("Last_name");
                String post = rs.getString("Post");
                String unitId = rs.getString("Unit_Id");
                String role = rs.getString("Role");
                String status = rs.getString("Status");
                String contactInformation = rs.getString("contact_information");

                personnelData.add(new PersonnelRecord(personnelId, firstName, lastName, post,
                        unitId, role, status, contactInformation));
            }

        } catch (SQLException e) {
            handleDatabaseError(e, "Database error");
        }
    }

    // Data model class for TableView
    public static class PersonnelRecord {
        private final String personnelId;
        private final String firstName;
        private final String lastName;
        private final String post;
        private final String unitId;
        private final String role;
        private final String status;
        private final String contactInformation;

        public PersonnelRecord(String personnelId, String firstName, String lastName, String post,
                String unitId, String role, String status, String contactInformation) {
            this.personnelId = personnelId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.post = post;
            this.unitId = unitId;
            this.role = role;
            this.status = status;
            this.contactInformation = contactInformation;
        }

        public String getPersonnelId() {
            return personnelId;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getPost() {
            return post;
        }

        public String getUnitId() {
            return unitId;
        }

        public String getRole() {
            return role;
        }

        public String getStatus() {
            return status;
        }

        public String getContactInformation() {
            return contactInformation;
        }
    }
}
