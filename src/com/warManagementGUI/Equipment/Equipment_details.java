package com.warManagementGUI.Equipment;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.JComboBox;
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

    private JTextField equip_ID_txt, equip_name_txt, Unit_ID_txt, location_ID_txt;
    private JComboBox<String> equip_type_txt, Status_txt;
    private JTable Equipment_table;

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
        equip_ID_txt = createTextField(192, 93, 86, 20);
        equip_name_txt = createTextField(192, 132, 86, 20);
        Unit_ID_txt = createTextField(192, 231, 86, 20);
        location_ID_txt = createTextField(192, 328, 96, 20);
    }

    private void setupComboBox() {
        equip_type_txt = new JComboBox<>(new String[]{"Weapon","Vehicle","Electronic","Other"});
        equip_type_txt.setBounds(192,186,86,21);
        contentPane.add(equip_type_txt);
        Status_txt = new JComboBox<>(new String[]{"Operational","Maintenance","Decommissioned"});
        Status_txt.setBounds(192,280,86,21);
        contentPane.add(Status_txt);
    }

    private void setupTable() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(298, 64, 451, 322);
        contentPane.add(scrollPane);

        Equipment_table = new JTable();
        Equipment_table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"Equipment_ID","Name","Type","Unit ID","Status","Location_ID"}));
        Equipment_table.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 10));
        Equipment_table.setForeground(TEXT_COLOR);
        Equipment_table.setBackground(Color.BLACK);
        scrollPane.setViewportView(Equipment_table);
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
        equip_ID_txt.setText(""); equip_name_txt.setText(""); Unit_ID_txt.setText(""); location_ID_txt.setText("");
    }

    private void refreshTableData() {
        DefaultTableModel model = (DefaultTableModel)Equipment_table.getModel(); model.setRowCount(0);
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
        if(equip_ID_txt.getText().isEmpty()||equip_name_txt.getText().isEmpty()||Unit_ID_txt.getText().isEmpty()||location_ID_txt.getText().isEmpty()){
            JOptionPane.showMessageDialog(this,"Please fill in all fields","Validation Error",JOptionPane.WARNING_MESSAGE);return;
        }
        String sql="INSERT INTO Equipment(equipment_id,name,type,unit_id,status,location_id) VALUES(?,?,?,?,?,?)";
        try(Connection conn=DBUtil.getConnection(); PreparedStatement p=conn.prepareStatement(sql)){
            p.setInt(1,Integer.parseInt(equip_ID_txt.getText()));p.setString(2,equip_name_txt.getText());
            p.setString(3,(String)equip_type_txt.getSelectedItem());p.setInt(4,Integer.parseInt(Unit_ID_txt.getText()));
            p.setString(5,(String)Status_txt.getSelectedItem());p.setInt(6,Integer.parseInt(location_ID_txt.getText()));
            if(p.executeUpdate()>0) JOptionPane.showMessageDialog(this,"Equipment inserted successfully.");
            refreshTextFields();refreshTableData();
        } catch(NumberFormatException e){handleDatabaseError(e,"Invalid number format");}
          catch(SQLException e){handleDatabaseError(e,"Error inserting equipment");}
    }

    private void updateEquipment() {
        if(equip_ID_txt.getText().isEmpty()||equip_name_txt.getText().isEmpty()||Unit_ID_txt.getText().isEmpty()||location_ID_txt.getText().isEmpty()){
            JOptionPane.showMessageDialog(this,"Please fill in all fields","Validation Error",JOptionPane.WARNING_MESSAGE);return;
        }
        String sql="UPDATE Equipment SET name=?,type=?,unit_id=?,status=?,location_id=? WHERE equipment_id=?";
        try(Connection conn=DBUtil.getConnection(); PreparedStatement p=conn.prepareStatement(sql)){
            p.setString(1,equip_name_txt.getText());p.setString(2,(String)equip_type_txt.getSelectedItem());
            p.setInt(3,Integer.parseInt(Unit_ID_txt.getText()));p.setString(4,(String)Status_txt.getSelectedItem());
            p.setInt(5,Integer.parseInt(location_ID_txt.getText()));p.setInt(6,Integer.parseInt(equip_ID_txt.getText()));
            if(p.executeUpdate()>0) JOptionPane.showMessageDialog(this,"Equipment updated successfully.");
            refreshTextFields();refreshTableData();
        } catch(NumberFormatException e){handleDatabaseError(e,"Invalid number format");}
          catch(SQLException e){handleDatabaseError(e,"Error updating equipment");}
    }

    private void deleteEquipment() {
        if(equip_ID_txt.getText().isEmpty()){JOptionPane.showMessageDialog(this,"Please enter Equipment ID","Validation Error",JOptionPane.WARNING_MESSAGE);return;}
        if(JOptionPane.showConfirmDialog(this,"Confirm deletion?","Delete Equipment",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) return;
        String sql="DELETE FROM Equipment WHERE equipment_id=?";
        try(Connection conn=DBUtil.getConnection(); PreparedStatement p=conn.prepareStatement(sql)){
            p.setInt(1,Integer.parseInt(equip_ID_txt.getText()));if(p.executeUpdate()>0) JOptionPane.showMessageDialog(this,"Equipment deleted successfully.");
            refreshTextFields();refreshTableData();
        } catch(NumberFormatException e){handleDatabaseError(e,"Invalid number format");}
          catch(SQLException e){handleDatabaseError(e,"Error deleting equipment");}
    }

    private void analyzeEquipment() {
        Map<String,Integer> data =DBUtil.getEquipmentStatusCount();
        EquipmentBarChart.showEquipmentStatusChart();
        dispose();
    }
}
