package DataAnalysis;


import javax.swing.*;
import java.awt.*;
import java.util.Map;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import util.DBUtil;

public class EquipmentBarChart extends AbstractBarChart {

    public EquipmentBarChart(Map<String, Integer> data, String t) {
        super(data, t);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Map<String, Integer> data = DBUtil.getEquipmentStatusCount();
        frame.getContentPane().add(new EquipmentBarChart(data, "Equipment Status Count"));

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
