package Personnel;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import warManagement.WarManagement;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
//import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class Personnel_Update extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField Personnel_ID_txt;
	private JTextField FirstName_txt;
	private JTextField LastName_txt;
	private JTextField Post_txt;
	private JTextField Unit_ID_txt;
	private JTextField Role_txt;
	private JTextField Contact_details_txt;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Personnel_Update frame = new Personnel_Update();
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Personnel_Update() {
		setTitle("Personnel_Update");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 773, 529);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 64, 64));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Personnel Update Page");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50));
		lblNewLabel.setBackground(new Color(0, 0, 0));
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(10, 10, 502, 84);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Personnel_ID");
		lblNewLabel_1.setBackground(new Color(0, 0, 0));
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_1.setBounds(31, 104, 148, 35);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("First Name");
		lblNewLabel_1_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_1_1.setBackground(new Color(0, 0, 0));
		lblNewLabel_1_1.setBounds(31, 161, 127, 35);
		contentPane.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_2 = new JLabel("Last Name");
		lblNewLabel_1_2.setForeground(new Color(255, 255, 255));
		lblNewLabel_1_2.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_1_2.setBackground(new Color(0, 0, 0));
		lblNewLabel_1_2.setBounds(31, 206, 127, 35);
		contentPane.add(lblNewLabel_1_2);
		
		JLabel lblNewLabel_1_2_1 = new JLabel("Post");
		lblNewLabel_1_2_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1_2_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_1_2_1.setBackground(new Color(0, 0, 0));
		lblNewLabel_1_2_1.setBounds(31, 257, 119, 35);
		contentPane.add(lblNewLabel_1_2_1);
		
		JLabel lblNewLabel_1_3 = new JLabel("Unit ID");
		lblNewLabel_1_3.setForeground(new Color(255, 255, 255));
		lblNewLabel_1_3.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_1_3.setBackground(new Color(0, 0, 0));
		lblNewLabel_1_3.setBounds(414, 104, 132, 35);
		contentPane.add(lblNewLabel_1_3);
		
		JLabel lblNewLabel_1_4 = new JLabel("Role");
		lblNewLabel_1_4.setForeground(new Color(255, 255, 255));
		lblNewLabel_1_4.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_1_4.setBackground(new Color(0, 0, 0));
		lblNewLabel_1_4.setBounds(414, 161, 142, 35);
		contentPane.add(lblNewLabel_1_4);
		
		JLabel lblNewLabel_1_5 = new JLabel("Status");
		lblNewLabel_1_5.setForeground(new Color(255, 255, 255));
		lblNewLabel_1_5.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_1_5.setBackground(new Color(0, 0, 0));
		lblNewLabel_1_5.setBounds(414, 206, 142, 35);
		contentPane.add(lblNewLabel_1_5);
		
		JLabel lblNewLabel_1_6 = new JLabel("Contact Details");
		lblNewLabel_1_6.setForeground(new Color(255, 255, 255));
		lblNewLabel_1_6.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_1_6.setBackground(new Color(0, 0, 0));
		lblNewLabel_1_6.setBounds(414, 257, 148, 35);
		contentPane.add(lblNewLabel_1_6);
		
		Personnel_ID_txt = new JTextField();
		Personnel_ID_txt.setBounds(209, 104, 96, 19);
		contentPane.add(Personnel_ID_txt);
		Personnel_ID_txt.setColumns(10);
		
		FirstName_txt = new JTextField();
		FirstName_txt.setColumns(10);
		FirstName_txt.setBounds(209, 161, 96, 19);
		contentPane.add(FirstName_txt);
		
		LastName_txt = new JTextField();
		LastName_txt.setColumns(10);
		LastName_txt.setBounds(209, 216, 96, 19);
		contentPane.add(LastName_txt);
		
		Post_txt = new JTextField();
		Post_txt.setColumns(10);
		Post_txt.setBounds(209, 267, 96, 19);
		contentPane.add(Post_txt);
		
		Unit_ID_txt = new JTextField();
		Unit_ID_txt.setColumns(10);
		Unit_ID_txt.setBounds(574, 114, 96, 19);
		contentPane.add(Unit_ID_txt);
		
		Role_txt = new JTextField();
		Role_txt.setColumns(10);
		Role_txt.setBounds(574, 161, 96, 19);
		contentPane.add(Role_txt);
		
		Contact_details_txt = new JTextField();
		Contact_details_txt.setColumns(10);
		Contact_details_txt.setBounds(574, 267, 96, 19);
		contentPane.add(Contact_details_txt);
		
		JComboBox Status_txt = new JComboBox();
		Status_txt.setModel(new DefaultComboBoxModel(new String[] {"active", "injured", "MIA", "KIA"}));
		Status_txt.setBounds(566, 208, 127, 35);
		contentPane.add(Status_txt);
		
		
		JButton btnNewButton = new JButton("Back to Dashboard");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WarManagement dashboard=new WarManagement();
				dashboard.setVisible(true);
				dispose();
			}
		});
		btnNewButton.setBackground(new Color(0, 0, 0));
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		btnNewButton.setBounds(10, 358, 207, 58);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Refresh");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshTextFields();
			}
			private void refreshTextFields() {
				Personnel_ID_txt.setText("");
				FirstName_txt.setText("");
				LastName_txt.setText("");
				Post_txt.setText("");
				Unit_ID_txt.setText("");
				Role_txt.setText("");
				
				Contact_details_txt.setText("");
				
			}
		});
		btnNewButton_1.setBackground(new Color(0, 0, 0));
		btnNewButton_1.setForeground(new Color(255, 255, 255));
		btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		btnNewButton_1.setBounds(319, 355, 127, 64);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_3 = new JButton("Update");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String url = "jdbc:mysql://localhost:3306/war";
			    String user = "root";
			    String password = "rayees@123";

			    String updateQuery = "UPDATE personnel SET first_Name=?, last_Name=?, post=?, unit_ID=?, role=?, status=?, contact_information=? WHERE personnel_id=?";

			    try (Connection conn = DriverManager.getConnection(url, user, password);
			         PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

			        pstmt.setString(1, FirstName_txt.getText());
			        pstmt.setString(2, LastName_txt.getText());
			        pstmt.setString(3, Post_txt.getText());
			        pstmt.setString(4, Unit_ID_txt.getText());
			        pstmt.setString(5, Role_txt.getText());
			        pstmt.setString(6, (String) Status_txt.getSelectedItem());

			        pstmt.setString(7, Contact_details_txt.getText());
			        pstmt.setString(8, Personnel_ID_txt.getText());

			        int rowsAffected = pstmt.executeUpdate();

			        if (rowsAffected > 0) {
			            System.out.println("Update successful.");
			            Personnel_details personnelDetails=new Personnel_details();
			            personnelDetails.setVisible(true);
			            dispose();
			        } else {
			        	JOptionPane.showMessageDialog(Personnel_Update.this,"Update failed. Personnel ID not found.");
			        }

			    } catch (SQLException e1) {
			        e1.printStackTrace();
			    }
			}
		});
		btnNewButton_3.setBackground(new Color(0, 0, 0));
		btnNewButton_3.setForeground(new Color(255, 255, 255));
		btnNewButton_3.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		btnNewButton_3.setBounds(602, 355, 119, 58);
		contentPane.add(btnNewButton_3);
		
		
		
	}
}
