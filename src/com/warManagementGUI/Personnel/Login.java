package com.warManagementGUI.Personnel;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.warManagementGUI.WarManagement;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField Personnel_ID_txt;
	private JTextField FirstName_txt;
	private JTextField Role_txt;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
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
	public Login() {
		setTitle("Personnel_Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 773, 529);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(255, 255, 255));
		contentPane.setBackground(new Color(0, 64, 64));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Login.class.getResource("/pics/login.jpeg")));
		lblNewLabel.setBounds(37, 82, 169, 265);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Personnel Login Page");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50));
		lblNewLabel_1.setBackground(new Color(0, 0, 0));
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setBounds(91, 24, 622, 41);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Personnel ID");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_2.setForeground(new Color(255, 255, 255));
		lblNewLabel_2.setBounds(272, 137, 128, 53);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("First  Name");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_3.setForeground(new Color(255, 255, 255));
		lblNewLabel_3.setBounds(272, 201, 128, 53);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Role");
		lblNewLabel_4.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_4.setForeground(new Color(255, 255, 255));
		lblNewLabel_4.setBounds(284, 265, 116, 53);
		contentPane.add(lblNewLabel_4);
		
		Personnel_ID_txt = new JTextField();
		Personnel_ID_txt.setBounds(473, 155, 150, 20);
		contentPane.add(Personnel_ID_txt);
		Personnel_ID_txt.setColumns(10);
		
		FirstName_txt = new JTextField();
		FirstName_txt.setBounds(473, 219, 150, 20);
		contentPane.add(FirstName_txt);
		FirstName_txt.setColumns(10);
		
		Role_txt = new JTextField();
		Role_txt.setBounds(473, 283, 155, 20);
		contentPane.add(Role_txt);
		Role_txt.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("login");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 String personnelId = Personnel_ID_txt.getText();
	                String firstName = FirstName_txt.getText();
	                String role = Role_txt.getText();
	                
	                if (checkCredentials(personnelId, firstName, role)) {
	                    Personnel_details personnelDetails = new Personnel_details();
	                    personnelDetails.setVisible(true);
	                    dispose();
	                } else {
	                    JOptionPane.showMessageDialog(contentPane, "Invalid credentials. Please try again.");
	                }
				/*Personnel_details personnelDetails=new Personnel_details();
				personnelDetails.setVisible(true);
				dispose();*/
			}
		});
		btnNewButton_1.setBackground(new Color(0, 0, 0));
		btnNewButton_1.setForeground(new Color(255, 255, 255));
		btnNewButton_1.setBounds(600, 420, 89, 41);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("remove");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Personnel_Remove_Details PersonnelRemove=new Personnel_Remove_Details();
				PersonnelRemove.setVisible(true);
				dispose();
			}
		});
		btnNewButton_2.setForeground(new Color(255, 255, 255));
		btnNewButton_2.setBackground(new Color(0, 0, 0));
		btnNewButton_2.setBounds(355, 420, 89, 41);
		contentPane.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("update");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Personnel_Update personnelUpdate=new Personnel_Update();
				personnelUpdate.setVisible(true);
				dispose();
			}
		});
		btnNewButton_3.setBackground(new Color(0, 0, 0));
		btnNewButton_3.setForeground(new Color(255, 255, 255));
		btnNewButton_3.setBounds(473, 420, 89, 41);
		contentPane.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Back to Dashboard");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WarManagement dashboard=new WarManagement();
				dashboard.setVisible(true);
				dispose();
			}
		});
		btnNewButton_4.setForeground(new Color(255, 255, 255));
		btnNewButton_4.setBackground(new Color(0, 0, 0));
		btnNewButton_4.setBounds(37, 420, 150, 41);
		contentPane.add(btnNewButton_4);
		
		 JButton btnNewButton = new JButton("Sign Up");
	        btnNewButton.addActionListener(e -> {
	            Personnel_SignUp personnelSignUp = new Personnel_SignUp();
	            personnelSignUp.setVisible(true);
	            dispose();
	        });
	        btnNewButton.setBackground(new Color(0, 0, 0));
	        btnNewButton.setForeground(new Color(255, 255, 255));
	        btnNewButton.setBounds(211, 420, 104, 41);
	        contentPane.add(btnNewButton);
	    }

	    private boolean checkCredentials(String personnelId, String firstName, String role) {
	        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/war", "root", "rayees@123");
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery("SELECT * FROM Personnel WHERE Personnel_id='" + personnelId + "' AND First_name='" + firstName + "' AND Role='" + role + "'")) {

	            return rs.next(); // If there's a result, credentials are valid

	        } catch (Exception e) {
	            e.printStackTrace();
	            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
	        }
	        return false;
	    }
	}