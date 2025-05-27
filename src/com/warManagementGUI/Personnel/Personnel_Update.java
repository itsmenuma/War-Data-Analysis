package com.warManagementGUI.Personnel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.warManagementGUI.WarManagement;
import com.warManagementGUI.util.AbstractDetailsStage;
import com.warManagementGUI.util.DBUtil;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class Personnel_Update extends AbstractDetailsStage {

    private TextField Personnel_ID_txt, FirstName_txt, LastName_txt, Post_txt, Unit_ID_txt, Role_txt, Contact_details_txt;
    private ComboBox<String> Status_txt;

    public Personnel_Update() {
        super("Personnel Update", 773, 529);
        setupLabels();
        setupTextFields();
        setupComboBox();
        setupButtons();
    }

    private void setupLabels() {
        createLabel("Personnel Update Page", TITLE_FONT, TEXT_COLOR, 10, 10, 502, 84);
        createLabel("Personnel ID", LABEL_FONT, TEXT_COLOR, 31, 104, 148, 35);
        createLabel("First Name", LABEL_FONT, TEXT_COLOR, 31, 161, 127, 35);
        createLabel("Last Name", LABEL_FONT, TEXT_COLOR, 31, 206, 127, 35);
        createLabel("Post", LABEL_FONT, TEXT_COLOR, 31, 257, 119, 35);
        createLabel("Unit ID", LABEL_FONT, TEXT_COLOR, 414, 104, 132, 35);
        createLabel("Role", LABEL_FONT, TEXT_COLOR, 414, 161, 142, 35);
        createLabel("Status", LABEL_FONT, TEXT_COLOR, 414, 206, 142, 35);
        createLabel("Contact Details", LABEL_FONT, TEXT_COLOR, 414, 257, 148, 35);
    }    private void setupTextFields() {
        Personnel_ID_txt = createTextField(209, 104, 96, 19);
        FirstName_txt   = createTextField(209, 161, 96, 19);
        LastName_txt    = createTextField(209, 216, 96, 19);
        Post_txt        = createTextField(209, 267, 96, 19);
        Unit_ID_txt     = createTextField(574, 114, 96, 19);
        Role_txt        = createTextField(574, 161, 96, 19);
        Contact_details_txt = createTextField(574, 267, 96, 19);
    }

    private void setupComboBox() {
        Status_txt = createComboBox(new String[]{"active","injured","MIA","KIA"}, 566, 208, 127, 35);
    }

    private void setupButtons() {
        createNavButton("Back to Dashboard",WarManagement.class, 10, 358, 207, 58);
        createButton("Refresh", BUTTON_FONT, TEXT_COLOR, BG_COLOR, 319, 355, 127, 64, e -> refreshTextFields());
        createButton("Update", BUTTON_FONT, TEXT_COLOR, BG_COLOR, 602, 355, 119, 58, e -> updatePersonnel());
    }

    private void refreshTextFields() {
        Personnel_ID_txt.setText("");
        FirstName_txt.setText("");
        LastName_txt.setText("");
        Post_txt.setText("");
        Unit_ID_txt.setText("");
        Role_txt.setText("");
        Contact_details_txt.setText("");
        Status_txt.getSelectionModel().selectFirst();
    }    private void updatePersonnel() {
        String sql = "UPDATE personnel SET first_Name=?, last_Name=?, post=?, unit_ID=?, role=?, status=?, contact_information=? WHERE personnel_id=?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, FirstName_txt.getText());
            pstmt.setString(2, LastName_txt.getText());
            pstmt.setString(3, Post_txt.getText());
            pstmt.setString(4, Unit_ID_txt.getText());
            pstmt.setString(5, Role_txt.getText());
            pstmt.setString(6, Status_txt.getSelectionModel().getSelectedItem());
            pstmt.setString(7, Contact_details_txt.getText());
            pstmt.setString(8, Personnel_ID_txt.getText());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Personnel updated successfully.");
                alert.showAndWait();
                new Personnel_details().display();
                dispose();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Update failed. Personnel ID not found.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            handleDatabaseError(e, "Error updating personnel");
        }
    }
}
