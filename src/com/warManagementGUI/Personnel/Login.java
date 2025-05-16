package com.warManagementGUI.Personnel;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.io.Serial;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.warManagementGUI.DataAnalysis.PersonnelBarChart;
import com.warManagementGUI.WarManagement;
import com.warManagementGUI.util.AbstractDetailsFrame;
import com.warManagementGUI.util.DBUtil;

public class Login extends AbstractDetailsFrame {

	@Serial
	private static final long serialVersionUID = 1L;
	private final JTextField Personnel_ID_txt;
	private final JTextField FirstName_txt;
	private final JTextField Role_txt;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			new Login().display();
		} catch (Exception e) {
			System.err.println("Database error: " + e.getMessage());
			JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(),
                                         "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		super("Personnel Login Page", 773, 529);
		// ...existing UI setup code...
         
		createIconLabel("/pics/login.jpeg", 37, 82, 169, 265);

		createLabel("Personnel Login Page", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50), Color.WHITE, 91, 24, 622, 41);

		createLabel("Personnel ID", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), Color.WHITE, 272, 137, 128, 53);

		createLabel("First  Name", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), Color.WHITE, 272, 201, 128, 53);

		createLabel("Role", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), Color.WHITE, 284, 265, 116, 53);

		Personnel_ID_txt = createTextField(473, 155, 150, 20, 10);

		FirstName_txt = createTextField(473, 219, 150, 20, 10);

		Role_txt = createTextField(473, 283, 155, 20, 10);

		createButton("login", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15), Color.WHITE, Color.BLACK,
			572, 420, 89, 41,
			e -> {
				String personnelId = Personnel_ID_txt.getText();
				String firstName = FirstName_txt.getText();
				String role = Role_txt.getText();
				if (checkCredentials(personnelId, firstName, role)) {
					new Personnel_details().display();
					dispose();
				} else {
					JOptionPane.showMessageDialog(contentPane, "Invalid credentials. Please try again.");
				}
			});

		createButton("remove", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15), Color.WHITE, Color.BLACK,
			325, 420, 119, 41,
			e -> { new Personnel_Remove_Details().display(); dispose(); });

		createButton("update", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15), Color.WHITE, Color.BLACK,
			454, 420, 108, 41,
			e -> { new Personnel_Update().display(); dispose(); });

		createButton("Back to Dashboard", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15), Color.WHITE, Color.BLACK,
			10, 420, 177, 41,
			e -> { new WarManagement().display(); dispose(); });

		createButton("Sign Up", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15), Color.WHITE, Color.BLACK,
			197, 420, 118, 41,
			e -> { new Personnel_SignUp().display(); dispose(); });

		createButton("Analyse", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15), Color.WHITE, Color.BLACK,
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
			System.err.println("Database error: " + e.getMessage());
			JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}

	private void showAnalysis() {
	   JFrame chartFrame = PersonnelBarChart.showPersonnelStatusChart();
	
	   // Optional: Add a listener to handle the chart window closing
	   chartFrame.addWindowListener(new WindowAdapter() {
	    //    @Override
	    //    public void windowClosing(WindowEvent e) {
	    //        refreshTableData();
	    //    }
	   });
	}
}