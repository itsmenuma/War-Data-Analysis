package com.warManagementGUI.DataAnalysis;

import java.util.Map;

import javax.swing.JFrame;

import com.warManagementGUI.util.DBUtil;

public class UnitsBarChart extends AbstractBarChart {
    private static final long serialVersionUID = 1L;

    public UnitsBarChart(Map<String, Integer> data) {
        super(data, "Units by Type");
    }
    
    public static void showUnitStatusChart() {
        Map<String, Integer> statusCount = DBUtil.getStatusCount("units");
        showBarChart("Units by Status", statusCount);
    }
    
    public static JFrame showUnitTypeChart() {
        Map<String, Integer> typeCount = DBUtil.getGroupCount("units", "unit_type");
        return showBarChart("Units by Type", typeCount);
    }
}
