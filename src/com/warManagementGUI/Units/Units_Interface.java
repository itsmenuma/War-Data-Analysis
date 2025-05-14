package com.warManagementGUI.Units;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

import com.warManagementGUI.WarManagement;

import javax.swing.JScrollPane;

public class Units_Interface extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField unit_id_txt;
	private JTextField unit_name_txt;
	private JTextField commander_ID_txt;
	private JTextField Location_ID_txt;
	private JTable Units_table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Units_Interface frame = new Units_Interface();
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Units_Interface() {
		setTitle("Units");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 773, 529);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(255, 255, 255));
		contentPane.setBackground(new Color(0, 64, 64));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Units Information");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50));
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(30, 11, 390, 103);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Unit ID");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setBounds(30, 124, 96, 25);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Unit Name");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_2.setForeground(new Color(255, 255, 255));
		lblNewLabel_2.setBounds(30, 175, 96, 25);
		contentPane.add(lblNewLabel_2);
		
		unit_id_txt = new JTextField();
		unit_id_txt.setBounds(174, 124, 86, 20);
		contentPane.add(unit_id_txt);
		unit_id_txt.setColumns(10);
		
		unit_name_txt = new JTextField();
		unit_name_txt.setBounds(174, 180, 86, 20);
		contentPane.add(unit_name_txt);
		unit_name_txt.setColumns(10);
		
		JButton btnNewButton = new JButton("Back to Dashboard");
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		btnNewButton.setBackground(new Color(0, 0, 0));
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WarManagement dashboard=new WarManagement();
				dashboard.setVisible(true);
				dispose();
			}
		});
		btnNewButton.setBounds(10, 418, 207, 44);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_2 = new JButton("Refresh");
		btnNewButton_2.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshTextFields();
			}

			private void refreshTextFields() {
				unit_id_txt.setText("");
				unit_name_txt.setText("");
				commander_ID_txt.setText("");
				Location_ID_txt.setText("");
				
				
			}
		});
		btnNewButton_2.setBackground(new Color(0, 0, 0));
		btnNewButton_2.setForeground(new Color(255, 255, 255));
		btnNewButton_2.setBounds(238, 418, 104, 44);
		contentPane.add(btnNewButton_2);
		
		JLabel lblNewLabel_3 = new JLabel("Unit Type");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_3.setForeground(new Color(255, 255, 255));
		lblNewLabel_3.setBackground(new Color(0, 0, 0));
		lblNewLabel_3.setBounds(30, 233, 96, 25);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Commander ID");
		lblNewLabel_4.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_4.setForeground(new Color(255, 255, 255));
		lblNewLabel_4.setBackground(new Color(0, 0, 0));
		lblNewLabel_4.setBounds(30, 276, 133, 34);
		contentPane.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("Location ID");
		lblNewLabel_5.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_5.setForeground(new Color(255, 255, 255));
		lblNewLabel_5.setBackground(new Color(0, 0, 0));
		lblNewLabel_5.setBounds(30, 343, 111, 25);
		contentPane.add(lblNewLabel_5);
		
		commander_ID_txt = new JTextField();
		commander_ID_txt.setBounds(174, 286, 96, 19);
		contentPane.add(commander_ID_txt);
		commander_ID_txt.setColumns(10);
		
		Location_ID_txt = new JTextField();
		Location_ID_txt.setBounds(174, 352, 96, 19);
		contentPane.add(Location_ID_txt);
		Location_ID_txt.setColumns(10);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 10));
		comboBox.setBackground(new Color(255, 255, 255));
		comboBox.setForeground(new Color(0, 0, 0));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"infantry", "cavalry", "artillery"}));
		comboBox.setBounds(174, 237, 86, 21);
		contentPane.add(comboBox);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(297, 110, 452, 258);
		contentPane.add(scrollPane);
		
		Units_table = new JTable();
		scrollPane.setViewportView(Units_table);
		Units_table.setBackground(new Color(0, 0, 0));
		Units_table.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 10));
		Units_table.setForeground(new Color(255, 255, 255));
		Units_table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Unit_ID", "Unit_Name", "Unit_type", "Commander_id", "location_id"
			}
		));
		
		JButton btnNewButton_3 = new JButton("Insert");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String url = "jdbc:mysql://localhost:3306/war";
		        String user = "root";
		        String password = "rayees@123";

		        String insertQuery = "INSERT INTO units (unit_id, unit_name, unit_type, commander_id, location_id) VALUES (?, ?, ?, ?, ?)";
		        try (Connection conn = DriverManager.getConnection(url, user, password);
		                PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

		               pstmt.setString(1, unit_id_txt.getText());
		               pstmt.setString(2, unit_name_txt.getText());
		               pstmt.setString(3, (String) comboBox.getSelectedItem());
		               pstmt.setString(4, commander_ID_txt.getText());
		               pstmt.setString(5, Location_ID_txt.getText());

		               int rowsAffected = pstmt.executeUpdate();

		               if (rowsAffected > 0) {
		                   JOptionPane.showMessageDialog(Units_Interface.this, "Insert successful.");
		                   refreshTextFields();
		                   refreshTableData();
		                   // Optionally refresh the table or display updated data
		               } else {
		                   JOptionPane.showMessageDialog(Units_Interface.this, "Insert failed.");
		               }

		           } catch (SQLException e1) {
		               e1.printStackTrace();
		               JOptionPane.showMessageDialog(Units_Interface.this, "Error: " + e1.getMessage());
		           }
		        
			}
			private void refreshTextFields() {
				unit_id_txt.setText("");
				unit_name_txt.setText("");
				commander_ID_txt.setText("");
				Location_ID_txt.setText("");
				
				
			}
			private void refreshTableData() {
			    DefaultTableModel model = (DefaultTableModel) Units_table.getModel();
			    model.setRowCount(0); // Clear existing data

			    String url = "jdbc:mysql://localhost:3306/war";
			    String user = "root";
			    String password = "rayees@123";

			    String selectQuery = "SELECT * FROM units";

			    try (Connection conn = DriverManager.getConnection(url, user, password);
			         PreparedStatement pstmt = conn.prepareStatement(selectQuery);
			         ResultSet rs = pstmt.executeQuery()) {

			        while (rs.next()) {
			            Object[] row = {
			                rs.getString("unit_id"),
			                rs.getString("unit_name"),
			                rs.getString("unit_type"),
			                rs.getString("commander_id"),
			                rs.getString("location_id")
			            };
			            model.addRow(row);
			        }

			    } catch (SQLException e) {
			        e.printStackTrace();
			        JOptionPane.showMessageDialog(Units_Interface.this, "Error: " + e.getMessage());
			    }
			}
		});
		
		btnNewButton_3.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		btnNewButton_3.setBackground(new Color(0, 0, 0));
		btnNewButton_3.setForeground(new Color(255, 255, 255));
		btnNewButton_3.setBounds(365, 418, 85, 44);
		contentPane.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Update");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 String url = "jdbc:mysql://localhost:3306/war";
			        String user = "root";
			        String password = "rayees@123";

			        String updateQuery = "UPDATE units SET unit_name = ?, unit_type = ?, commander_id = ?, location_id = ? WHERE unit_id = ?";
			        
			        try (Connection conn = DriverManager.getConnection(url, user, password);
			             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

			            pstmt.setString(1, unit_name_txt.getText());
			            pstmt.setString(2, (String) comboBox.getSelectedItem());
			            pstmt.setString(3, commander_ID_txt.getText());
			            pstmt.setString(4, Location_ID_txt.getText());
			            pstmt.setString(5, unit_id_txt.getText());

			            int rowsAffected = pstmt.executeUpdate();

			            if (rowsAffected > 0) {
			                JOptionPane.showMessageDialog(Units_Interface.this, "Update successful.");
			                refreshTextFields();
			                refreshTableData(); // Refresh the table data to show updates
			            } else {
			                JOptionPane.showMessageDialog(Units_Interface.this, "Update failed.");
			            }

			        } catch (SQLException e1) {
			            e1.printStackTrace();
			            JOptionPane.showMessageDialog(Units_Interface.this, "Error: " + e1.getMessage());
			        }
			        
			}

			private void refreshTextFields() {
				unit_id_txt.setText("");
				unit_name_txt.setText("");
				commander_ID_txt.setText("");
				Location_ID_txt.setText("");
				
			}

			private void refreshTableData() {
				 DefaultTableModel model = (DefaultTableModel) Units_table.getModel();
				    model.setRowCount(0); // Clear existing data

				    String url = "jdbc:mysql://localhost:3306/war";
				    String user = "root";
				    String password = "rayees@123";

				    String selectQuery = "SELECT * FROM units";

				    try (Connection conn = DriverManager.getConnection(url, user, password);
				         PreparedStatement pstmt = conn.prepareStatement(selectQuery);
				         ResultSet rs = pstmt.executeQuery()) {

				        while (rs.next()) {
				            Object[] row = {
				                rs.getString("unit_id"),
				                rs.getString("unit_name"),
				                rs.getString("unit_type"),
				                rs.getString("commander_id"),
				                rs.getString("location_id")
				            };
				            model.addRow(row);
				        }

				    } catch (SQLException e) {
				        e.printStackTrace();
				        JOptionPane.showMessageDialog(Units_Interface.this, "Error: " + e.getMessage());
				    }
				
			}
			
			
		});
		btnNewButton_4.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		btnNewButton_4.setBackground(new Color(0, 0, 0));
		btnNewButton_4.setForeground(new Color(255, 255, 255));
		btnNewButton_4.setBounds(477, 418, 117, 44);
		contentPane.add(btnNewButton_4);
		
		JButton btnNewButton_5 = new JButton("Delete");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String url = "jdbc:mysql://localhost:3306/war";
		        String user = "root";
		        String password = "rayees@123";

		        String deleteQuery = "DELETE FROM units WHERE unit_id = ?";
		        
		        try (Connection conn = DriverManager.getConnection(url, user, password);
		             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

		            pstmt.setString(1, unit_id_txt.getText());

		            int rowsAffected = pstmt.executeUpdate();

		            if (rowsAffected > 0) {
		                JOptionPane.showMessageDialog(Units_Interface.this, "Delete successful.");
		                refreshTextFields();
		                refreshTableData(); // Refresh the table data to show deletions
		            } else {
		                JOptionPane.showMessageDialog(Units_Interface.this, "Delete failed.");
		            }

		        } catch (SQLException e1) {
		            e1.printStackTrace();
		            JOptionPane.showMessageDialog(Units_Interface.this, "Error: " + e1.getMessage());
		        }
			}
		});
		btnNewButton_5.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		btnNewButton_5.setBackground(new Color(0, 0, 0));
		btnNewButton_5.setForeground(new Color(255, 255, 255));
		btnNewButton_5.setBounds(616, 418, 133, 44);
		contentPane.add(btnNewButton_5);
	}

	protected void refreshTableData() {
		unit_id_txt.setText("");
		unit_name_txt.setText("");
		commander_ID_txt.setText("");
		Location_ID_txt.setText("");
	}

	protected void refreshTextFields() {
		DefaultTableModel model = (DefaultTableModel) Units_table.getModel();
	    model.setRowCount(0); // Clear existing data

	    String url = "jdbc:mysql://localhost:3306/war";
	    String user = "root";
	    String password = "rayees@123";

	    String selectQuery = "SELECT * FROM units";

	    try (Connection conn = DriverManager.getConnection(url, user, password);
	         PreparedStatement pstmt = conn.prepareStatement(selectQuery);
	         ResultSet rs = pstmt.executeQuery()) {

	        while (rs.next()) {
	            Object[] row = {
	                rs.getString("unit_id"),
	                rs.getString("unit_name"),
	                rs.getString("unit_type"),
	                rs.getString("commander_id"),
	                rs.getString("location_id")
	            };
	            model.addRow(row);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(Units_Interface.this, "Error: " + e.getMessage());
	    }
		
	}
}
