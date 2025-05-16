package Personnel;


import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;

import util.DBUtil;
import warManagement.WarManagement;

public class Personnel_details extends JFrame {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPane;
    private final JTable personnelTable;
    private final DefaultTableModel model;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Personnel_details frame = new Personnel_details();
                frame.setVisible(true);
            } catch (Exception e) {
                System.err.println("Database error: " + e.getMessage());
                JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Create the frame.
     */
    public Personnel_details() {
        setTitle("Personnel Details");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 773, 529);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 64, 64));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        createLabel("Personnel Details", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50), Color.WHITE, 29, 11, 394, 58);

        createButton("Back to Dashboard", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15), Color.WHITE, Color.BLACK, 33, 383, 202, 73,
            e -> { new WarManagement().setVisible(true); dispose(); });

        JButton logoutBtn = createButton("Log out", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), Color.WHITE, Color.BLACK, 590, 385, 134, 64,
            e -> { new Login().setVisible(true); dispose(); });
        logoutBtn.setHorizontalAlignment(SwingConstants.LEFT);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(39, 79, 697, 274);
        contentPane.add(scrollPane);

        personnelTable = new JTable();
        personnelTable.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
        scrollPane.setViewportView(personnelTable);
        
        model = new DefaultTableModel(
            new Object[][] {},
            new String[] {
                "Personnel ID", "First Name", "Last Name", "Post", "Unit ID", "Role", "Status", "contact_information"
            }
        );
        personnelTable.setModel(model);
        personnelTable.setForeground(new Color(255, 255, 255));
        personnelTable.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 10));
        personnelTable.setBackground(new Color(0, 0, 0));

        loadPersonnelData();
    }

    private void loadPersonnelData() {
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Personnel")) {

            while (rs.next()) {
                String personnelId = rs.getString("Personnel_id");
                String firstName = rs.getString("First_name");
                String lastName = rs.getString("Last_name");
                String post = rs.getString("Post");
                String unitId = rs.getString("Unit_Id");
                String role = rs.getString("Role");
                String status = rs.getString("Status");
                String contact_information = rs.getString("contact_information");

                model.addRow(new Object[]{personnelId, firstName, lastName, post, unitId, role, status, contact_information});
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper to create labels
    private JLabel createLabel(String text, Font font, Color fg, int x, int y, int w, int h) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(fg);
        lbl.setBounds(x, y, w, h);
        contentPane.add(lbl);
        return lbl;
    }

    // Helper to create buttons
    private JButton createButton(String text, Font font, Color fg, Color bg,
                                  int x, int y, int w, int h,
                                  java.awt.event.ActionListener al) {
        JButton btn = new JButton(text);
        btn.setFont(font);
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setBounds(x, y, w, h);
        btn.addActionListener(al);
        contentPane.add(btn);
        return btn;
    }
}
