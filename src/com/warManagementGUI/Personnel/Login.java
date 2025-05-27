package com.warManagementGUI.Personnel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.warManagementGUI.WarManagement;
import com.warManagementGUI.util.AbstractDetailsStage;
import com.warManagementGUI.util.DBUtil;

import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Login extends AbstractDetailsStage {

	private TextField Personnel_ID_txt;
	private TextField FirstName_txt;
	private TextField Role_txt;

	/**
	 * Create the stage.
	 */
	public Login() {
		super("Personnel Login Page", 773, 529);
		
		createIconLabel("/pics/login.jpeg", 37, 82, 169, 265);

		createLabel("Personnel Login Page", TITLE_FONT, Color.WHITE, 91, 24, 622, 41);

		createLabel("Personnel ID",LABEL_FONT, Color.WHITE, 272, 137, 128, 53);

		createLabel("First  Name",LABEL_FONT, Color.WHITE, 272, 201, 128, 53);

		createLabel("Role",LABEL_FONT, Color.WHITE, 284, 265, 116, 53);

		Personnel_ID_txt = createTextField(473, 155, 150, 20);

		FirstName_txt = createTextField(473, 219, 150, 20);
		Role_txt = createTextField(473, 283, 155, 20);

		createButton("login", BUTTON_FONT, Color.WHITE, Color.BLACK,
			572, 420, 89, 41,
			e -> {
				String personnelId = Personnel_ID_txt.getText();
				String firstName = FirstName_txt.getText();
				String role = Role_txt.getText();
				if (checkCredentials(personnelId, firstName, role)) {
					new Personnel_details().display();
					dispose();
				} else {
					showErrorDialog("Invalid credentials. Please try again.");
				}
			});

		createNavButton("remove", Personnel_Remove_Details.class ,325, 420, 119, 41);

		createNavButton("update", Personnel_Update.class, 454, 420, 108, 41);

		createNavButton("Back to Dashboard", WarManagement.class, 10, 420, 177, 41);

		createNavButton("Sign Up", Personnel_SignUp.class ,197, 420, 118, 41);

		createButton("Analyse", Font.font("Times New Roman", FontWeight.BOLD, 15), Color.WHITE, Color.BLACK,
			671, 420, 85, 41,
			e -> showAnalysis());
	}
	
	private boolean checkCredentials(String personnelId, String firstName, String role) {
        try (Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Personnel WHERE Personnel_id=? AND First_name=? AND Role=?")) {
            pstmt.setString(1, personnelId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, role);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // If there's a result, credentials are valid

		} catch (Exception e) {
			handleDatabaseError(e, "Database error");
		}
		return false;
	}
	private void showAnalysis() {
		try {
			// Show Personnel by Status chart
			com.warManagementGUI.DataAnalysis.PersonnelBarChart.showPersonnelStatusChart();
		} catch (Exception e) {
			showErrorDialog("Error showing analysis: " + e.getMessage());
		}
	}
}