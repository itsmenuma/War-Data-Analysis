package Equipment;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import DataAnalysis.EquipmentBarChart;
import util.DBUtil;
import warManagement.WarManagement;

public class Equipment_details extends JFrame {
    private static final long serialVersionUID = 1L;
    // constants for UI
    private static final Font LABEL_FONT = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20);
    private static final Font BUTTON_FONT = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 15);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color BG_COLOR = new Color(0, 64, 64);

    private JPanel contentPane;
    private JTextField equip_ID_txt, equip_name_txt, Unit_ID_txt, location_ID_txt;
    private JComboBox<String> equip_type_txt, Status_txt;
    private JTable Equipment_table;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new Equipment_details().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error launching application: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public Equipment_details() {
        initializeFrame();
        setupLabels();
        setupTextFields();
        setupComboBox();
        setupTable();
        setupButtons();
        refreshTableData();
    }

    private void initializeFrame() {
        setTitle("Equipment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 773, 529);
        contentPane = new JPanel();
        contentPane.setBackground(BG_COLOR);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);
    }

    private void setupLabels() {
        createLabel("Equipment Details", new Font("Times New Roman", Font.BOLD | Font.ITALIC, 35), 27, 10, 275, 83);
        createLabel("Equipment ID", LABEL_FONT, 27, 90, 155, 25);
        createLabel("Name", LABEL_FONT, 27, 132, 69, 28);
        createLabel("Type", LABEL_FONT, 27, 186, 69, 25);
        createLabel("Unit ID", LABEL_FONT, 27, 223, 86, 30);
        createLabel("Status", LABEL_FONT, 27, 274, 75, 28);
        createLabel("Location Id", LABEL_FONT, 27, 321, 127, 28);
    }

    private void setupTextFields() {
        equip_ID_txt = createTextField(192, 93, 86, 20);
        equip_name_txt = createTextField(192, 132, 86, 20);
        Unit_ID_txt = createTextField(192, 231, 86, 20);
        location_ID_txt = createTextField(192, 328, 96, 20);
    }

    private void setupComboBox() {
        equip_type_txt = new JComboBox<>(new String[]{"Weapon","Vehicle","Electronic","Other"});
        equip_type_txt.setBounds(192,186,86,21);
        contentPane.add(equip_type_txt);
        Status_txt = new JComboBox<>(new String[]{"Operational","Maintenance","Decommissioned"});
        Status_txt.setBounds(192,280,86,21);
        contentPane.add(Status_txt);
    }

    private void setupTable() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(298, 64, 451, 322);
        contentPane.add(scrollPane);

        Equipment_table = new JTable();
        Equipment_table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"Equipment_ID","Name","Type","Unit ID","Status","Location_ID"}));
        Equipment_table.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 10));
        Equipment_table.setForeground(TEXT_COLOR);
        Equipment_table.setBackground(Color.BLACK);
        scrollPane.setViewportView(Equipment_table);
    }

    private void setupButtons() {
        createButton("Back to Dashboard", 10, 410, 161, 72, e -> { new WarManagement().setVisible(true); dispose(); });
        createButton("Refresh", 192, 410, 96, 72, e -> refreshTextFields());
        createButton("Delete", 310, 410, 86, 72, e -> deleteEquipment());
        createButton("Update", 425, 411, 86, 70, e -> updateEquipment());
        createButton("Insert", 533, 413, 86, 67, e -> insertEquipment());
        createButton("Analyse", 647, 410, 102, 72, e -> analyzeEquipment());
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

    private JButton createButton(String text, int x, int y, int w, int h, ActionListener l) {
        JButton btn = new JButton(text);
        btn.setFont(BUTTON_FONT);
        btn.setBackground(Color.BLACK);
        btn.setForeground(TEXT_COLOR);
        btn.setBounds(x, y, w, h);
        btn.addActionListener(l);
        contentPane.add(btn);
        return btn;
    }

    private void refreshTextFields() {
        equip_ID_txt.setText(""); equip_name_txt.setText(""); Unit_ID_txt.setText(""); location_ID_txt.setText("");
    }

    private void refreshTableData() {
        DefaultTableModel model = (DefaultTableModel)Equipment_table.getModel(); model.setRowCount(0);
        String sql = "SELECT * FROM Equipment";
        try(Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()){
            while(rs.next()) {
                model.addRow(new Object[]{rs.getInt("equipment_id"), rs.getString("name"), rs.getString("type"), rs.getInt("unit_id"), rs.getString("status"), rs.getInt("location_id")});
            }
        } catch(SQLException e) {
            handleDatabaseError(e, "Error retrieving equipment data");
        }
    }

    private void insertEquipment() {
        if(equip_ID_txt.getText().isEmpty()||equip_name_txt.getText().isEmpty()||Unit_ID_txt.getText().isEmpty()||location_ID_txt.getText().isEmpty()){
            JOptionPane.showMessageDialog(this,"Please fill in all fields","Validation Error",JOptionPane.WARNING_MESSAGE);return;
        }
        String sql="INSERT INTO Equipment(equipment_id,name,type,unit_id,status,location_id) VALUES(?,?,?,?,?,?)";
        try(Connection conn=DBUtil.getConnection(); PreparedStatement p=conn.prepareStatement(sql)){
            p.setInt(1,Integer.parseInt(equip_ID_txt.getText()));p.setString(2,equip_name_txt.getText());
            p.setString(3,(String)equip_type_txt.getSelectedItem());p.setInt(4,Integer.parseInt(Unit_ID_txt.getText()));
            p.setString(5,(String)Status_txt.getSelectedItem());p.setInt(6,Integer.parseInt(location_ID_txt.getText()));
            if(p.executeUpdate()>0) JOptionPane.showMessageDialog(this,"Equipment inserted successfully.");
            refreshTextFields();refreshTableData();
        } catch(NumberFormatException e){handleDatabaseError(e,"Invalid number format");}
          catch(SQLException e){handleDatabaseError(e,"Error inserting equipment");}
    }

    private void updateEquipment() {
        if(equip_ID_txt.getText().isEmpty()||equip_name_txt.getText().isEmpty()||Unit_ID_txt.getText().isEmpty()||location_ID_txt.getText().isEmpty()){
            JOptionPane.showMessageDialog(this,"Please fill in all fields","Validation Error",JOptionPane.WARNING_MESSAGE);return;
        }
        String sql="UPDATE Equipment SET name=?,type=?,unit_id=?,status=?,location_id=? WHERE equipment_id=?";
        try(Connection conn=DBUtil.getConnection(); PreparedStatement p=conn.prepareStatement(sql)){
            p.setString(1,equip_name_txt.getText());p.setString(2,(String)equip_type_txt.getSelectedItem());
            p.setInt(3,Integer.parseInt(Unit_ID_txt.getText()));p.setString(4,(String)Status_txt.getSelectedItem());
            p.setInt(5,Integer.parseInt(location_ID_txt.getText()));p.setInt(6,Integer.parseInt(equip_ID_txt.getText()));
            if(p.executeUpdate()>0) JOptionPane.showMessageDialog(this,"Equipment updated successfully.");
            refreshTextFields();refreshTableData();
        } catch(NumberFormatException e){handleDatabaseError(e,"Invalid number format");}
          catch(SQLException e){handleDatabaseError(e,"Error updating equipment");}
    }

    private void deleteEquipment() {
        if(equip_ID_txt.getText().isEmpty()){JOptionPane.showMessageDialog(this,"Please enter Equipment ID","Validation Error",JOptionPane.WARNING_MESSAGE);return;}
        if(JOptionPane.showConfirmDialog(this,"Confirm deletion?","Delete Equipment",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) return;
        String sql="DELETE FROM Equipment WHERE equipment_id=?";
        try(Connection conn=DBUtil.getConnection(); PreparedStatement p=conn.prepareStatement(sql)){
            p.setInt(1,Integer.parseInt(equip_ID_txt.getText()));if(p.executeUpdate()>0) JOptionPane.showMessageDialog(this,"Equipment deleted successfully.");
            refreshTextFields();refreshTableData();
        } catch(NumberFormatException e){handleDatabaseError(e,"Invalid number format");}
          catch(SQLException e){handleDatabaseError(e,"Error deleting equipment");}
    }

    private void analyzeEquipment() {
        Map<String,Integer> data=DBUtil.getEquipmentStatusCount();
        JFrame f=new JFrame("Equipment Status Count");f.setSize(800,600);f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.getContentPane().add(new EquipmentBarChart(data,"Equipment Status Count"));f.setVisible(true);dispose();
    }

    private void handleDatabaseError(Exception e, String msg) {
        System.err.println(msg+": "+e.getMessage());
        JOptionPane.showMessageDialog(this,msg+": "+e.getMessage(),"Database Error",JOptionPane.ERROR_MESSAGE);
    }
}
