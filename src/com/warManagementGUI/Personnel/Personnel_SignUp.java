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

public class Personnel_SignUp extends AbstractDetailsStage {    
    private TextField Personnel_ID_txt, Fname_txt, Lname_txt, Post_txt, Unit_ID_txt, Role_txt, Contact_details_txt;
    private ComboBox<String> Status_txt;

    public Personnel_SignUp() {
        super("Personnel Sign Up", 773, 529);
        setupLabels();
        setupTextFields();
        setupComboBox();
        setupButtons();
    }

    private void setupLabels() {
        createLabel("Personnel Sign Up", TITLE_FONT, 38, 26, 406, 81);
        createLabel("Personnel ID", LABEL_FONT, 38, 117, 129, 30);
        createLabel("First Name", LABEL_FONT, 38, 169, 139, 30);
        createLabel("Last Name", LABEL_FONT, 38, 232, 139, 30);
        createLabel("Post", LABEL_FONT, 38, 290, 63, 35);
        createLabel("Unit ID", LABEL_FONT, 390, 117, 85, 30);
        createLabel("Role", LABEL_FONT, 390, 172, 69, 25);
        createLabel("Status", LABEL_FONT, 390, 236, 69, 22);
        createLabel("Contact Details", LABEL_FONT, 390, 295, 139, 24);
    }

    private void setupTextFields() {
        Personnel_ID_txt = createTextField(201, 117, 96, 19);
        Fname_txt = createTextField(201, 177, 96, 19);
        Lname_txt = createTextField(201, 240, 96, 19);
        Post_txt = createTextField(201, 300, 96, 19);
        Unit_ID_txt = createTextField(565, 125, 96, 19);
        Role_txt = createTextField(565, 177, 96, 19);
        Contact_details_txt = createTextField(565, 300, 96, 19);
    }

    private void setupComboBox() {
        Status_txt = createComboBox(new String[]{"active", "injured", "MIA", "KIA"}, 565, 237, 96, 21);
    }

    private void setupButtons() {
        createNavButton("Back to Dashboard", WarManagement.class, 10, 380, 228, 47);
        createButton("Reset", BUTTON_FONT, TEXT_COLOR, BUTTON_COLOR, 313, 380, 145, 47, e -> refreshTextFields());
        createButton("Sign Up", BUTTON_FONT, TEXT_COLOR, BUTTON_COLOR, 549, 380, 145, 47, e -> insertPersonnel());
    }
    private void refreshTextFields() {
        Personnel_ID_txt.setText("");
        Fname_txt.setText("");
        Lname_txt.setText("");
        Post_txt.setText("");
        Unit_ID_txt.setText("");
        Role_txt.setText("");
        Contact_details_txt.setText("");
        Status_txt.getSelectionModel().selectFirst();
    }

    private void insertPersonnel() {
        String personnelId = Personnel_ID_txt.getText().trim();
        String firstName = Fname_txt.getText().trim();
        String lastName = Lname_txt.getText().trim();
        String post = Post_txt.getText().trim();
        String unitId = Unit_ID_txt.getText().trim();
        String role = Role_txt.getText().trim();
        String status = Status_txt.getSelectionModel().getSelectedItem();
        String contactInfo = Contact_details_txt.getText().trim();

        if (isAnyFieldEmpty(personnelId, firstName, lastName, post, unitId, role, status, contactInfo)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("All fields must be filled.");
            alert.showAndWait();
            return;
        }

        String sql = "INSERT INTO Personnel(Personnel_id, First_name, Last_name, Post, Unit_Id, Role, Status, contact_information) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, personnelId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, post);
            pstmt.setString(5, unitId);
            pstmt.setString(6, role);
            pstmt.setString(7, status);
            pstmt.setString(8, contactInfo);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Personnel details inserted successfully.");
                alert.showAndWait();
                new Personnel_details().display();
                dispose();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to insert personnel details.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            handleDatabaseError(e, "Error inserting personnel");
        }
    }

    private boolean isAnyFieldEmpty(String... fields) {
        for (String f : fields) {
            if (f == null || f.isEmpty()) return true;
        }
        return false;
    }
}

