package com.warManagementGUI.DataAnalysis;

import java.util.Map;

import com.warManagementGUI.util.DBUtil;

import javafx.stage.Stage;

public class EquipmentBarChart extends AbstractBarChart {

    public EquipmentBarChart(Map<String, Integer> data) {
        super(data, "Equipment Analysis");
    }
    
    public static Stage showEquipmentStatusChart() {
        Map<String, Integer> statusCount = DBUtil.getStatusCount("equipment");
        return showBarChart("Equipment by Status", statusCount);
    }
    
    public static Stage showEquipmentTypeChart() {
        Map<String, Integer> typeCount = DBUtil.getGroupCount("equipment", "equipment_type");
        return showBarChart("Equipment by Type", typeCount);
    }
}
