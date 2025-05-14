package Equipment;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import DataAnalysis.EquipmentBarChart;
import DataAnalysis.Equipment_database;
import warManagement.WarManagement;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Map;
import java.util.Vector;

public class Equipment_details extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField equip_ID_txt;
    private JTextField equip_name_txt;
    private JTextField Unit_ID_txt;
    private JTextField location_ID_txt;
    private JTable Equipment_table;
    private JComboBox<String> Status_txt;
    private JComboBox<String> equip_type_txt;
    private static final String url = "jdbc:mysql://localhost:3306/war";
    private static final String user = "root";
    private static final String password = "PASSWORD";

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Equipment_details frame = new Equipment_details();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Equipment_details() {
        setTitle("Equipment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 773, 529);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 64, 64));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Equipment Details");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 35));
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setBounds(27, 10, 275, 83);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Equipment ID");
        lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
        lblNewLabel_1.setForeground(new Color(255, 255, 255));
        lblNewLabel_1.setBounds(27, 90, 155, 20);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("Name");
        lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
        lblNewLabel_2.setForeground(new Color(255, 255, 255));
        lblNewLabel_2.setBounds(27, 132, 69, 28);
        contentPane.add(lblNewLabel_2);

        equip_ID_txt = new JTextField();
        equip_ID_txt.setBounds(192, 93, 86, 20);
        contentPane.add(equip_ID_txt);
        equip_ID_txt.setColumns(10);

        equip_name_txt = new JTextField();
        equip_name_txt.setBounds(192, 132, 86, 20);
        contentPane.add(equip_name_txt);
        equip_name_txt.setColumns(10);

        JButton btnNewButton = new JButton("Refresh");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshTextFields();
            }

			private void refreshTextFields() {
				 equip_ID_txt.setText("");
			        equip_name_txt.setText("");
			        Unit_ID_txt.setText("");
			        location_ID_txt.setText("");
			}
        });
        btnNewButton.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
        btnNewButton.setBackground(new Color(0, 0, 0));
        btnNewButton.setForeground(new Color(255, 255, 255));
        btnNewButton.setBounds(192, 410, 96, 72);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Back to Dashboard");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WarManagement dashboard = new WarManagement();
                dashboard.setVisible(true);
                dispose();
            }
        });
        btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
        btnNewButton_1.setBackground(new Color(0, 0, 0));
        btnNewButton_1.setForeground(new Color(255, 255, 255));
        btnNewButton_1.setBounds(10, 410, 161, 72);
        contentPane.add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("Insert");
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertEquipment();
                loadEquipmentData();
                refreshTextFields();
            }

			private void loadEquipmentData() {
				DefaultTableModel model = (DefaultTableModel) Equipment_table.getModel();
		          model.setRowCount(0); // Clear table

		          try (Connection conn = DriverManager.getConnection(url, user, password)) {
		              String query = "SELECT * FROM Equipment";
		              PreparedStatement pstmt = conn.prepareStatement(query);
		              ResultSet rs = pstmt.executeQuery();

		              while (rs.next()) {
		                  Vector<Object> row = new Vector<>();
		                  row.add(rs.getInt("equipment_id"));
		                  row.add(rs.getString("name"));
		                  row.add(rs.getString("type"));
		                  row.add(rs.getInt("unit_id"));
		                  row.add(rs.getString("status"));
		                  row.add(rs.getInt("location_id"));
		                  model.addRow(row);
		              }

		          } catch (SQLException e) {
		              e.printStackTrace();
		          }
			}

			private void insertEquipment() {
				 String insertQuery = "INSERT INTO Equipment (equipment_id, name, type, unit_id, status, location_id) VALUES (?, ?, ?, ?, ?, ?)";

				    // Validate input fields
				    if (equip_ID_txt.getText().isEmpty() || equip_name_txt.getText().isEmpty() || Unit_ID_txt.getText().isEmpty()
				            || location_ID_txt.getText().isEmpty()) {
				        JOptionPane.showMessageDialog(Equipment_details.this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
				        return;
				    }

				    try (Connection conn = DriverManager.getConnection(url, user, password);
				         PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

				        pstmt.setInt(1, Integer.parseInt(equip_ID_txt.getText()));
				        pstmt.setString(2, equip_name_txt.getText());
				        pstmt.setString(3, (String) equip_type_txt.getSelectedItem());
				        pstmt.setInt(4, Integer.parseInt(Unit_ID_txt.getText()));
				        pstmt.setString(5, (String) Status_txt.getSelectedItem());
				        pstmt.setInt(6, Integer.parseInt(location_ID_txt.getText()));

				        int rowsAffected = pstmt.executeUpdate();

				        if (rowsAffected > 0) {
				            JOptionPane.showMessageDialog(Equipment_details.this, "Equipment inserted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
				            refreshTextFields();
				            loadEquipmentData();
				        } else {
				            JOptionPane.showMessageDialog(Equipment_details.this, "Failed to insert equipment.", "Error", JOptionPane.ERROR_MESSAGE);
				        }

				    } catch (NumberFormatException e) {
				        JOptionPane.showMessageDialog(Equipment_details.this, "Invalid number format in ID fields.", "Error", JOptionPane.ERROR_MESSAGE);
				    } catch (SQLException e) {
				        e.printStackTrace();
				        JOptionPane.showMessageDialog(Equipment_details.this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				    }
			}

			private void refreshTextFields() {
				 equip_ID_txt.setText("");
			        equip_name_txt.setText("");
			        Unit_ID_txt.setText("");
			        location_ID_txt.setText("");
			}
        });
        btnNewButton_2.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
        btnNewButton_2.setBackground(new Color(0, 0, 0));
        btnNewButton_2.setForeground(new Color(255, 255, 255));
        btnNewButton_2.setBounds(533, 413, 86, 67);
        contentPane.add(btnNewButton_2);

        JLabel lblNewLabel_3 = new JLabel("Type");
        lblNewLabel_3.setForeground(new Color(255, 255, 255));
        lblNewLabel_3.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
        lblNewLabel_3.setBounds(27, 186, 69, 21);
        contentPane.add(lblNewLabel_3);

        JLabel lblNewLabel_4 = new JLabel("Unit ID");
        lblNewLabel_4.setForeground(new Color(255, 255, 255));
        lblNewLabel_4.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
        lblNewLabel_4.setBounds(27, 223, 86, 30);
        contentPane.add(lblNewLabel_4);

        JLabel lblNewLabel_5 = new JLabel("Status");
        lblNewLabel_5.setForeground(new Color(255, 255, 255));
        lblNewLabel_5.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
        lblNewLabel_5.setBounds(27, 274, 75, 28);
        contentPane.add(lblNewLabel_5);

        JLabel lblNewLabel_6 = new JLabel("Location Id");
        lblNewLabel_6.setForeground(new Color(255, 255, 255));
        lblNewLabel_6.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
        lblNewLabel_6.setBounds(27, 321, 127, 28);
        contentPane.add(lblNewLabel_6);

        equip_type_txt = new JComboBox<String>();
        equip_type_txt.setModel(new DefaultComboBoxModel<String>(new String[] {"Weapon", "Vehicle", "Electronic", "Other"}));
        equip_type_txt.setBounds(192, 186, 86, 21);
        contentPane.add(equip_type_txt);

        Unit_ID_txt = new JTextField();
        Unit_ID_txt.setBounds(192, 231, 86, 19);
        contentPane.add(Unit_ID_txt);
        Unit_ID_txt.setColumns(10);

        location_ID_txt = new JTextField();
        location_ID_txt.setBounds(192, 328, 96, 19);
        contentPane.add(location_ID_txt);
        location_ID_txt.setColumns(10);

        Status_txt = new JComboBox<String>();
        Status_txt.setModel(new DefaultComboBoxModel<String>(new String[] {"Operational", "Maintenance", "Decommissioned"}));
        Status_txt.setBounds(192, 280, 86, 21);
        contentPane.add(Status_txt);

        JButton btnNewButton_3 = new JButton("Delete");
        btnNewButton_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	deleteEquipment();
            }

			private void deleteEquipment() {
				 String deleteQuery = "DELETE FROM Equipment WHERE equipment_id = ?";

				    // Validate input fields
				    if (equip_ID_txt.getText().isEmpty()) {
				        JOptionPane.showMessageDialog(Equipment_details.this, "Please enter Equipment ID.", "Error", JOptionPane.ERROR_MESSAGE);
				        return;
				    }

				    int confirm = JOptionPane.showConfirmDialog(Equipment_details.this, "Are you sure you want to delete this equipment?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
				    if (confirm != JOptionPane.YES_OPTION) {
				        return; // User canceled deletion
				    }

				    try (Connection conn = DriverManager.getConnection(url, user, password);
				         PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

				        pstmt.setInt(1, Integer.parseInt(equip_ID_txt.getText()));

				        int rowsAffected = pstmt.executeUpdate();

				        if (rowsAffected > 0) {
				            JOptionPane.showMessageDialog(Equipment_details.this, "Equipment deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
				            loadEquipmentData(); // Refresh table after deletion
				            refreshTextFields();
				        } else {
				            JOptionPane.showMessageDialog(Equipment_details.this, "Failed to delete equipment.", "Error", JOptionPane.ERROR_MESSAGE);
				        }

				    } catch (NumberFormatException e) {
				        JOptionPane.showMessageDialog(Equipment_details.this, "Invalid number format in ID field.", "Error", JOptionPane.ERROR_MESSAGE);
				    } catch (SQLException e) {
				        e.printStackTrace();
				        JOptionPane.showMessageDialog(Equipment_details.this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				    }
			}

			private void refreshTextFields() {
				 equip_ID_txt.setText("");
			        equip_name_txt.setText("");
			        Unit_ID_txt.setText("");
			        location_ID_txt.setText("");
			}

			private void loadEquipmentData() {
				  DefaultTableModel model = (DefaultTableModel) Equipment_table.getModel();
			        model.setRowCount(0); // Clear table

			        try (Connection conn = DriverManager.getConnection(url, user, password)) {
			            String query = "SELECT * FROM Equipment";
			            PreparedStatement pstmt = conn.prepareStatement(query);
			            ResultSet rs = pstmt.executeQuery();

			            while (rs.next()) {
			                Vector<Object> row = new Vector<>();
			                row.add(rs.getInt("equipment_id"));
			                row.add(rs.getString("name"));
			                row.add(rs.getString("type"));
			                row.add(rs.getInt("unit_id"));
			                row.add(rs.getString("status"));
			                row.add(rs.getInt("location_id"));
			                model.addRow(row);
			            }

			        } catch (SQLException e) {
			            e.printStackTrace();
			        }
			}

			
        });
        btnNewButton_3.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
        btnNewButton_3.setForeground(new Color(255, 255, 255));
        btnNewButton_3.setBackground(new Color(0, 0, 0));
        btnNewButton_3.setBounds(310, 410, 86, 72);
        contentPane.add(btnNewButton_3);

        JButton btnNewButton_4 = new JButton("Update");
        btnNewButton_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	updateEquipment();
            	
            }

			private void updateEquipment() {
				String updateQuery = "UPDATE Equipment SET name = ?, type = ?, unit_id = ?, status = ?, location_id = ? WHERE equipment_id = ?";

		        // Validate input fields
		        if (equip_ID_txt.getText().isEmpty() || equip_name_txt.getText().isEmpty() || Unit_ID_txt.getText().isEmpty()
		                || location_ID_txt.getText().isEmpty()) {
		            JOptionPane.showMessageDialog(Equipment_details.this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        try (Connection conn = DriverManager.getConnection(url, user, password);
		             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

		            pstmt.setString(1, equip_name_txt.getText());
		            pstmt.setString(2, (String) equip_type_txt.getSelectedItem());
		            pstmt.setInt(3, Integer.parseInt(Unit_ID_txt.getText()));
		            pstmt.setString(4, (String) Status_txt.getSelectedItem());
		            pstmt.setInt(5, Integer.parseInt(location_ID_txt.getText()));
		            pstmt.setInt(6, Integer.parseInt(equip_ID_txt.getText()));

		            int rowsAffected = pstmt.executeUpdate();

		            if (rowsAffected > 0) {
		                JOptionPane.showMessageDialog(Equipment_details.this, "Equipment updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
		                refreshTextFields();
		                loadEquipmentData();
		                
		            } else {
		                JOptionPane.showMessageDialog(Equipment_details.this, "Failed to update equipment.", "Error", JOptionPane.ERROR_MESSAGE);
		            }

		        } catch (NumberFormatException e) {
		            JOptionPane.showMessageDialog(Equipment_details.this, "Invalid number format in ID fields.", "Error", JOptionPane.ERROR_MESSAGE);
		        } catch (SQLException e) {
		            e.printStackTrace();
		            JOptionPane.showMessageDialog(Equipment_details.this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		        }
			}
			 private void loadEquipmentData() {
		    	  DefaultTableModel model = (DefaultTableModel) Equipment_table.getModel();
		          model.setRowCount(0); // Clear table

		          try (Connection conn = DriverManager.getConnection(url, user, password)) {
		              String query = "SELECT * FROM Equipment";
		              PreparedStatement pstmt = conn.prepareStatement(query);
		              ResultSet rs = pstmt.executeQuery();

		              while (rs.next()) {
		                  Vector<Object> row = new Vector<>();
		                  row.add(rs.getInt("equipment_id"));
		                  row.add(rs.getString("name"));
		                  row.add(rs.getString("type"));
		                  row.add(rs.getInt("unit_id"));
		                  row.add(rs.getString("status"));
		                  row.add(rs.getInt("location_id"));
		                  model.addRow(row);
		              }

		          } catch (SQLException e) {
		              e.printStackTrace();
		          }
			}



			private void refreshTextFields() {
				 equip_ID_txt.setText("");
			        equip_name_txt.setText("");
			        Unit_ID_txt.setText("");
			        location_ID_txt.setText("");
			}

			
        });
        btnNewButton_4.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
        btnNewButton_4.setForeground(new Color(255, 255, 255));
        btnNewButton_4.setBackground(new Color(0, 0, 0));
        btnNewButton_4.setBounds(425, 411, 86, 70);
        contentPane.add(btnNewButton_4);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(298, 64, 451, 322);
        contentPane.add(scrollPane);

        Equipment_table = new JTable();
        scrollPane.setViewportView(Equipment_table);
        Equipment_table.setModel(new DefaultTableModel(
                new Object[][] {
                },
                new String[] {
                        "Equipment_ID", "Name", "Type", "Unit ID", "Status", "Location_ID"
                }
        ));
        Equipment_table.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 10));
        Equipment_table.setForeground(new Color(255, 255, 255));
        Equipment_table.setBackground(new Color(0, 0, 0));
        
        JButton btnNewButton_5 = new JButton("Analyse");
        btnNewButton_5.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Map<String, Integer> data = Equipment_database.getPersonnelStatusCount();
                // Create a new frame for the bar chart
                JFrame chartFrame = new JFrame("Personnel Status Count");
                chartFrame.setSize(800, 600);
                chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                chartFrame.getContentPane().add(new EquipmentBarChart(data, "Equipment Status Count"));
                chartFrame.setVisible(true);
                // Dispose the current frame
               Equipment_details.this.dispose();
        	}
        });
        btnNewButton_5.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
        btnNewButton_5.setBackground(new Color(0, 0, 0));
        btnNewButton_5.setForeground(new Color(255, 255, 255));
        btnNewButton_5.setBounds(647, 410, 102, 72);
        contentPane.add(btnNewButton_5);
        
    }
}
