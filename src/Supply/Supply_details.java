package Supply;

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
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

import DataAnalysis.SuppliesBarChart;
import DataAnalysis.SuppliesDataSet;
import warManagement.WarManagement;

import javax.swing.JScrollPane;

public class Supply_details extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField supply_ID_txt;
	private JTextField supply_name_txt;
	private JTextField Name_txt;
	private JTextField Quantity_txt;
	private JTextField Unit_ID_txt;
	private JTextField loc_ID_txt;
	private JTable Supply_table;
	private JComboBox<String>Status_txt;
	
	 private static final String url = "jdbc:mysql://localhost:3306/war";
	    private static final String user = "root";
	    private static final String password = "rayees@123";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Supply_details frame = new Supply_details();
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

	public Supply_details() {
		setTitle("Supplies");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 773, 529);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 64, 64));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Supply Details");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 35));
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(10, 11, 222, 51);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Supply ID");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setBounds(10, 72, 112, 44);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Supply type");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_2.setForeground(new Color(255, 255, 255));
		lblNewLabel_2.setBounds(10, 164, 112, 44);
		contentPane.add(lblNewLabel_2);
		
		supply_ID_txt = new JTextField();
		supply_ID_txt.setBounds(152, 87, 96, 19);
		contentPane.add(supply_ID_txt);
		supply_ID_txt.setColumns(10);
		
		supply_name_txt = new JTextField();
		supply_name_txt.setBounds(152, 179, 96, 19);
		contentPane.add(supply_name_txt);
		supply_name_txt.setColumns(10);
		
		 Status_txt = new JComboBox<String>();
		    Status_txt.setModel(new DefaultComboBoxModel<String>(new String[] {"Available", "In Use", "Out of Stock"}));
		    Status_txt.setBounds(152, 364, 96, 21);
		    contentPane.add(Status_txt);
		
		JButton btnNewButton = new JButton("Refresh");
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshTextFields();
			}

			
		});
		btnNewButton.setBackground(new Color(0, 0, 0));
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setBounds(183, 429, 82, 51);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Back to Dashboard");
		btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WarManagement dashboard=new WarManagement();
				dashboard.setVisible(true);
				dispose();
			}
		});
		btnNewButton_1.setBackground(new Color(0, 0, 0));
		btnNewButton_1.setForeground(new Color(255, 255, 255));
		btnNewButton_1.setBounds(10, 429, 163, 51);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Delete");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSupply();
			}
		});
		btnNewButton_2.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
		btnNewButton_2.setBackground(new Color(0, 0, 0));
		btnNewButton_2.setForeground(new Color(255, 255, 255));
		btnNewButton_2.setBounds(486, 429, 101, 51);
		contentPane.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Insert");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insertSupply();
			}
		});
		btnNewButton_3.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
		btnNewButton_3.setForeground(new Color(255, 255, 255));
		btnNewButton_3.setBackground(new Color(0, 0, 0));
		btnNewButton_3.setBounds(275, 429, 82, 51);
		contentPane.add(btnNewButton_3);
		
		JButton btnNewButton_5 = new JButton("Update");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateSupply();
			}
		});
		btnNewButton_5.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
		btnNewButton_5.setForeground(new Color(255, 255, 255));
		btnNewButton_5.setBackground(new Color(0, 0, 0));
		btnNewButton_5.setBounds(367, 429, 96, 51);
		contentPane.add(btnNewButton_5);
		
		JLabel lblNewLabel_3 = new JLabel("Name");
		lblNewLabel_3.setBackground(new Color(0, 0, 0));
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_3.setForeground(new Color(255, 255, 255));
		lblNewLabel_3.setBounds(10, 130, 112, 13);
		contentPane.add(lblNewLabel_3);
		
		Name_txt = new JTextField();
		Name_txt.setBounds(152, 129, 96, 19);
		contentPane.add(Name_txt);
		Name_txt.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("Quantity");
		lblNewLabel_4.setBackground(new Color(0, 0, 0));
		lblNewLabel_4.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_4.setForeground(new Color(255, 255, 255));
		lblNewLabel_4.setBounds(10, 218, 82, 31);
		contentPane.add(lblNewLabel_4);
		
		Quantity_txt = new JTextField();
		Quantity_txt.setBounds(152, 226, 96, 19);
		contentPane.add(Quantity_txt);
		Quantity_txt.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("Unit ID");
		lblNewLabel_5.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_5.setBackground(new Color(0, 0, 0));
		lblNewLabel_5.setForeground(new Color(255, 255, 255));
		lblNewLabel_5.setBounds(10, 259, 89, 33);
		contentPane.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("Location ID");
		lblNewLabel_6.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_6.setBackground(new Color(0, 0, 0));
		lblNewLabel_6.setForeground(new Color(255, 255, 255));
		lblNewLabel_6.setBounds(10, 316, 110, 31);
		contentPane.add(lblNewLabel_6);
		
		JLabel lblNewLabel_7 = new JLabel("Status");
		lblNewLabel_7.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_7.setBackground(new Color(0, 0, 0));
		lblNewLabel_7.setForeground(new Color(255, 255, 255));
		lblNewLabel_7.setBounds(10, 357, 89, 31);
		contentPane.add(lblNewLabel_7);
		
		Unit_ID_txt = new JTextField();
		Unit_ID_txt.setBounds(152, 268, 96, 19);
		contentPane.add(Unit_ID_txt);
		Unit_ID_txt.setColumns(10);
		
		loc_ID_txt = new JTextField();
		loc_ID_txt.setBounds(152, 324, 96, 19);
		contentPane.add(loc_ID_txt);
		loc_ID_txt.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(278, 23, 471, 379);
		contentPane.add(scrollPane);
		
		Supply_table = new JTable();
		scrollPane.setViewportView(Supply_table);
		Supply_table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Supply_ID", "Name", "Type", "Quantity", "Unit_ID", "Location_ID", "Status"
			}
		));
		Supply_table.setForeground(new Color(255, 255, 255));
		Supply_table.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 10));
		Supply_table.setBackground(new Color(0, 0, 0));
		
		JButton btnNewButton_4 = new JButton("Analyse");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Map<String, Integer> data = SuppliesDataSet.getPersonnelStatusCount();
                // Create a new frame for the bar chart
                JFrame chartFrame = new JFrame("Personnel Status Count");
                chartFrame.setSize(800, 600);
                chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                chartFrame.getContentPane().add(new SuppliesBarChart(data, "Supplies Status Count"));
                chartFrame.setVisible(true);
                // Dispose the current frame
                Supply_details.this.dispose();
			}
		});
		btnNewButton_4.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
		btnNewButton_4.setBackground(new Color(0, 0, 0));
		btnNewButton_4.setForeground(new Color(255, 255, 255));
		btnNewButton_4.setBounds(605, 428, 132, 53);
		contentPane.add(btnNewButton_4);
		
		
	}

	protected void updateSupply() {
		// Check if supply_ID_txt is empty
	    if (supply_ID_txt.getText().isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Please enter a Supply ID to update.", "Error", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    String updateQuery = "UPDATE Supplies SET name = ?, type = ?, quantity = ?, unit_id = ?, location_id = ?, status = ? WHERE supply_id = ?";

	    try (Connection conn = DriverManager.getConnection(url, user, password);
	         PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

	        pstmt.setString(1, supply_name_txt.getText());
	        pstmt.setString(2, Name_txt.getText());
	        pstmt.setInt(3, Integer.parseInt(Quantity_txt.getText()));
	        pstmt.setInt(4, Integer.parseInt(Unit_ID_txt.getText()));
	        pstmt.setInt(5, Integer.parseInt(loc_ID_txt.getText()));

	        // Convert selected item to String explicitly
	        String status = (String) Status_txt.getSelectedItem();
	        pstmt.setString(6, status);

	        pstmt.setInt(7, Integer.parseInt(supply_ID_txt.getText()));

	        int rowsAffected = pstmt.executeUpdate();

	        if (rowsAffected > 0) {
	            JOptionPane.showMessageDialog(this, "Supply updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
	            refreshTextFields();
	            loadSupplyData(); // Refresh table after update
	        } else {
	            JOptionPane.showMessageDialog(this, "Failed to update supply. Supply ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
	        }

	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    } catch (NumberFormatException ex) {
	        JOptionPane.showMessageDialog(this, "Please enter valid numeric values for ID, Quantity, Unit ID, and Location ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

	protected void deleteSupply() {
		 // Check if supply_ID_txt is empty
	    if (supply_ID_txt.getText().isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Please enter a Supply ID to delete.", "Error", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    String deleteQuery = "DELETE FROM Supplies WHERE supply_id = ?";

	    try (Connection conn = DriverManager.getConnection(url, user, password);
	         PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

	        pstmt.setInt(1, Integer.parseInt(supply_ID_txt.getText()));

	        int rowsAffected = pstmt.executeUpdate();

	        if (rowsAffected > 0) {
	            JOptionPane.showMessageDialog(this, "Supply deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
	            refreshTextFields();
	            loadSupplyData(); // Refresh table after deletion
	        } else {
	            JOptionPane.showMessageDialog(this, "Failed to delete supply. Supply ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
	        }

	    } catch (NumberFormatException ex) {
	        JOptionPane.showMessageDialog(this, "Please enter a valid numeric Supply ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

	protected void insertSupply() {
		 String insertQuery = "INSERT INTO Supplies (supply_id, name, type, quantity, unit_id, location_id, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

		    try (Connection conn = DriverManager.getConnection(url, user, password);
		         PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

		        pstmt.setInt(1, Integer.parseInt(supply_ID_txt.getText()));
		        pstmt.setString(2, supply_name_txt.getText());
		        pstmt.setString(3, Name_txt.getText());
		        pstmt.setInt(4, Integer.parseInt(Quantity_txt.getText()));
		        pstmt.setInt(5, Integer.parseInt(Unit_ID_txt.getText()));
		        pstmt.setInt(6, Integer.parseInt(loc_ID_txt.getText()));

		        // Convert selected item to String explicitly
		        String status = (String) Status_txt.getSelectedItem();
		        pstmt.setString(7, status);

		        int rowsAffected = pstmt.executeUpdate();

		        if (rowsAffected > 0) {
		            JOptionPane.showMessageDialog(this, "Supply inserted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
		            refreshTextFields();
		            loadSupplyData(); // Refresh table after insertion
		        } else {
		            JOptionPane.showMessageDialog(this, "Failed to insert supply.", "Error", JOptionPane.ERROR_MESSAGE);
		        }

		    } catch (SQLException ex) {
		        ex.printStackTrace();
		        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		    } catch (NumberFormatException ex) {
		        JOptionPane.showMessageDialog(this, "Please enter valid numeric values for ID, Quantity, Unit ID, and Location ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
		    }
	}

	private void loadSupplyData() {
		 String query = "SELECT supply_id, name, type, quantity, unit_id, location_id, status FROM Supplies";
	        
	        try (Connection conn = DriverManager.getConnection(url, user, password);
	             PreparedStatement pstmt = conn.prepareStatement(query);
	             ResultSet rs = pstmt.executeQuery()) {

	            // Clear existing table data
	            DefaultTableModel model = (DefaultTableModel) Supply_table.getModel();
	            model.setRowCount(0);

	            // Iterate through the result set and populate the table
	            while (rs.next()) {
	                int supply_id = rs.getInt("supply_id");
	                String name = rs.getString("name");
	                String type = rs.getString("type");
	                int quantity = rs.getInt("quantity");
	                int unit_id = rs.getInt("unit_id");
	                int location_id = rs.getInt("location_id");
	                String status = rs.getString("status");

	                // Add row to table model
	                Object[] row = { supply_id, name, type, quantity, unit_id, location_id, status };
	                model.addRow(row);
	            }

	        } catch (SQLException ex) {
	            ex.printStackTrace();
	            JOptionPane.showMessageDialog(this, "Error loading supply data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        }
	}

	protected void refreshTextFields() {
		supply_ID_txt.setText("");
		supply_name_txt.setText("");
		Name_txt.setText("");
		Quantity_txt.setText("");
		Unit_ID_txt.setText("");
		loc_ID_txt.setText("");
	}
}
