package com.warManagementGUI.Personnel;

import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.warManagementGUI.WarManagement;
import com.warManagementGUI.util.AbstractDetailsFrame;
import com.warManagementGUI.util.DBUtil;

public class Personnel_Update extends AbstractDetailsFrame {
    private static final long serialVersionUID = 1L;
    // Title font only
    private static final Font TITLE_FONT = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50);

    private JTextField Personnel_ID_txt, FirstName_txt, LastName_txt, Post_txt, Unit_ID_txt, Role_txt, Contact_details_txt;
    private JComboBox<String> Status_txt;

    public static void main(String[] args) {
        try {
            new Personnel_Update().display();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error launching application: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

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
    }

    private void setupTextFields() {
        Personnel_ID_txt = createTextField(209, 104, 96, 19, 10);
        FirstName_txt   = createTextField(209, 161, 96, 19, 10);
        LastName_txt    = createTextField(209, 216, 96, 19, 10);
        Post_txt        = createTextField(209, 267, 96, 19, 10);
        Unit_ID_txt     = createTextField(574, 114, 96, 19, 10);
        Role_txt        = createTextField(574, 161, 96, 19, 10);
        Contact_details_txt = createTextField(574, 267, 96, 19, 10);
    }

    private void setupComboBox() {
        Status_txt = createComboBox(new String[]{"active","injured","MIA","KIA"}, 566, 208, 127, 35);
    }

    private void setupButtons() {
        createButton("Back to Dashboard", BUTTON_FONT, TEXT_COLOR, BG_COLOR, 10, 358, 207, 58, e -> navigateToDashboard());
        createButton("Refresh", BUTTON_FONT, TEXT_COLOR, BG_COLOR, 319, 355, 127, 64, e -> refreshTextFields());
        createButton("Update", BUTTON_FONT, TEXT_COLOR, BG_COLOR, 602, 355, 119, 58, e -> updatePersonnel());
    }

    private void navigateToDashboard() {
        new WarManagement().display();
        dispose();
    }

    private void refreshTextFields() {
        Personnel_ID_txt.setText("");
        FirstName_txt.setText("");
        LastName_txt.setText("");
        Post_txt.setText("");
        Unit_ID_txt.setText("");
        Role_txt.setText("");
        Contact_details_txt.setText("");
        Status_txt.setSelectedIndex(0);
    }

    private void updatePersonnel() {
        String sql = "UPDATE personnel SET first_Name=?, last_Name=?, post=?, unit_ID=?, role=?, status=?, contact_information=? WHERE personnel_id=?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, FirstName_txt.getText());
            pstmt.setString(2, LastName_txt.getText());
            pstmt.setString(3, Post_txt.getText());
            pstmt.setString(4, Unit_ID_txt.getText());
            pstmt.setString(5, Role_txt.getText());
            pstmt.setString(6, (String) Status_txt.getSelectedItem());
            pstmt.setString(7, Contact_details_txt.getText());
            pstmt.setString(8, Personnel_ID_txt.getText());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Personnel updated successfully.");
                new Personnel_details().display();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Update failed. Personnel ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            handleDatabaseError(e, "Error updating personnel");
        }
    }
}
