package Personnel;


import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.Serial;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import DataAnalysis.BarChartExample;
import util.DBUtil;
import warManagement.WarManagement;

public class Login extends JFrame {

	@Serial
	private static final long serialVersionUID = 1L;
	private final JPanel contentPane;
	private final JTextField Personnel_ID_txt;
	private final JTextField FirstName_txt;
	private final JTextField Role_txt;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
                    try {
                        Login frame = new Login();
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
	public Login() {
		setTitle("Personnel_Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 773, 529);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(255, 255, 255));
		contentPane.setBackground(new Color(0, 64, 64));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		createIconLabel("/pics/login.jpeg", 37, 82, 169, 265, contentPane);

		createLabel("Personnel Login Page", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50), Color.WHITE, 91, 24, 622, 41, contentPane);

		createLabel("Personnel ID", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), Color.WHITE, 272, 137, 128, 53, contentPane);

		createLabel("First  Name", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), Color.WHITE, 272, 201, 128, 53, contentPane);

		createLabel("Role", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20), Color.WHITE, 284, 265, 116, 53, contentPane);

		Personnel_ID_txt = createTextField(473, 155, 150, 20, 10, contentPane);

		FirstName_txt = createTextField(473, 219, 150, 20, 10, contentPane);

		Role_txt = createTextField(473, 283, 155, 20, 10, contentPane);

		createButton("login", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15), Color.WHITE, Color.BLACK,
			572, 420, 89, 41,
			e -> {
				String personnelId = Personnel_ID_txt.getText();
				String firstName = FirstName_txt.getText();
				String role = Role_txt.getText();
				if (checkCredentials(personnelId, firstName, role)) {
					new Personnel_details().setVisible(true);
					dispose();
				} else {
					JOptionPane.showMessageDialog(contentPane, "Invalid credentials. Please try again.");
				}
			}, contentPane);

		createButton("remove", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15), Color.WHITE, Color.BLACK,
			325, 420, 119, 41,
			e -> { new Personnel_Remove_Details().setVisible(true); dispose(); }, contentPane);

		createButton("update", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15), Color.WHITE, Color.BLACK,
			454, 420, 108, 41,
			e -> { new Personnel_Update().setVisible(true); dispose(); }, contentPane);

		createButton("Back to Dashboard", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15), Color.WHITE, Color.BLACK,
			10, 420, 177, 41,
			e -> { new WarManagement().setVisible(true); dispose(); }, contentPane);

		createButton("Sign Up", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15), Color.WHITE, Color.BLACK,
			197, 420, 118, 41,
			e -> { new Personnel_SignUp().setVisible(true); dispose(); }, contentPane);

		createButton("Analyse", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15), Color.WHITE, Color.BLACK,
			671, 420, 85, 41,
			e -> {
				Map<String, Integer> data = DBUtil.getPersonnelStatusCount();
				JFrame chartFrame = new JFrame("Personnel Status Count");
				chartFrame.setSize(800, 600);
				chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				chartFrame.getContentPane().add(new BarChartExample(data, "Personnel Status Count"));
				chartFrame.setVisible(true);
				dispose();
			}, contentPane);
	}

	private boolean checkCredentials(String personnelId, String firstName, String role) {
		try (Connection conn = DBUtil.getConnection();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery("SELECT * FROM Personnel WHERE Personnel_id='" + personnelId + "' AND First_name='" + firstName + "' AND Role='" + role + "'")) {

			return rs.next(); // If there's a result, credentials are valid

		} catch (Exception e) {
			System.err.println("Database error: " + e.getMessage());
			JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}

	// Helper to create icon labels
	private JLabel createIconLabel(String path, int x, int y, int w, int h, JPanel panel) {
		JLabel lbl = new JLabel("");
		lbl.setIcon(new ImageIcon(getClass().getResource(path)));
		lbl.setBounds(x, y, w, h);
		panel.add(lbl);
		return lbl;
	}

	// Helper to create text labels
	private JLabel createLabel(String text, Font font, Color fg, int x, int y, int w, int h, JPanel panel) {
		JLabel lbl = new JLabel(text);
		lbl.setFont(font);
		lbl.setForeground(fg);
		lbl.setBounds(x, y, w, h);
		panel.add(lbl);
		return lbl;
	}

	// Helper to create text fields
	private JTextField createTextField(int x, int y, int w, int h, int cols, JPanel panel) {
		JTextField tf = new JTextField();
		tf.setBounds(x, y, w, h);
		tf.setColumns(cols);
		panel.add(tf);
		return tf;
	}

	// Helper to create buttons
	private JButton createButton(String text, Font font, Color fg, Color bg,
								 int x, int y, int w, int h,
								 java.awt.event.ActionListener al,
								 JPanel panel) {
		JButton btn = new JButton(text);
		btn.setFont(font);
		btn.setForeground(fg);
		btn.setBackground(bg);
		btn.setBounds(x, y, w, h);
		btn.addActionListener(al);
		panel.add(btn);
		return btn;
	}
}