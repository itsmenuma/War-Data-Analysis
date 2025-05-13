package Personnel;


import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JButton;
//import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import warManagement.WarManagement;

import javax.swing.JScrollPane;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;

public class Personnel_details extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable personnelTable;
    private DefaultTableModel model;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Personnel_details frame = new Personnel_details();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
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

        JLabel lblNewLabel = new JLabel("Personnel Details");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50));
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setBounds(29, 11, 394, 58);
        contentPane.add(lblNewLabel);

        JButton btnNewButton = new JButton("Back to Dashboard");
        btnNewButton.addActionListener(e -> {
            WarManagement dashboard = new WarManagement();
            dashboard.setVisible(true);
            dispose();
        });
        btnNewButton.setBackground(new Color(0, 0, 0));
        btnNewButton.setForeground(new Color(255, 255, 255));
        btnNewButton.setBounds(33, 383, 202, 73);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Log out");
        btnNewButton_1.setHorizontalAlignment(SwingConstants.LEFT);
        btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
        btnNewButton_1.addActionListener(e -> {
            Login login = new Login();
            login.setVisible(true);
            dispose();
        });
        btnNewButton_1.setBackground(new Color(0, 0, 0));
        btnNewButton_1.setForeground(new Color(255, 255, 255));
        btnNewButton_1.setBounds(590, 385, 134, 64);
        contentPane.add(btnNewButton_1);

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
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/war", "root", "SP1234sp()");
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
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
