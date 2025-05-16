package Units;


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

import DataAnalysis.UnitsBarChart;
import util.DBUtil;
import warManagement.WarManagement;

public class Units_Interface extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField unit_id_txt;
    private JTextField unit_name_txt;
    private JTextField commander_ID_txt;
    private JTextField Location_ID_txt;
    private JTable Units_table;
    private JComboBox<String> unitTypeComboBox;
    private static final Font LABEL_FONT = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20);
    private static final Font BUTTON_FONT = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color BG_COLOR = Color.BLACK;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Units_Interface frame = new Units_Interface();
                frame.setVisible(true);
            } catch (Exception e) {
                System.err.println("Error launching application: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "Error launching application: " + e.getMessage(), 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Create the frame.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Units_Interface() {
        // Initialize the frame
        initializeFrame();
        
        // Setup UI components
        setupLabels();
        setupTextFields();
        setupComboBox();
        setupTable();
        setupButtons();
        
        // Load initial data
        initializeTableData();
    }
    
    private void initializeFrame() {
        setTitle("Units");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 773, 529);
        contentPane = new JPanel();
        contentPane.setForeground(TEXT_COLOR);
        contentPane.setBackground(new Color(0, 64, 64));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
    }
    
    private void setupLabels() {
        createLabel("Units Information", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50), 30, 11, 390, 103);
        createLabel("Unit ID", LABEL_FONT, 30, 124, 96, 25);
        createLabel("Unit Name", LABEL_FONT, 30, 175, 96, 25);
        createLabel("Unit Type", LABEL_FONT, 30, 233, 96, 25);
        createLabel("Commander ID", LABEL_FONT, 30, 276, 133, 34);
        createLabel("Location ID", LABEL_FONT, 30, 343, 111, 25);
    }
    
    private void setupTextFields() {
        unit_id_txt = createTextField(174, 124, 86, 20, 10);
        unit_name_txt = createTextField(174, 180, 86, 20, 10);
        commander_ID_txt = createTextField(174, 286, 96, 19, 10);
        Location_ID_txt = createTextField(174, 352, 96, 19, 10);
    }
    
    private void setupComboBox() {
        unitTypeComboBox = new JComboBox<>();
        unitTypeComboBox.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 10));
        unitTypeComboBox.setBackground(Color.WHITE);
        unitTypeComboBox.setForeground(Color.BLACK);
        unitTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[] {"infantry", "cavalry", "artillery"}));
        unitTypeComboBox.setBounds(174, 237, 86, 21);
        contentPane.add(unitTypeComboBox);
    }
    
    private void setupTable() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(297, 110, 452, 258);
        contentPane.add(scrollPane);
        
        Units_table = new JTable();
        scrollPane.setViewportView(Units_table);
        Units_table.setBackground(BG_COLOR);
        Units_table.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 10));
        Units_table.setForeground(TEXT_COLOR);
        Units_table.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"Unit_ID", "Unit_Name", "Unit_type", "Commander_id", "location_id"}
        ));
    }
    
    private void setupButtons() {
        createButton("Back to Dashboard", 10, 418, 153, 44, e -> navigateToDashboard());
        createButton("Refresh", 186, 418, 104, 44, e -> refreshTextFields());
        createButton("Insert", 309, 418, 85, 44, e -> insertUnit());
        createButton("Update", 422, 418, 86, 44, e -> updateUnit());
        createButton("Delete", 532, 418, 104, 44, e -> deleteUnit());
        createButton("Analyse", 664, 418, 85, 44, e -> analyzeUnits());
    }
    
    private JLabel createLabel(String text, Font font, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(TEXT_COLOR);
        label.setBounds(x, y, width, height);
        contentPane.add(label);
        return label;
    }
    
    private JTextField createTextField(int x, int y, int width, int height, int columns) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        textField.setColumns(columns);
        contentPane.add(textField);
        return textField;
    }
    
    private JButton createButton(String text, int x, int y, int width, int height, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(BG_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setBounds(x, y, width, height);
        button.addActionListener(listener);
        contentPane.add(button);
        return button;
    }
    
    private void navigateToDashboard() {
        new WarManagement().setVisible(true);
        dispose();
    }
    
    private void analyzeUnits() {
        Map<String, Integer> data = DBUtil.getUnitsByTypeCount();
        JFrame chartFrame = new JFrame("Units by Type");
        chartFrame.setSize(800, 600);
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chartFrame.getContentPane().add(new UnitsBarChart(data, "Units by Type Count"));
        chartFrame.setVisible(true);
        dispose();
    }
    
    protected void refreshTextFields() {
        unit_id_txt.setText("");
        unit_name_txt.setText("");
        commander_ID_txt.setText("");
        Location_ID_txt.setText("");
    }
    
    protected void refreshTableData() {
        DefaultTableModel model = (DefaultTableModel) Units_table.getModel();
        model.setRowCount(0); // Clear existing data

        String selectQuery = "SELECT * FROM units";

        try (Connection conn = DBUtil.getConnection();
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
            handleDatabaseError(e, "Error retrieving unit data");
        }
    }
    
    private void insertUnit() {
        String unitId = unit_id_txt.getText();
        String unitName = unit_name_txt.getText();
        String unitType = (String) unitTypeComboBox.getSelectedItem();
        String commanderId = commander_ID_txt.getText();
        String locationId = Location_ID_txt.getText();
        
        // Input validation
        if (unitId.isEmpty() || unitName.isEmpty() || commanderId.isEmpty() || locationId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String insertQuery = "INSERT INTO units (unit_id, unit_name, unit_type, commander_id, location_id) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            pstmt.setString(1, unitId);
            pstmt.setString(2, unitName);
            pstmt.setString(3, unitType);
            pstmt.setString(4, commanderId);
            pstmt.setString(5, locationId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Unit inserted successfully.");
                refreshTextFields();
                refreshTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to insert unit.");
            }
        } catch (SQLException e) {
            handleDatabaseError(e, "Error inserting unit data");
        }
    }
    
    private void updateUnit() {
        String unitId = unit_id_txt.getText();
        String unitName = unit_name_txt.getText();
        String unitType = (String) unitTypeComboBox.getSelectedItem();
        String commanderId = commander_ID_txt.getText();
        String locationId = Location_ID_txt.getText();
        
        // Input validation
        if (unitId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Unit ID to update", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String updateQuery = "UPDATE units SET unit_name = ?, unit_type = ?, commander_id = ?, location_id = ? WHERE unit_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.setString(1, unitName);
            pstmt.setString(2, unitType);
            pstmt.setString(3, commanderId);
            pstmt.setString(4, locationId);
            pstmt.setString(5, unitId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Unit updated successfully.");
                refreshTextFields();
                refreshTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update unit. Unit ID may not exist.");
            }
        } catch (SQLException e) {
            handleDatabaseError(e, "Error updating unit data");
        }
    }
    
    private void deleteUnit() {
        String unitId = unit_id_txt.getText();
        
        // Input validation
        if (unitId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Unit ID to delete", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete Unit ID: " + unitId + "?", 
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        String deleteQuery = "DELETE FROM units WHERE unit_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

            pstmt.setString(1, unitId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Unit deleted successfully.");
                refreshTextFields();
                refreshTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete unit. Unit ID may not exist.");
            }
        } catch (SQLException e) {
            handleDatabaseError(e, "Error deleting unit data");
        }
    }
    
    private void handleDatabaseError(SQLException e, String message) {
        System.err.println(message + ": " + e.getMessage());
        JOptionPane.showMessageDialog(this, 
            message + ": " + e.getMessage(), 
            "Database Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    private void initializeTableData() {
        DefaultTableModel model = (DefaultTableModel) Units_table.getModel();
        model.setRowCount(0); // Clear existing data

        String selectQuery = "SELECT * FROM units";

        try (Connection conn = DBUtil.getConnection();
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
            handleDatabaseError(e, "Error retrieving unit data");
        }
    }
}
