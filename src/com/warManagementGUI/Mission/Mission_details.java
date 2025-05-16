package com.warManagementGUI.Mission;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.warManagementGUI.DataAnalysis.MissionsBarChart;
import com.warManagementGUI.WarManagement;
import com.warManagementGUI.util.AbstractDetailsFrame;
import com.warManagementGUI.util.DBUtil;

public class Mission_details extends AbstractDetailsFrame {
    private static final long serialVersionUID = 1L;
    private JTextField mission_ID_txt, mission_name_txt, Objective_txt, start_date_txt, end_date_txt, location_ID_txt;
    private JComboBox<String> statusComboBox;
    private JTable Missions_table;

    public static void main(String[] args) {
        try {
            new Mission_details().display();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error launching application: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Mission_details() {
        super("Mission", 773, 529);
        setupLabels();
        setupTextFields();
        setupComboBox();
        setupTable();
        setupButtons();
        refreshTableData();
    }

    private void setupLabels() {
        createLabel("Mission Details", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 30), 10, 23, 228, 40);
        createLabel("Mission ID", LABEL_FONT, 20, 84, 114, 25);
        createLabel("Mission Name", LABEL_FONT, 20, 120, 138, 25);
        createLabel("Objective", LABEL_FONT, 20, 170, 125, 25);
        createLabel("Start Date", LABEL_FONT, 20, 220, 108, 25);
        createLabel("End Date", LABEL_FONT, 20, 266, 108, 25);
        createLabel("Status", LABEL_FONT, 20, 309, 108, 25);
        createLabel("Location ID", LABEL_FONT, 20, 352, 114, 25);
    }

    private void setupTextFields() {
        mission_ID_txt = createTextField(179, 79, 86, 20);
        mission_name_txt = createTextField(179, 133, 86, 20);
        Objective_txt = createTextField(179, 179, 86, 20);
        start_date_txt = createTextField(179, 230, 86, 20);
        end_date_txt = createTextField(179, 278, 86, 20);
        location_ID_txt = createTextField(179, 361, 86, 20);
    }

    private void setupComboBox() {
        statusComboBox = new JComboBox<>();
        statusComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"planned", "ongoing", "completed"}));
        statusComboBox.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 10));
        statusComboBox.setBounds(179, 311, 96, 21);
        contentPane.add(statusComboBox);
    }

    private void setupTable() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(294, 48, 454, 315);
        contentPane.add(scrollPane);

        Missions_table = new JTable();
        Missions_table.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Mission ID", "Name", "Objective", "Start Date", "End Date", "Status", "Location ID"}
        ));
        Missions_table.setBackground(Color.BLACK);
        Missions_table.setForeground(TEXT_COLOR);
        scrollPane.setViewportView(Missions_table);
    }

    private void setupButtons() {
        createButton("Back to Dashboard", 10, 427, 198, 55, e -> {
            new WarManagement().setVisible(true);
            dispose();
        });
        createButton("Refresh", 218, 425, 105, 58, e -> refreshTextFields());
        createButton("Insert", 333, 425, 86, 58, e -> insertMission());
        createButton("Update", 429, 427, 86, 55, e -> updateMission());
        createButton("Delete", 525, 425, 86, 58, e -> deleteMission());
        createButton("Analyze", 621, 425, 114, 58, e -> analyzeMissions());
    }

    private void analyzeMissions() {
        MissionsBarChart.showMissionStatusChart();
        dispose();
    }

    private void refreshTextFields() {
        mission_ID_txt.setText(""); mission_name_txt.setText(""); Objective_txt.setText("");
        start_date_txt.setText(""); end_date_txt.setText(""); location_ID_txt.setText("");
    }

    private void refreshTableData() {
        DefaultTableModel model = (DefaultTableModel) Missions_table.getModel();
        model.setRowCount(0);
        String query = "SELECT * FROM missions";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("mission_id"), rs.getString("name"), rs.getString("objective"),
                        rs.getString("start_date"), rs.getString("end_date"), rs.getString("status"), rs.getString("location_id")
                });
            }
        } catch (SQLException e) {
            handleDatabaseError(e, "Error retrieving mission data");
        }
    }

    private void insertMission() {
        if (mission_ID_txt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String sql = "INSERT INTO missions(mission_id,name,objective,start_date,end_date,status,location_id) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mission_ID_txt.getText()); pstmt.setString(2, mission_name_txt.getText());
            pstmt.setString(3, Objective_txt.getText()); pstmt.setString(4, start_date_txt.getText());
            pstmt.setString(5, end_date_txt.getText()); pstmt.setString(6, (String)statusComboBox.getSelectedItem());
            pstmt.setString(7, location_ID_txt.getText());
            if (pstmt.executeUpdate() > 0) JOptionPane.showMessageDialog(this, "Mission inserted.");
            refreshTextFields(); refreshTableData();
        } catch (SQLException e) {
            handleDatabaseError(e, "Error inserting mission");
        }
    }

    private void updateMission() {
        if (mission_ID_txt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Mission ID to update", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String sql = "UPDATE missions SET name=?,objective=?,start_date=?,end_date=?,status=?,location_id=? WHERE mission_id=?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mission_name_txt.getText()); pstmt.setString(2, Objective_txt.getText());
            pstmt.setString(3, start_date_txt.getText()); pstmt.setString(4, end_date_txt.getText());
            pstmt.setString(5, (String)statusComboBox.getSelectedItem()); pstmt.setString(6, location_ID_txt.getText());
            pstmt.setString(7, mission_ID_txt.getText());
            if (pstmt.executeUpdate() > 0) JOptionPane.showMessageDialog(this, "Mission updated.");
            refreshTextFields(); refreshTableData();
        } catch (SQLException e) {
            handleDatabaseError(e, "Error updating mission");
        }
    }

    private void deleteMission() {
        if (mission_ID_txt.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Mission ID to delete", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Confirm deletion?", "Delete Mission", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        String sql = "DELETE FROM missions WHERE mission_id=?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, mission_ID_txt.getText());
            if (pstmt.executeUpdate() > 0) JOptionPane.showMessageDialog(this, "Mission deleted.");
            refreshTextFields(); refreshTableData();
        } catch (SQLException e) {
            handleDatabaseError(e, "Error deleting mission");
        }
    }
}
