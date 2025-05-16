package DataAnalysis;


import java.util.Map;

import util.DBUtil;

public class EquipmentBarChart extends AbstractBarChart {

    public EquipmentBarChart(Map<String, Integer> data, String t) {
        super(data, t);
    }

    public static void main(String[] args) {
        Map<String, Integer> data = DBUtil.getEquipmentStatusCount();
        new EquipmentBarChart(data, "Equipment Status Count").display();
    }
}
