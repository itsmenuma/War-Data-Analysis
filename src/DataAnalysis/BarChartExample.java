package DataAnalysis;
import java.util.Map;

import javax.swing.JFrame;

import util.DBUtil;

public class BarChartExample extends AbstractBarChart {

    public BarChartExample(Map<String, Integer> data, String t) {
        super(data, t);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Map<String, Integer> data = DBUtil.getPersonnelStatusCount();
        frame.getContentPane().add(new BarChartExample(data, "Personnel Status Count"));

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}