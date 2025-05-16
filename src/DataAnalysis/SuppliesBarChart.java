package DataAnalysis;

import java.util.Map;

import javax.swing.JFrame;

import util.DBUtil;

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
