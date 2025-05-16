package com.warManagementGUI.Equipment;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.warManagementGUI.DataAnalysis.EquipmentBarChart;
import com.warManagementGUI.WarManagement;
import com.warManagementGUI.util.AbstractDetailsFrame;
import com.warManagementGUI.util.DBUtil;

public class Equipment_details extends AbstractDetailsFrame {
    private static final long serialVersionUID = 1L;

	 private static final String[] EQUIPMENT_TYPES = {"Weapon", "Vehicle", "Electronic", "Other"};
	 private static final String[] STATUS_OPTIONS = {"Operational", "Maintenance", "Decommissioned"};    

    private JTextField equipIdTxt, equipNameTxt, unitIdTxt, locationIdTxt;
    private JComboBox<String> equipTypeTxt, statusTxt;
    private JTable equipmentTable;

    public static void main(String[] args) {
        try {
            new Equipment_details().display();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error launching application: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Equipment_details() {
        super("Equipment", 773, 529);
        setupLabels();
        setupTextFields();
        setupComboBox();
        setupTable();
        setupButtons();
        refreshTableData();
    }

    private void setupLabels() {
        createLabel("Equipment Details", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 35), 27, 10, 275, 83);
        createLabel("Equipment ID", LABEL_FONT, 27, 90, 155, 25);
        createLabel("Name", LABEL_FONT, 27, 132, 69, 28);
        createLabel("Type", LABEL_FONT, 27, 186, 69, 25);
        createLabel("Unit ID", LABEL_FONT, 27, 223, 86, 30);
        createLabel("Status", LABEL_FONT, 27, 274, 75, 28);
        createLabel("Location Id", LABEL_FONT, 27, 321, 127, 28);
    }

    private void setupTextFields() {
        equipIdTxt = createTextField(192, 93, 86, 20);
        equipNameTxt = createTextField(192, 132, 86, 20);
        unitIdTxt = createTextField(192, 231, 86, 20);
        locationIdTxt = createTextField(192, 328, 96, 20);
    }

    private void setupComboBox() {
        equipTypeTxt = new JComboBox<>(EQUIPMENT_TYPES);
        equipTypeTxt.setBounds(192,186,86,21);
        contentPane.add(equipTypeTxt);
        statusTxt = new JComboBox<>(STATUS_OPTIONS);
        statusTxt.setBounds(192,280,86,21);
        contentPane.add(statusTxt);
    }

    private void setupTable() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(298, 64, 451, 322);
        contentPane.add(scrollPane);

        equipmentTable = new JTable();
        equipmentTable.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"Equipment_ID","Name","Type","Unit ID","Status","Location_ID"}));
        equipmentTable.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 10));
        equipmentTable.setForeground(TEXT_COLOR);
        equipmentTable.setBackground(Color.BLACK);
        scrollPane.setViewportView(equipmentTable);
    }

    private void setupButtons() {
        createButton("Back to Dashboard", 10, 410, 161, 72, e -> { new WarManagement().setVisible(true); dispose(); });
        createButton("Refresh", 192, 410, 96, 72, e -> refreshTextFields());
        createButton("Delete", 310, 410, 86, 72, e -> deleteEquipment());
        createButton("Update", 425, 411, 86, 70, e -> updateEquipment());
        createButton("Insert", 533, 413, 86, 67, e -> insertEquipment());
        createButton("Analyse", 647, 410, 102, 72, e -> analyzeEquipment());
    }

    private void refreshTextFields() {
        equipIdTxt.setText(""); 
        equipNameTxt.setText(""); 
        unitIdTxt.setText(""); 
        locationIdTxt.setText("");
    }

    private void refreshTableData() {
        DefaultTableModel model = (DefaultTableModel)equipmentTable.getModel(); model.setRowCount(0);
        String sql = "SELECT * FROM Equipment";
        try(Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()){
            while(rs.next()) {
                model.addRow(new Object[]{rs.getInt("equipment_id"), rs.getString("name"), rs.getString("type"), rs.getInt("unit_id"), rs.getString("status"), rs.getInt("location_id")});
            }
        } catch(SQLException e) {
            handleDatabaseError(e, "Error retrieving equipment data");
        }
    }
private void insertEquipment() {
        if(equipIdTxt.getText().isEmpty() || 
           equipNameTxt.getText().isEmpty() || 
           unitIdTxt.getText().isEmpty() || 
           locationIdTxt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validate numeric fields
        try {
            Integer.valueOf(equipIdTxt.getText());
            Integer.valueOf(unitIdTxt.getText());
            Integer.valueOf(locationIdTxt.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID fields must contain valid numbers", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String sql="INSERT INTO Equipment(equipment_id,name,type,unit_id,status,location_id) VALUES(?,?,?,?,?,?)";
        try(Connection conn = DBUtil.getConnection(); PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, Integer.parseInt(equipIdTxt.getText()));
            p.setString(2, equipNameTxt.getText());
            p.setString(3, (String)equipTypeTxt.getSelectedItem());
            p.setInt(4, Integer.parseInt(unitIdTxt.getText()));
            p.setString(5, (String)statusTxt.getSelectedItem());
            p.setInt(6, Integer.parseInt(locationIdTxt.getText()));
            
            if(p.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Equipment inserted successfully.");
            }
            refreshTextFields();
            refreshTableData();
        } catch(NumberFormatException e){handleDatabaseError(e,"Invalid number format");}
          catch(SQLException e){handleDatabaseError(e,"Error inserting equipment");}
    }
private void updateEquipment() {
        if(equipIdTxt.getText().isEmpty() || 
           equipNameTxt.getText().isEmpty() || 
           unitIdTxt.getText().isEmpty() || 
           locationIdTxt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validate numeric fields
        try {
            Integer.valueOf(equipIdTxt.getText());
            Integer.valueOf(unitIdTxt.getText());
            Integer.valueOf(locationIdTxt.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID fields must contain valid numbers", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String sql="UPDATE Equipment SET name=?,type=?,unit_id=?,status=?,location_id=? WHERE equipment_id=?";
        try(Connection conn = DBUtil.getConnection(); PreparedStatement p = conn.prepareStatement(sql)) {
            p.setString(1, equipNameTxt.getText());
            p.setString(2, (String)equipTypeTxt.getSelectedItem());
            p.setInt(3, Integer.parseInt(unitIdTxt.getText()));
            p.setString(4, (String)statusTxt.getSelectedItem());
            p.setInt(5, Integer.parseInt(locationIdTxt.getText()));
            p.setInt(6, Integer.parseInt(equipIdTxt.getText()));
            
            if(p.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Equipment updated successfully.");
            }
            refreshTextFields();
            refreshTableData();
        } catch(NumberFormatException e){handleDatabaseError(e,"Invalid number format");}
          catch(SQLException e){handleDatabaseError(e,"Error updating equipment");}
    }

    private void deleteEquipment() {
        if(equipIdTxt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Equipment ID", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if(JOptionPane.showConfirmDialog(this, "Confirm deletion?", "Delete Equipment", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        
        String sql="DELETE FROM Equipment WHERE equipment_id=?";
        try(Connection conn = DBUtil.getConnection(); PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, Integer.parseInt(equipIdTxt.getText()));
            
            if(p.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Equipment deleted successfully.");
            }
            refreshTextFields();
            refreshTableData();
        } catch(NumberFormatException e){handleDatabaseError(e,"Invalid number format");}
          catch(SQLException e){handleDatabaseError(e,"Error deleting equipment");}
    }
	private void analyzeEquipment() {
	   JFrame chartFrame = EquipmentBarChart.showEquipmentStatusChart();
	
	   // Optional: Add a listener to handle the chart window closing
	   chartFrame.addWindowListener(new WindowAdapter() {
	       @Override
	       public void windowClosing(WindowEvent e) {
	           refreshTableData();
	       }
	   });
	}
}
