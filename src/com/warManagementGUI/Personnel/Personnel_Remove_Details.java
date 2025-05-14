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
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class Personnel_Remove_Details extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField Personnel_ID_txt;
	private JTextField Fname_txt;
	private JTextField Unit_ID_txt;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Personnel_Remove_Details frame = new Personnel_Remove_Details();
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
	public Personnel_Remove_Details() {
		setTitle("Remove Personnel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 773, 529);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 64, 64));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Remove Personnel Detetails");
		lblNewLabel.setBackground(new Color(0, 0, 0));
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50));
		lblNewLabel.setBounds(21, 10, 593, 123);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Personnel ID");
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_1.setBackground(new Color(0, 0, 0));
		lblNewLabel_1.setBounds(33, 154, 133, 50);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("First Name");
		lblNewLabel_2.setForeground(new Color(255, 255, 255));
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_2.setBackground(new Color(0, 0, 0));
		lblNewLabel_2.setBounds(33, 226, 119, 45);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Unit ID");
		lblNewLabel_3.setForeground(new Color(255, 255, 255));
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_3.setBackground(new Color(0, 0, 0));
		lblNewLabel_3.setBounds(33, 310, 77, 45);
		contentPane.add(lblNewLabel_3);
		
		Personnel_ID_txt = new JTextField();
		Personnel_ID_txt.setBounds(199, 172, 96, 19);
		contentPane.add(Personnel_ID_txt);
		Personnel_ID_txt.setColumns(10);
		
		Fname_txt = new JTextField();
		Fname_txt.setBounds(199, 241, 96, 19);
		contentPane.add(Fname_txt);
		Fname_txt.setColumns(10);
		
		Unit_ID_txt = new JTextField();
		Unit_ID_txt.setBounds(199, 310, 96, 19);
		contentPane.add(Unit_ID_txt);
		Unit_ID_txt.setColumns(10);
		
		JButton btnNewButton = new JButton("Back to Dashboard");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WarManagement dashboard=new WarManagement();
				dashboard.setVisible(true);
				dispose();
			}
		});
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		btnNewButton.setBackground(new Color(0, 0, 0));
		btnNewButton.setBounds(21, 397, 213, 50);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Reset");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}

			private void reset() {
				Personnel_ID_txt.setText("");
				Fname_txt.setText("");
				Unit_ID_txt.setText("");
			}
		});
		btnNewButton_1.setForeground(new Color(255, 255, 255));
		btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		btnNewButton_1.setBackground(new Color(0, 0, 0));
		btnNewButton_1.setBounds(334, 397, 133, 50);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Remove");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RemoveDetails();
			}

			private void RemoveDetails() {
				String personnelId = Personnel_ID_txt.getText();
		        String firstName = Fname_txt.getText();
		        String unitId = Unit_ID_txt.getText();

		        String url = "jdbc:mysql://localhost:3306/war";
		        String user = "root"; // Replace with your MySQL username
		        String password = "rayees@123"; // Replace with your MySQL password

		        StringBuilder sql = new StringBuilder("DELETE FROM Personnel WHERE 1=1");
		        if (!personnelId.isEmpty()) sql.append(" AND Personnel_id = ?");
		        if (!firstName.isEmpty()) sql.append(" AND First_name = ?");
		        if (!unitId.isEmpty()) sql.append(" AND Unit_Id = ?");

		        try (Connection conn = DriverManager.getConnection(url, user, password);
		             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

		            int index = 1;
		            if (!personnelId.isEmpty()) pstmt.setString(index++, personnelId);
		            if (!firstName.isEmpty()) pstmt.setString(index++, firstName);
		            if (!unitId.isEmpty()) pstmt.setString(index++, unitId);

		            int rowsAffected = pstmt.executeUpdate();
		            if (rowsAffected > 0) {
		                JOptionPane.showMessageDialog(Personnel_Remove_Details.this, "Personnel details removed successfully.");
		                Personnel_details personnelDetails = new Personnel_details();
	                    personnelDetails.setVisible(true);
	                    dispose();
		            } else {
		                JOptionPane.showMessageDialog(Personnel_Remove_Details.this, "No matching personnel found.");
		            }
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		            JOptionPane.showMessageDialog(Personnel_Remove_Details.this, "Error: " + ex.getMessage());
		        }
		    
				
			}
		});
		btnNewButton_2.setForeground(new Color(255, 255, 255));
		btnNewButton_2.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		btnNewButton_2.setBackground(new Color(0, 0, 0));
		btnNewButton_2.setBounds(558, 397, 133, 50);
		contentPane.add(btnNewButton_2);
	}

}
