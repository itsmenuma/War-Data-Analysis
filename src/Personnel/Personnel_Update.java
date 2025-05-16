package Personnel;

import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import util.DBUtil;
import warManagement.WarManagement;

public class Personnel_Update extends JFrame {
    private static final long serialVersionUID = 1L;
    // UI constants
    private static final Font TITLE_FONT = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50);
    private static final Font LABEL_FONT = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20);
    private static final Font BUTTON_FONT = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color BG_COLOR = new Color(0, 64, 64);

    private JPanel contentPane;
    private JTextField Personnel_ID_txt, FirstName_txt, LastName_txt, Post_txt, Unit_ID_txt, Role_txt, Contact_details_txt;
    private JComboBox<String> Status_txt;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new Personnel_Update().setVisible(true));
    }

    public Personnel_Update() {
        initializeFrame();
        setupLabels();
        setupTextFields();
        setupComboBox();
        setupButtons();
    }

    private void initializeFrame() {
        setTitle("Personnel Update");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 773, 529);
        contentPane = new JPanel();
        contentPane.setBackground(BG_COLOR);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);
    }

    private void setupLabels() {
        createLabel("Personnel Update Page", TITLE_FONT, 10, 10, 502, 84);
        createLabel("Personnel ID", LABEL_FONT, 31, 104, 148, 35);
        createLabel("First Name", LABEL_FONT, 31, 161, 127, 35);
        createLabel("Last Name", LABEL_FONT, 31, 206, 127, 35);
        createLabel("Post", LABEL_FONT, 31, 257, 119, 35);
        createLabel("Unit ID", LABEL_FONT, 414, 104, 132, 35);
        createLabel("Role", LABEL_FONT, 414, 161, 142, 35);
        createLabel("Status", LABEL_FONT, 414, 206, 142, 35);
        createLabel("Contact Details", LABEL_FONT, 414, 257, 148, 35);
    }

    private void setupTextFields() {
        Personnel_ID_txt = createTextField(209, 104, 96, 19);
        FirstName_txt   = createTextField(209, 161, 96, 19);
        LastName_txt    = createTextField(209, 216, 96, 19);
        Post_txt        = createTextField(209, 267, 96, 19);
        Unit_ID_txt     = createTextField(574, 114, 96, 19);
        Role_txt        = createTextField(574, 161, 96, 19);
        Contact_details_txt = createTextField(574, 267, 96, 19);
    }

    private void setupComboBox() {
        Status_txt = createComboBox(
            new String[]{"active","injured","MIA","KIA"}, 566, 208, 127, 35
        );
    }

    private void setupButtons() {
        createButton("Back to Dashboard", 10, 358, 207, 58, e -> navigateToDashboard());
        createButton("Refresh", 319, 355, 127, 64, e -> refreshTextFields());
        createButton("Update", 602, 355, 119, 58, e -> updatePersonnel());
    }

    private JLabel createLabel(String text, Font font, int x, int y, int w, int h) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(TEXT_COLOR);
        lbl.setBounds(x, y, w, h);
        contentPane.add(lbl);
        return lbl;
    }

    private JTextField createTextField(int x, int y, int w, int h) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, w, h);
        tf.setColumns(10);
        contentPane.add(tf);
        return tf;
    }

    private JComboBox<String> createComboBox(String[] items, int x, int y, int w, int h) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(LABEL_FONT);
        cb.setBounds(x, y, w, h);
        contentPane.add(cb);
        return cb;
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

    private void navigateToDashboard() {
        new WarManagement().setVisible(true);
        dispose();
    }

    private void refreshTextFields() {
        Personnel_ID_txt.setText("");
        FirstName_txt.setText("");
        LastName_txt.setText("");
        Post_txt.setText("");
        Unit_ID_txt.setText("");
        Role_txt.setText("");
        Contact_details_txt.setText("");
        Status_txt.setSelectedIndex(0);
    }

    private void updatePersonnel() {
        String sql = "UPDATE personnel SET first_Name=?, last_Name=?, post=?, unit_ID=?, role=?, status=?, contact_information=? WHERE personnel_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, FirstName_txt.getText());
            pstmt.setString(2, LastName_txt.getText());
            pstmt.setString(3, Post_txt.getText());
            pstmt.setString(4, Unit_ID_txt.getText());
            pstmt.setString(5, Role_txt.getText());
            pstmt.setString(6, (String) Status_txt.getSelectedItem());
            pstmt.setString(7, Contact_details_txt.getText());
            pstmt.setString(8, Personnel_ID_txt.getText());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Personnel updated successfully.");
                new Personnel_details().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Update failed. Personnel ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            handleDatabaseError(e, "Error updating personnel");
        }
    }

    private void handleDatabaseError(SQLException e, String message) {
        System.err.println(message + ": " + e.getMessage());
        JOptionPane.showMessageDialog(this, message + ": " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}
