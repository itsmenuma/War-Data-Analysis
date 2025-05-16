package Personnel;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import util.DBUtil;
import warManagement.WarManagement;

public class Personnel_Remove_Details extends JFrame {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPane = new JPanel();
    private JTextField Personnel_ID_txt;
    private JTextField Fname_txt;
    private JTextField Unit_ID_txt;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Personnel_Remove_Details frame = new Personnel_Remove_Details();
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
    public Personnel_Remove_Details() {
        initializeFrame();
        setupLabels();
        setupTextFields();
        setupButtons();
    }
    
    // Initialize the frame
    private void initializeFrame() {
        setTitle("Remove Personnel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 773, 529);
        contentPane.setBackground(new Color(0, 64, 64));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
    }
    
    // Setup labels
    private void setupLabels() {
        createLabel("Remove Personnel Details", 
                   new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50), 
                   Color.WHITE, 21, 10, 593, 123);
        
        createLabel("Personnel ID", 
                   new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), 
                   Color.WHITE, 33, 154, 133, 50);
        
        createLabel("First Name", 
                   new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), 
                   Color.WHITE, 33, 226, 119, 45);
        
        createLabel("Unit ID", 
                   new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), 
                   Color.WHITE, 33, 310, 77, 45);
    }
    
    // Setup text fields
    private void setupTextFields() {
        Personnel_ID_txt = createTextField(199, 172, 96, 19, 10);
        Fname_txt = createTextField(199, 241, 96, 19, 10);
        Unit_ID_txt = createTextField(199, 310, 96, 19, 10);
    }
    
    // Setup buttons
    private void setupButtons() {
        createButton("Back to Dashboard", 
                     new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20),
                     21, 397, 213, 50,
                     e -> navigateToDashboard());
        
        createButton("Reset", 
                     new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20),
                     334, 397, 133, 50,
                     e -> resetTextFields());
        
        createButton("Remove", 
                     new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20),
                     558, 397, 133, 50,
                     e -> removeDetails());
    }

    // Helper method for creating labels
    private JLabel createLabel(String text, Font font, Color color, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        label.setBounds(x, y, width, height);
        contentPane.add(label);
        return label;
    }
    
    // Helper method for creating text fields
    private JTextField createTextField(int x, int y, int width, int height, int columns) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        textField.setColumns(columns);
        contentPane.add(textField);
        return textField;
    }
    
    // Helper method for creating buttons
    private JButton createButton(String text, Font font, int x, int y, int width, int height, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLACK);
        button.setBounds(x, y, width, height);
        button.addActionListener(listener);
        contentPane.add(button);
        return button;
    }
    
    // Navigate to dashboard
    private void navigateToDashboard() {
        new WarManagement().setVisible(true);
        dispose();
    }
    
    // Helper method to reset text fields
    private void resetTextFields() {
        Personnel_ID_txt.setText("");
        Fname_txt.setText("");
        Unit_ID_txt.setText("");
    }
    
    // Helper method to remove personnel details
    private void removeDetails() {
        String personnelId = Personnel_ID_txt.getText();
        String firstName = Fname_txt.getText();
        String unitId = Unit_ID_txt.getText();

        if (personnelId.isEmpty() && firstName.isEmpty() && unitId.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter at least one field to identify personnel to remove.", 
                "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String sql = buildDeleteQuery(personnelId, firstName, unitId);
            executeDeleteQuery(sql, personnelId, firstName, unitId);
        } catch (SQLException ex) {
            System.err.println("Database error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Build SQL delete query based on provided fields
    private String buildDeleteQuery(String personnelId, String firstName, String unitId) {
        StringBuilder sql = new StringBuilder("DELETE FROM Personnel WHERE 1=1");
        if (!personnelId.isEmpty()) sql.append(" AND Personnel_id = ?");
        if (!firstName.isEmpty()) sql.append(" AND First_name = ?");
        if (!unitId.isEmpty()) sql.append(" AND Unit_Id = ?");
        return sql.toString();
    }
    
    // Execute the delete query with parameters
    private void executeDeleteQuery(String sql, String personnelId, String firstName, String unitId) 
            throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setQueryParameters(pstmt, personnelId, firstName, unitId);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Personnel details removed successfully.");
                navigateToPersonnelDetails();
            } else {
                JOptionPane.showMessageDialog(this, "No matching personnel found.");
            }
        }
    }
    
    // Set parameters for the prepared statement
    private void setQueryParameters(PreparedStatement pstmt, String personnelId, 
                                   String firstName, String unitId) throws SQLException {
        int index = 1;
        if (!personnelId.isEmpty()) pstmt.setString(index++, personnelId);
        if (!firstName.isEmpty()) pstmt.setString(index++, firstName);
        if (!unitId.isEmpty()) pstmt.setString(index++, unitId);
    }
    
    // Navigate to personnel details screen
    private void navigateToPersonnelDetails() {
        new Personnel_details().setVisible(true);
        dispose();
    }
}
