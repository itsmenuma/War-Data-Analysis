package com.warManagementGUI.DataAnalysis;

import java.util.Map;

import javax.swing.JFrame;

import com.warManagementGUI.util.DBUtil;

public class EquipmentBarChart extends AbstractBarChart {
    private static final long serialVersionUID = 1L;

    public EquipmentBarChart(Map<String, Integer> data) {
        super(data, "Equipment Analysis");
    }
    
    public static JFrame showEquipmentStatusChart() {
        Map<String, Integer> statusCount = DBUtil.getStatusCount("equipment");
        return showBarChart("Equipment by Status", statusCount);
    }
    
    public static JFrame showEquipmentTypeChart() {
        Map<String, Integer> typeCount = DBUtil.getGroupCount("equipment", "equipment_type");
        return showBarChart("Equipment by Type", typeCount);
    }
}
