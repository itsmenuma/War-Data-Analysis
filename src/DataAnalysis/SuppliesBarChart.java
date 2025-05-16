package DataAnalysis;

import java.util.Map;

import util.DBUtil;

public class SuppliesBarChart extends AbstractBarChart {

    public SuppliesBarChart(Map<String, Integer> data, String t) {
        super(data, t);
    }

    public static void main(String[] args) {
        Map<String, Integer> data = DBUtil.getSuppliesStatusCount();
        new SuppliesBarChart(data, "Supply Status Count").display();
    }
}
