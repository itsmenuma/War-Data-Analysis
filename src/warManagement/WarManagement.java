package warManagement;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Equipment.Equipment_details;
import Mission.Mission_details;
import Personnel.Login;
import Supply.Supply_details;
import Units.Units_Interface;

public class WarManagement extends JFrame {

	@Serial
	private static final long serialVersionUID = 1L;
    private final JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
                    try {
                        WarManagement frame = new WarManagement();
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
	public WarManagement() {
		setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 30));
		setTitle("WarAnalyze");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 773, 529);
        contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 64, 64));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		createIconLabel("/pics/PI.jpg", 28, 40, 109, 109);
		
		createButton("Personnel", Login.class, 28, 173, 128, 23);
		
		createIconLabel("/pics/UI.png", 186, 47, 89, 101);

		createButton("Units", Units_Interface.class, 188, 173, 103, 23);
		
		createIconLabel("/pics/MI.png", 313, 47, 109, 101);
		
		createButton("Missions", Mission_details.class, 313, 173, 109, 23);
		
		createIconLabel("/pics/EI.png", 466, 47, 109, 101);
		
		createButton("Equipments", Equipment_details.class, 454, 173, 147, 23);
		
		createIconLabel("/pics/SI.png", 615, 47, 103, 101);
		
		createButton("Supplies", Supply_details.class, 609, 173, 109, 23);
		
		createTextLabel("War Analysis Dashboard", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50), new Color(255, 255, 255), 55, 195, 651, 101);
		
		createIconLabel("/pics/quote1.jpg", 158, 290, 438, 176);
		
		createLabel("New label", 55, 77, 46, 14);
	}

	private <T extends JFrame> void createButton(String buttonName, Class<T> frameClass, int x, int y, int width, int height) {
		JButton newButton = new JButton(buttonName);
		newButton.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		newButton.addActionListener(e -> {
            try {
                T infoClass = frameClass.getDeclaredConstructor().newInstance();
                infoClass.setVisible(true);
                dispose();
            } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
                System.err.println("Database error: " + ex.getMessage());
                JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
		newButton.setForeground(new Color(255, 255, 255));
		newButton.setBackground(new Color(0, 0, 0));
		newButton.setBounds(x, y, width, height);
		contentPane.add(newButton);
	}
	
	private void createIconLabel(String iconPath, int x, int y, int width, int height) {
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(Objects.requireNonNull(WarManagement.class.getResource(iconPath))));
		label.setBounds(x, y, width, height);
		contentPane.add(label);
	}

	private void createTextLabel(String text, Font font, Color fg, int x, int y, int width, int height) {
		JLabel label = new JLabel(text);
		label.setFont(font);
		label.setForeground(fg);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(x, y, width, height);
		contentPane.add(label);
	}

	private void createLabel(String text, int x, int y, int width, int height) {
		JLabel label = new JLabel(text);
		label.setBounds(x, y, width, height);
		contentPane.add(label);
	}
}
