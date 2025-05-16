package DataAnalysis;

import java.util.Map;

import util.DBUtil;

public class MissionsBarChart extends AbstractBarChart {

    public MissionsBarChart(Map<String, Integer> data, String t) {
        super(data, t);
    }

    public static void main(String[] args) {
        Map<String, Integer> data = DBUtil.getMissionsStatusCount();
        new MissionsBarChart(data, "Mission Status Count").display();
    }
}
