package DataAnalysis;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import util.DBUtil;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;

public class SuppliesBarChart extends AbstractBarChart {

    public SuppliesBarChart(Map<String, Integer> data, String t) {
        super(data, t);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Map<String, Integer> data = DBUtil.getSuppliesStatusCount();
        frame.getContentPane().add(new SuppliesBarChart(data, "Supply Status Count"));

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
