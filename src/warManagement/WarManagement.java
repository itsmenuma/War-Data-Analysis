package warManagement;


//import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Equipment.Equipment_details;
import Mission.Mission_details;
import Personnel.Login;
import Supply.Supply_details;
import Units.Units_Interface;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WarManagement extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WarManagement frame = new WarManagement();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(WarManagement.class.getResource("/pics/PI.jpg")));
		lblNewLabel.setBounds(28, 40, 109, 109);
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Personnel");
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login personnel_login=new Login();
				personnel_login.setVisible(true);
				dispose();
			}
		});
		btnNewButton.setBackground(new Color(0, 0, 0));
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setBounds(28, 173, 128, 23);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(WarManagement.class.getResource("/pics/UI.png")));
		lblNewLabel_1.setBounds(186, 47, 89, 101);
		contentPane.add(lblNewLabel_1);
		
		JButton btnNewButton_1 = new JButton("Units");
		btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Units_Interface unitInfo=new Units_Interface();
				unitInfo.setVisible(true);
				dispose();
			}
		});
		btnNewButton_1.setForeground(new Color(255, 255, 255));
		btnNewButton_1.setBackground(new Color(0, 0, 0));
		btnNewButton_1.setBounds(188, 173, 103, 23);
		contentPane.add(btnNewButton_1);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(WarManagement.class.getResource("/pics/MI.png")));
		lblNewLabel_2.setBounds(313, 47, 109, 101);
		contentPane.add(lblNewLabel_2);
		
		JButton btnNewButton_2 = new JButton("Missions");
		btnNewButton_2.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mission_details missionDetails=new Mission_details();
				missionDetails.setVisible(true);
				dispose();
			}
		});
		btnNewButton_2.setForeground(new Color(255, 255, 255));
		btnNewButton_2.setBackground(new Color(0, 0, 0));
		btnNewButton_2.setBounds(313, 173, 109, 23);
		contentPane.add(btnNewButton_2);
		
		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setIcon(new ImageIcon(WarManagement.class.getResource("/pics/EI.png")));
		lblNewLabel_3.setBounds(466, 47, 109, 101);
		contentPane.add(lblNewLabel_3);
		
		JButton btnNewButton_3 = new JButton("Equipments");
		btnNewButton_3.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Equipment_details equipmentDetails=new Equipment_details();
				equipmentDetails.setVisible(true);
				dispose();
			}
		});
		btnNewButton_3.setForeground(new Color(255, 255, 255));
		btnNewButton_3.setBackground(new Color(0, 0, 0));
		btnNewButton_3.setBounds(454, 173, 147, 23);
		contentPane.add(btnNewButton_3);
		
		JLabel lblNewLabel_4 = new JLabel("");
		lblNewLabel_4.setIcon(new ImageIcon(WarManagement.class.getResource("/pics/SI.png")));
		lblNewLabel_4.setBounds(615, 47, 103, 101);
		contentPane.add(lblNewLabel_4);
		
		JButton btnNewButton_4 = new JButton("Supplies");
		btnNewButton_4.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Supply_details supplyDetails=new Supply_details();
				supplyDetails.setVisible(true);
				dispose();
			}
		});
		btnNewButton_4.setForeground(new Color(255, 255, 255));
		btnNewButton_4.setBackground(new Color(0, 0, 0));
		btnNewButton_4.setBounds(609, 173, 109, 23);
		contentPane.add(btnNewButton_4);
		
		JLabel lblNewLabel_5 = new JLabel("War Analysis Dashboard");
		lblNewLabel_5.setForeground(new Color(255, 255, 255));
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50));
		lblNewLabel_5.setBounds(55, 195, 651, 101);
		contentPane.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("");
		lblNewLabel_6.setIcon(new ImageIcon(WarManagement.class.getResource("/pics/quote1.jpg")));
		lblNewLabel_6.setBounds(158, 290, 438, 176);
		contentPane.add(lblNewLabel_6);
		
		JLabel label = new JLabel("New label");
		label.setBounds(55, 77, 46, 14);
		contentPane.add(label);
	}
}
