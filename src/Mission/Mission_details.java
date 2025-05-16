package Mission;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import DataAnalysis.MissionsBarChart;
import util.DBUtil;
import warManagement.WarManagement;

public class Mission_details extends JFrame {
    private static final long serialVersionUID = 1L;
    // constants for UI
    private static final Font LABEL_FONT = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20);
    private static final Font BUTTON_FONT = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color BG_COLOR = new Color(0, 64, 64);

    private JPanel contentPane;
    private JTextField mission_ID_txt, mission_name_txt, Objective_txt, start_date_txt, end_date_txt, location_ID_txt;
    private JComboBox<String> statusComboBox;
    private JTable Missions_table;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new Mission_details().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error launching application: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Mission_details() {
        initializeFrame();
        setupLabels();
        setupTextFields();
        setupComboBox();
        setupTable();
        setupButtons();
        refreshTableData();
    }

    private void initializeFrame() {
        setTitle("Mission");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 773, 529);
        contentPane = new JPanel();
        contentPane.setBackground(BG_COLOR);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);
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

    private JLabel createLabel(String text, Font font, int x, int y, int w, int h) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(TEXT_COLOR);
        label.setBounds(x, y, w, h);
        contentPane.add(label);
        return label;
    }

    private JTextField createTextField(int x, int y, int w, int h) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, w, h);
        tf.setColumns(10);
        contentPane.add(tf);
        return tf;
    }

    private JButton createButton(String text, int x, int y, int w, int h, ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setFont(BUTTON_FONT);
        btn.setBackground(Color.BLACK);
        btn.setForeground(TEXT_COLOR);
        btn.setBounds(x, y, w, h);
        btn.addActionListener(listener);
        contentPane.add(btn);
        return btn;
    }

    private void analyzeMissions() {
        Map<String, Integer> data = DBUtil.getMissionsStatusCount();
        JFrame chartFrame = new JFrame("Mission Status Count");
        chartFrame.setSize(800, 600);
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chartFrame.getContentPane().add(new MissionsBarChart(data, "Mission Status Count"));
        chartFrame.setVisible(true);
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

    private void handleDatabaseError(SQLException e, String message) {
        System.err.println(message + ": " + e.getMessage());
        JOptionPane.showMessageDialog(this, message + ": " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}
