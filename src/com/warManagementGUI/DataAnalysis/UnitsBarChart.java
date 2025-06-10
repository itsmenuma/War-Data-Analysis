package com.warManagementGUI.DataAnalysis;

import java.util.Map;

import com.warManagementGUI.util.DBUtil;

import javafx.stage.Stage;

public class UnitsBarChart extends AbstractBarChart {

    public UnitsBarChart(Map<String, Integer> data) {
        super(data, "Units by Type");
    }
    /* Deprecated Method since there is no column status in UNITS */
    // public static Stage showUnitStatusChart() {
    // Map<String, Integer> statusCount = DBUtil.getStatusCount("units");
    // return showBarChart("Units by Status", statusCount);
    // }

    public static Stage showUnitTypeChart() {
        Map<String, Integer> typeCount = DBUtil.getGroupCount("units", "unit_type");
        return showBarChart("Units by Type", typeCount);
    }
}
