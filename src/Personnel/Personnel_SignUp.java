package Personnel;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import util.DBUtil;
import warManagement.WarManagement;

public class Personnel_SignUp extends JFrame {
    private static final long serialVersionUID = 1L;
    // UI constants
    private static final Font TITLE_FONT = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50);
    private static final Font LABEL_FONT = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20);
    private static final Font BUTTON_FONT = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color BG_COLOR = new Color(0, 64, 64);

    private JPanel contentPane;
    private JTextField Personnel_ID_txt, Fname_txt, Lname_txt, Post_txt, Unit_ID_txt, Role_txt, Contact_details_txt;
    private JComboBox<String> Status_txt;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new Personnel_SignUp().setVisible(true));
    }

    public Personnel_SignUp() {
        initializeFrame();
        setupLabels();
        setupTextFields();
        setupComboBox();
        setupButtons();
    }

    private void initializeFrame() {
        setTitle("Personnel Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 773, 529);
        contentPane = new JPanel();
        contentPane.setBackground(BG_COLOR);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);
    }

    private void setupLabels() {
        createLabel("Personnel Sign Up", TITLE_FONT, 38, 26, 406, 81);
        createLabel("Personnel ID", LABEL_FONT, 38, 117, 129, 30);
        createLabel("First Name", LABEL_FONT, 38, 169, 139, 30);
        createLabel("Last Name", LABEL_FONT, 38, 232, 139, 30);
        createLabel("Post", LABEL_FONT, 38, 290, 63, 35);
        createLabel("Unit ID", LABEL_FONT, 390, 117, 85, 30);
        createLabel("Role", LABEL_FONT, 390, 172, 69, 25);
        createLabel("Status", LABEL_FONT, 390, 236, 69, 22);
        createLabel("Contact Details", LABEL_FONT, 390, 295, 139, 24);
    }

    private void setupTextFields() {
        Personnel_ID_txt = createTextField(201, 117, 96, 19);
        Fname_txt = createTextField(201, 177, 96, 19);
        Lname_txt = createTextField(201, 240, 96, 19);
        Post_txt = createTextField(201, 300, 96, 19);
        Unit_ID_txt = createTextField(565, 125, 96, 19);
        Role_txt = createTextField(565, 177, 96, 19);
        Contact_details_txt = createTextField(565, 300, 96, 19);
    }

    private void setupComboBox() {
        Status_txt = createComboBox(new String[]{"active", "injured", "MIA", "KIA"}, 565, 237, 96, 21);
    }

    private void setupButtons() {
        createButton("Back to Dashboard", 10, 380, 228, 47, e -> navigateToDashboard());
        createButton("Reset", 313, 380, 145, 47, e -> refreshTextFields());
        createButton("Sign Up", 549, 380, 145, 47, e -> insertPersonnel());
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
        cb.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
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
        Fname_txt.setText("");
        Lname_txt.setText("");
        Post_txt.setText("");
        Unit_ID_txt.setText("");
        Role_txt.setText("");
        Contact_details_txt.setText("");
        Status_txt.setSelectedIndex(0);
    }

    private void insertPersonnel() {
        String personnelId = Personnel_ID_txt.getText().trim();
        String firstName = Fname_txt.getText().trim();
        String lastName = Lname_txt.getText().trim();
        String post = Post_txt.getText().trim();
        String unitId = Unit_ID_txt.getText().trim();
        String role = Role_txt.getText().trim();
        String status = (String) Status_txt.getSelectedItem();
        String contactInfo = Contact_details_txt.getText().trim();

        if (isAnyFieldEmpty(personnelId, firstName, lastName, post, unitId, role, status, contactInfo)) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO Personnel(Personnel_id, First_name, Last_name, Post, Unit_Id, Role, Status, contact_information) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, personnelId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, post);
            pstmt.setString(5, unitId);
            pstmt.setString(6, role);
            pstmt.setString(7, status);
            pstmt.setString(8, contactInfo);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Personnel details inserted successfully.");
                new Personnel_details().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to insert personnel details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            handleDatabaseError(e, "Error inserting personnel");
        }
    }

    private boolean isAnyFieldEmpty(String... fields) {
        for (String f : fields) {
            if (f == null || f.isEmpty()) return true;
        }
        return false;
    }

    private void handleDatabaseError(SQLException e, String message) {
        System.err.println(message + ": " + e.getMessage());
        JOptionPane.showMessageDialog(this, message + ": " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

