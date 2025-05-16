package DataAnalysis;
import java.util.Map;

import util.DBUtil;

public class BarChartExample extends AbstractBarChart {

    public BarChartExample(Map<String, Integer> data, String t) {
        super(data, t);
    }

    public static void main(String[] args) {
        Map<String, Integer> data = DBUtil.getPersonnelStatusCount();
        new BarChartExample(data, "Personnel Status Count").display();
    }
}