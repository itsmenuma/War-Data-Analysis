package Personnel;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;

import util.AbstractDetailsFrame;
import util.DBUtil;
import warManagement.WarManagement;

public class Personnel_details extends AbstractDetailsFrame {

    private static final long serialVersionUID = 1L;
    private final JTable personnelTable;
    private final DefaultTableModel model;

    public static void main(String[] args) {
        try {
            new Personnel_details().display();
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(),
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Personnel_details() {
        super("Personnel Details", 773, 529);
        createLabel("Personnel Details", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50), Color.WHITE, 29, 11, 394, 58);
        createButton("Back to Dashboard", BUTTON_FONT, TEXT_COLOR, BG_COLOR, 33, 383, 202, 73, e -> { new WarManagement().display(); dispose(); });
        createButton("Log out", BUTTON_FONT, TEXT_COLOR, BG_COLOR, 590, 385, 134, 64, e -> { new Login().display(); dispose(); });

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(39, 79, 697, 274);
        contentPane.add(scrollPane);

        personnelTable = new JTable();
        personnelTable.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
        scrollPane.setViewportView(personnelTable);

        model = new DefaultTableModel(
            new Object[][] {},
            new String[] {"Personnel ID","First Name","Last Name","Post","Unit ID","Role","Status","contact_information"}
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
            handleDatabaseError(e, "Database error");
        }
    }

    // use inherited createLabel and createButton
}
