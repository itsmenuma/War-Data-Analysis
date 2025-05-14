package com.warManagementGUI.Personnel;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.warManagementGUI.WarManagement;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
//import javax.swing.SwingUtilities;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class Personnel_SignUp extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel Jpanel;
	private JTextField Personnel_ID_txt;
	private JTextField Fname_txt;
	private JTextField Lname_txt;
	private JTextField Post_txt;
	private JTextField Unit_ID_txt;
	private JTextField Role_txt;
	private JTextField Contact_details_txt;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Personnel_SignUp frame = new Personnel_SignUp();
					frame.setVisible(true);
					
				            
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Personnel_SignUp() {
		setTitle("Personnel Sign Up");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 773, 529);
		Jpanel = new JPanel();
		Jpanel.setBackground(new Color(0, 64, 64));
		Jpanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(Jpanel);
		Jpanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Personnel Sign Up");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50));
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(38, 26, 406, 81);
		Jpanel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Personnel_ID");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setBounds(38, 117, 129, 30);
		Jpanel.add(lblNewLabel_1);
		
		Personnel_ID_txt = new JTextField();
		Personnel_ID_txt.setBounds(201, 117, 96, 19);
		Jpanel.add(Personnel_ID_txt);
		Personnel_ID_txt.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("First Name");
		lblNewLabel_2.setBackground(new Color(0, 0, 0));
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_2.setForeground(new Color(255, 255, 255));
		lblNewLabel_2.setBounds(38, 169, 139, 30);
		Jpanel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Last Name");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_3.setForeground(new Color(255, 255, 255));
		lblNewLabel_3.setBounds(38, 232, 139, 30);
		Jpanel.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Post");
		lblNewLabel_4.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_4.setForeground(new Color(255, 255, 255));
		lblNewLabel_4.setBounds(38, 290, 63, 35);
		Jpanel.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("Unit_ID");
		lblNewLabel_5.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_5.setForeground(new Color(255, 255, 255));
		lblNewLabel_5.setBounds(390, 117, 85, 30);
		Jpanel.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("Role");
		lblNewLabel_6.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_6.setForeground(new Color(255, 255, 255));
		lblNewLabel_6.setBounds(390, 172, 69, 25);
		Jpanel.add(lblNewLabel_6);
		
		JLabel lblNewLabel_7 = new JLabel("Status");
		lblNewLabel_7.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_7.setForeground(new Color(255, 255, 255));
		lblNewLabel_7.setBounds(390, 236, 69, 22);
		Jpanel.add(lblNewLabel_7);
		
		JLabel lblNewLabel_8 = new JLabel("Contact Details");
		lblNewLabel_8.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_8.setForeground(new Color(255, 255, 255));
		lblNewLabel_8.setBounds(390, 295, 139, 24);
		Jpanel.add(lblNewLabel_8);
		
		Fname_txt = new JTextField();
		Fname_txt.setBounds(201, 177, 96, 19);
		Jpanel.add(Fname_txt);
		Fname_txt.setColumns(10);
		
		Lname_txt = new JTextField();
		Lname_txt.setBounds(201, 240, 96, 19);
		Jpanel.add(Lname_txt);
		Lname_txt.setColumns(10);
		
		Post_txt = new JTextField();
		Post_txt.setBounds(201, 300, 96, 19);
		Jpanel.add(Post_txt);
		Post_txt.setColumns(10);
		
		Unit_ID_txt = new JTextField();
		Unit_ID_txt.setBounds(565, 125, 96, 19);
		Jpanel.add(Unit_ID_txt);
		Unit_ID_txt.setColumns(10);
		
		Role_txt = new JTextField();
		Role_txt.setBounds(565, 177, 96, 19);
		Jpanel.add(Role_txt);
		Role_txt.setColumns(10);
		
		Contact_details_txt = new JTextField();
		Contact_details_txt.setBounds(565, 300, 96, 19);
		Jpanel.add(Contact_details_txt);
		Contact_details_txt.setColumns(10);
		
		JComboBox Status_txt = new JComboBox();
		Status_txt.setModel(new DefaultComboBoxModel(new String[] {"active", "injured", "MIA", "KIA"}));
		Status_txt.setBounds(565, 237, 96, 21);
		Jpanel.add(Status_txt);
		
		JButton btnNewButton = new JButton("Back to Dashboard");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WarManagement dashboard=new WarManagement();
				dashboard.setVisible(true);
				dispose();
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		btnNewButton.setBackground(new Color(0, 0, 0));
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setBounds(10, 380, 228, 47);
		Jpanel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Reset");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshTextFields();
			}

			private void refreshTextFields() {
				Personnel_ID_txt.setText("");
				Fname_txt.setText("");
				Lname_txt.setText("");
				Post_txt.setText("");
				Unit_ID_txt.setText("");
				Role_txt.setText("");
				
				Contact_details_txt.setText("");
				
			}
			
		});
		btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		btnNewButton_1.setBackground(new Color(0, 0, 0));
		btnNewButton_1.setForeground(new Color(255, 255, 255));
		btnNewButton_1.setBounds(313, 380, 145, 47);
		Jpanel.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Sign In");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PersonnelInput();
				
			}
			private void PersonnelInput()
			{
				 String personnelId = Personnel_ID_txt.getText();
			        String firstName = Fname_txt.getText();
			        String lastName = Lname_txt.getText();
			        String post = Post_txt.getText();
			        String unitId = Unit_ID_txt.getText();
			        String role = Role_txt.getText();
			        String status = (String) Status_txt.getSelectedItem();
			        String contact_information = Contact_details_txt.getText();

			        if (isAnyFieldEmpty(personnelId, firstName, lastName, post, unitId, role, status, contact_information)) {
			            JOptionPane.showMessageDialog(Personnel_SignUp.this, "All fields must be filled.");
			            return;
			        }

			        String url = "jdbc:mysql://localhost:3306/war";
			        String user = "root"; // Replace with your MySQL username
			        String password = "PASSWORD"; // Replace with your MySQL password

			        String sql = "INSERT INTO Personnel (Personnel_id, First_name, Last_name, Post, Unit_Id, Role, Status, contact_information) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

			        try (Connection conn = DriverManager.getConnection(url, user, password);
			             PreparedStatement pstmt = conn.prepareStatement(sql)) {

			            pstmt.setString(1, personnelId);
			            pstmt.setString(2, firstName);
			            pstmt.setString(3, lastName);
			            pstmt.setString(4, post);
			            pstmt.setString(5, unitId);
			            pstmt.setString(6, role);
			            pstmt.setString(7, status);
			            pstmt.setString(8, contact_information);

			            int rowsAffected = pstmt.executeUpdate();
			            if (rowsAffected > 0) {
			                JOptionPane.showMessageDialog(Personnel_SignUp.this, "Personnel details inserted successfully.");
			                Personnel_details personnelDetails=new Personnel_details();
							personnelDetails.setVisible(true);
							dispose();
			                
			            } else {
			                JOptionPane.showMessageDialog(Personnel_SignUp.this, "Failed to insert personnel details.");
			            }
			        } catch (SQLException ex) {
			            ex.printStackTrace();
			            JOptionPane.showMessageDialog(Personnel_SignUp.this, "Error: " + ex.getMessage());
			        }
			    }

			    private boolean isAnyFieldEmpty(String... fields) {
			        for (String field : fields) {
			            if (field == null || field.trim().isEmpty()) {
			                return true;
			            }
			        }
			        return false;
			    }
			
		});
		
	
		
		
		btnNewButton_2.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		btnNewButton_2.setBackground(new Color(0, 0, 0));
		btnNewButton_2.setForeground(new Color(255, 255, 255));
		btnNewButton_2.setBounds(549, 380, 145, 47);
		Jpanel.add(btnNewButton_2);
		
		
		
	}
	
}

