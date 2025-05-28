package com.warManagementGUI.Mission;

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

public class Mission_details extends AbstractDetailsStage {
    private TextField mission_ID_txt, mission_name_txt, Objective_txt, start_date_txt, end_date_txt, location_ID_txt;
    private ComboBox<String> statusComboBox;
    private TableView<MissionRecord> Missions_table;
    private final ObservableList<MissionRecord> missionData;

    public Mission_details() {
        super("Mission", 773, 529);
        missionData = FXCollections.observableArrayList();
        setupLabels();
        setupTextFields();
        setupComboBox();
        setupTable();
        setupButtons();
        refreshTableData();
    }

    private void setupLabels() {
        createLabel("Mission Details", TITLE_FONT, 10, 23, 228, 40);
        createLabel("Mission ID", 20, 84, 114, 25);
        createLabel("Mission Name", 20, 120, 138, 25);
        createLabel("Objective", 20, 170, 125, 25);
        createLabel("Start Date", 20, 220, 108, 25);
        createLabel("End Date", 20, 266, 108, 25);
        createLabel("Status", 20, 309, 108, 25);
        createLabel("Location ID", 20, 352, 114, 25);
    }

    private void setupTextFields() {
        mission_ID_txt = createTextField(179, 79, 86, 20);
        mission_name_txt = createTextField(179, 133, 86, 20);
        Objective_txt = createTextField(179, 179, 86, 20);
        start_date_txt = createTextField(179, 230, 86, 20);
        end_date_txt = createTextField(179, 278, 86, 20);
        location_ID_txt = createTextField(179, 361, 86, 20);
    }

    private void setupComboBox() {
        statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("planned", "ongoing", "completed");
        statusComboBox.setLayoutX(179);
        statusComboBox.setLayoutY(311);
        statusComboBox.setPrefWidth(96);
        statusComboBox.setPrefHeight(21);
        rootPane.getChildren().add(statusComboBox);
    }

    private void setupTable() {
        Missions_table = new TableView<>();
        Missions_table.setLayoutX(294);
        Missions_table.setLayoutY(48);
        Missions_table.setPrefWidth(454);
        Missions_table.setPrefHeight(315);
        
        TableColumn<MissionRecord, String> missionIdCol = new TableColumn<>("Mission ID");
        missionIdCol.setCellValueFactory(new PropertyValueFactory<>("missionId"));
        missionIdCol.setPrefWidth(65);
        
        TableColumn<MissionRecord, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(80);
        
        TableColumn<MissionRecord, String> objectiveCol = new TableColumn<>("Objective");
        objectiveCol.setCellValueFactory(new PropertyValueFactory<>("objective"));
        objectiveCol.setPrefWidth(90);
        
        TableColumn<MissionRecord, String> startDateCol = new TableColumn<>("Start Date");
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startDateCol.setPrefWidth(70);
        
        TableColumn<MissionRecord, String> endDateCol = new TableColumn<>("End Date");
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endDateCol.setPrefWidth(70);
        
        TableColumn<MissionRecord, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(60);
        
        TableColumn<MissionRecord, String> locationIdCol = new TableColumn<>("Location ID");
        locationIdCol.setCellValueFactory(new PropertyValueFactory<>("locationId"));
        locationIdCol.setPrefWidth(70);
        
        Missions_table.getColumns().addAll(missionIdCol, nameCol, objectiveCol, startDateCol, endDateCol, statusCol, locationIdCol);
        Missions_table.setItems(missionData);
        rootPane.getChildren().add(Missions_table);
    }

    private void setupButtons() {
        createNavButton("Back to Dashboard", WarManagement.class, 10, 427, 198, 55);
        createButton("Refresh", 218, 425, 105, 58, e -> refreshTextFields());
        createButton("Insert", 333, 425, 86, 58, e -> insertMission());
        createButton("Update", 429, 427, 86, 55, e -> updateMission());
        createButton("Delete", 525, 425, 86, 58, e -> deleteMission());
        createButton("Analyze", 621, 425, 114, 58, e -> analyzeMissions());
    }    private void analyzeMissions() {
        try {
            // Show Mission by Status chart
            com.warManagementGUI.DataAnalysis.MissionsBarChart.showMissionStatusChart();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Analysis Error");
            alert.setContentText("Error showing analysis: " + e.getMessage());
            alert.showAndWait();
        }
    }private void refreshTextFields() {
        mission_ID_txt.clear();
        mission_name_txt.clear();
        Objective_txt.clear();
        start_date_txt.clear();
        end_date_txt.clear();
        location_ID_txt.clear();
    }

    private void refreshTableData() {
        missionData.clear();
        String query = "SELECT * FROM missions";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                missionData.add(new MissionRecord(
                        rs.getString("mission_id"), rs.getString("name"), rs.getString("objective"),
                        rs.getString("start_date"), rs.getString("end_date"), rs.getString("status"), rs.getString("location_id")
                ));
            }
        } catch (SQLException e) {
            handleDatabaseError(e, "Error retrieving mission data");
        }
    }    private void insertMission() {
        if (mission_ID_txt.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
            return;
        }
        String sql = "INSERT INTO missions(mission_id,name,objective,start_date,end_date,status,location_id) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mission_ID_txt.getText()); pstmt.setString(2, mission_name_txt.getText());
            pstmt.setString(3, Objective_txt.getText()); pstmt.setString(4, start_date_txt.getText());
            pstmt.setString(5, end_date_txt.getText()); pstmt.setString(6, statusComboBox.getSelectionModel().getSelectedItem());
            pstmt.setString(7, location_ID_txt.getText());
            if (pstmt.executeUpdate() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Mission inserted.");
                alert.showAndWait();
            }
            refreshTextFields(); refreshTableData();
        } catch (SQLException e) {
            handleDatabaseError(e, "Error inserting mission");
        }
    }

    private void updateMission() {
        if (mission_ID_txt.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a Mission ID to update");
            alert.showAndWait();
            return;
        }
        String sql = "UPDATE missions SET name=?,objective=?,start_date=?,end_date=?,status=?,location_id=? WHERE mission_id=?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mission_name_txt.getText()); pstmt.setString(2, Objective_txt.getText());
            pstmt.setString(3, start_date_txt.getText()); pstmt.setString(4, end_date_txt.getText());
            pstmt.setString(5, statusComboBox.getSelectionModel().getSelectedItem()); pstmt.setString(6, location_ID_txt.getText());
            pstmt.setString(7, mission_ID_txt.getText());
            if (pstmt.executeUpdate() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Mission updated.");
                alert.showAndWait();
            }
            refreshTextFields(); refreshTableData();
        } catch (SQLException e) {
            handleDatabaseError(e, "Error updating mission");
        }
    }

    private void deleteMission() {
        if (mission_ID_txt.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a Mission ID to delete");
            alert.showAndWait();
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Mission");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Confirm deletion?");
        if (confirmAlert.showAndWait().get() != ButtonType.OK) return;
        
        String sql = "DELETE FROM missions WHERE mission_id=?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mission_ID_txt.getText());
            if (pstmt.executeUpdate() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Mission deleted.");
                alert.showAndWait();
            }
            refreshTextFields(); refreshTableData();
        } catch (SQLException e) {
            handleDatabaseError(e, "Error deleting mission");
        }
    }
}
