package com.warManagementGUI.DataAnalysis;

import java.util.Map;

import com.warManagementGUI.util.DBUtil;

public class EquipmentBarChart extends AbstractBarChart {
    private static final long serialVersionUID = 1L;

    public EquipmentBarChart(Map<String, Integer> data) {
        super(data, "Equipment Analysis");
    }
    
    public static void showEquipmentStatusChart() {
        Map<String, Integer> statusCount = DBUtil.getStatusCount("equipment");
        showBarChart("Equipment by Status", statusCount);
    }
    
    public static void showEquipmentTypeChart() {
        Map<String, Integer> typeCount = DBUtil.getGroupCount("equipment", "equipment_type");
        showBarChart("Equipment by Type", typeCount);
    }
}
