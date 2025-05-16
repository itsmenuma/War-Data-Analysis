package DataAnalysis;

import java.util.Map;

import util.DBUtil;

public class UnitsBarChart extends AbstractBarChart {

    public UnitsBarChart(Map<String, Integer> data, String t) {
        super(data, t);
    }

    public static void main(String[] args) {
        Map<String, Integer> data = DBUtil.getUnitsByTypeCount();
        new UnitsBarChart(data, "Units Type Count").display();
    }
}
