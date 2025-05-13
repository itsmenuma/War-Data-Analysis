package Mission;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import util.DBUtil;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;

import DataAnalysis.MissionsBarChart;
import DataAnalysis.Missions_DataSet;
import warManagement.WarManagement;

import javax.swing.JScrollPane;

public class Mission_details extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField mission_name_txt;
    private JTextField Objective_txt;
    private JTextField start_date_txt;
    private JTextField end_date_txt;
    private JTextField location_ID_txt;
    private JTextField mission_ID_txt;
    private JTable Missions_table;
    private JComboBox<String> status_txt;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Mission_details frame = new Mission_details();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Mission_details() {
        setTitle("Mission");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 773, 529);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 64, 64));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblNewLabel_1 = new JLabel("Mission Name");
        lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
        lblNewLabel_1.setForeground(new Color(255, 255, 255));
        lblNewLabel_1.setBounds(20, 120, 138, 40);
        contentPane.add(lblNewLabel_1);
        
        JLabel lblNewLabel_2 = new JLabel("Mission ID");
        lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
        lblNewLabel_2.setForeground(new Color(255, 255, 255));
        lblNewLabel_2.setBounds(20, 84, 114, 14);
        contentPane.add(lblNewLabel_2);
        
        JButton btnNewButton = new JButton("Refresh");
        btnNewButton.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
        btnNewButton.addActionListener(e -> refreshTextFields());
        btnNewButton.setBackground(new Color(0, 0, 0));
        btnNewButton.setForeground(new Color(255, 255, 255));
        btnNewButton.setBounds(218, 425, 105, 58);
        contentPane.add(btnNewButton);
        
        JButton btnNewButton_1 = new JButton("Back to Dashboard\r\n");
        btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WarManagement dashboard = new WarManagement();
                dashboard.setVisible(true);
                dispose();
            }
        });
        
        JComboBox status_txt = new JComboBox();
        status_txt.setModel(new DefaultComboBoxModel(new String[] {"planned", "ongoing", "completed"}));
        status_txt.setBounds(179, 311, 96, 21);
        contentPane.add(status_txt);
        
        btnNewButton_1.setBackground(new Color(0, 0, 0));
        btnNewButton_1.setForeground(new Color(255, 255, 255));
        btnNewButton_1.setBounds(10, 427, 198, 55);
        contentPane.add(btnNewButton_1);
        
        JButton btnNewButton_2 = new JButton("Insert");
        btnNewButton_2.addActionListener(e -> insertMission());
        btnNewButton_2.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
        btnNewButton_2.setBackground(new Color(0, 0, 0));
        btnNewButton_2.setForeground(new Color(255, 255, 255));
        btnNewButton_2.setBounds(333, 425, 86, 58);
        contentPane.add(btnNewButton_2);
        
        mission_name_txt = new JTextField();
        mission_name_txt.setBounds(179, 133, 86, 20);
        contentPane.add(mission_name_txt);
        mission_name_txt.setColumns(10);
        
        JLabel lblNewLabel_3 = new JLabel("Objective");
        lblNewLabel_3.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
        lblNewLabel_3.setBackground(new Color(0, 0, 0));
        lblNewLabel_3.setForeground(new Color(255, 255, 255));
        lblNewLabel_3.setBounds(20, 170, 125, 28);
        contentPane.add(lblNewLabel_3);
        
        JLabel lblNewLabel_4 = new JLabel("Start Date");
        lblNewLabel_4.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
        lblNewLabel_4.setBackground(new Color(0, 0, 0));
        lblNewLabel_4.setForeground(new Color(255, 255, 255));
        lblNewLabel_4.setBounds(20, 221, 108, 28);
        contentPane.add(lblNewLabel_4);
        
        JLabel lblNewLabel_5 = new JLabel("End Date");
        lblNewLabel_5.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
        lblNewLabel_5.setBackground(new Color(0, 0, 0));
        lblNewLabel_5.setForeground(new Color(255, 255, 255));
        lblNewLabel_5.setBounds(20, 267, 108, 34);
        contentPane.add(lblNewLabel_5);
        
        JLabel lblNewLabel_6 = new JLabel("Status");
        lblNewLabel_6.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
        lblNewLabel_6.setBackground(new Color(0, 0, 0));
        lblNewLabel_6.setForeground(new Color(255, 255, 255));
        lblNewLabel_6.setBounds(20, 309, 108, 22);
        contentPane.add(lblNewLabel_6);
        
        JLabel lblNewLabel_7 = new JLabel("Location ID");
        lblNewLabel_7.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
        lblNewLabel_7.setBackground(new Color(0, 0, 0));
        lblNewLabel_7.setForeground(new Color(255, 255, 255));
        lblNewLabel_7.setBounds(20, 352, 114, 22);
        contentPane.add(lblNewLabel_7);
        
        Objective_txt = new JTextField();
        Objective_txt.setBounds(179, 179, 86, 20);
        contentPane.add(Objective_txt);
        Objective_txt.setColumns(10);
        
        start_date_txt = new JTextField();
        start_date_txt.setBounds(179, 230, 86, 20);
        contentPane.add(start_date_txt);
        start_date_txt.setColumns(10);
        
        end_date_txt = new JTextField();
        end_date_txt.setBounds(179, 278, 86, 20);
        contentPane.add(end_date_txt);
        end_date_txt.setColumns(10);
        
        location_ID_txt = new JTextField();
        location_ID_txt.setBounds(179, 361, 86, 20);
        contentPane.add(location_ID_txt);
        location_ID_txt.setColumns(10);
        
        mission_ID_txt = new JTextField();
        mission_ID_txt.setBounds(179, 79, 86, 20);
        contentPane.add(mission_ID_txt);
        mission_ID_txt.setColumns(10);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(294, 48, 454, 315);
        contentPane.add(scrollPane);
        
        Missions_table = new JTable();
        Missions_table.setForeground(new Color(255, 255, 255));
        Missions_table.setBackground(new Color(0, 0, 0));
        scrollPane.setViewportView(Missions_table);
        Missions_table.setModel(new DefaultTableModel(
            new Object[][] {
            },
            new String[] {
                "Mission ID", "Name", "Objective", "Start Date", "End Date", "Status", "Location ID"
            }
        ));
        
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
        btnUpdate.setBackground(new Color(0, 0, 0));
        btnUpdate.setForeground(new Color(255, 255, 255));
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateMission();
            }
        });
        btnUpdate.setBounds(429, 427, 86, 55);
        contentPane.add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
        btnDelete.setBackground(new Color(0, 0, 0));
        btnDelete.setForeground(new Color(255, 255, 255));
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteMission();
            }
        });
        btnDelete.setBounds(525, 425, 86, 58);
        contentPane.add(btnDelete);
        
        JLabel lblNewLabel = new JLabel("Mission Details");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 30));
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setBounds(10, 23, 228, 40);
        contentPane.add(lblNewLabel);
        
        JButton btnNewButton_3 = new JButton("Analyze");
        btnNewButton_3.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Map<String, Integer> data =Missions_DataSet.getPersonnelStatusCount();
                // Create a new frame for the bar chart
                JFrame chartFrame = new JFrame("Mission Status Count");
                chartFrame.setSize(800, 600);
                chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                chartFrame.getContentPane().add(new MissionsBarChart(data, "Mission Status Count"));
                chartFrame.setVisible(true);
                // Dispose the current frame
                Mission_details.this.dispose();
        	}
        });
        btnNewButton_3.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15));
        btnNewButton_3.setBackground(new Color(0, 0, 0));
        btnNewButton_3.setForeground(new Color(255, 255, 255));
        btnNewButton_3.setBounds(621, 425, 114, 58);
        contentPane.add(btnNewButton_3);

        // Initialize data
        loadMissionData();
    }

    // Common operations
    private void refreshTextFields() {
        mission_ID_txt.setText(""); mission_name_txt.setText(""); Objective_txt.setText("");
        start_date_txt.setText(""); end_date_txt.setText(""); location_ID_txt.setText("");
    }

    private void loadMissionData() {
        DefaultTableModel model = (DefaultTableModel) Missions_table.getModel();
        model.setRowCount(0);
        String query = "SELECT * FROM missions";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("mission_id"), rs.getString("name"), rs.getString("objective"),
                    rs.getString("start_date"), rs.getString("end_date"), rs.getString("status"), rs.getString("location_id")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void insertMission() {
        String insertQuery = "INSERT INTO missions(mission_id,name,objective,start_date,end_date,status,location_id) VALUES(?,?,?,?,?,?,?)";
        if (mission_ID_txt.getText().isEmpty()) { JOptionPane.showMessageDialog(this,"Fill all fields.","Error",JOptionPane.ERROR_MESSAGE); return; }
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, mission_ID_txt.getText()); pstmt.setString(2, mission_name_txt.getText());
            pstmt.setString(3, Objective_txt.getText()); pstmt.setString(4, start_date_txt.getText());
            pstmt.setString(5, end_date_txt.getText()); pstmt.setString(6, (String)status_txt.getSelectedItem());
            pstmt.setString(7, location_ID_txt.getText());
            if (pstmt.executeUpdate()>0) JOptionPane.showMessageDialog(this,"Inserted.","Success",JOptionPane.INFORMATION_MESSAGE);
            loadMissionData(); refreshTextFields();
        } catch (SQLException e) { e.printStackTrace(); JOptionPane.showMessageDialog(this,"Error: "+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE); }
    }

    private void updateMission() {
        String updateQuery = "UPDATE missions SET name=?,objective=?,start_date=?,end_date=?,status=?,location_id=? WHERE mission_id=?";
        if (mission_ID_txt.getText().isEmpty()) { JOptionPane.showMessageDialog(this,"Enter ID.","Error",JOptionPane.ERROR_MESSAGE); return; }
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, mission_name_txt.getText()); pstmt.setString(2, Objective_txt.getText());
            pstmt.setString(3, start_date_txt.getText()); pstmt.setString(4, end_date_txt.getText());
            pstmt.setString(5, (String)status_txt.getSelectedItem()); pstmt.setString(6, location_ID_txt.getText());
            pstmt.setString(7, mission_ID_txt.getText());
            if (pstmt.executeUpdate()>0) JOptionPane.showMessageDialog(this,"Updated.","Success",JOptionPane.INFORMATION_MESSAGE);
            loadMissionData(); refreshTextFields();
        } catch (SQLException e) { e.printStackTrace(); JOptionPane.showMessageDialog(this,"Error: "+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE); }
    }

    private void deleteMission() {
        String deleteQuery = "DELETE FROM missions WHERE mission_id=?";
        if (mission_ID_txt.getText().isEmpty()) { JOptionPane.showMessageDialog(this,"Enter ID.","Error",JOptionPane.ERROR_MESSAGE); return; }
        if (JOptionPane.showConfirmDialog(this,"Confirm deletion?","Delete",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) return;
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setString(1, mission_ID_txt.getText()); if (pstmt.executeUpdate()>0) JOptionPane.showMessageDialog(this,"Deleted.","Success",JOptionPane.INFORMATION_MESSAGE);
            loadMissionData(); refreshTextFields();
        } catch(SQLException e) { e.printStackTrace(); JOptionPane.showMessageDialog(this,"Error: "+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE); }
    }
}
