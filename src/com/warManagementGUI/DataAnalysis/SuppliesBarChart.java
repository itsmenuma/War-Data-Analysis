package com.warManagementGUI.DataAnalysis;

import java.util.Map;

import com.warManagementGUI.util.DBUtil;

import javafx.stage.Stage;

public class SuppliesBarChart extends AbstractBarChart {

    public SuppliesBarChart(Map<String, Integer> data) {
        super(data, "Supplies Analysis");
    }

    public static Stage showSupplyStatusChart() {
        Map<String, Integer> statusCount = DBUtil.getStatusCount("supplies");
        return showBarChart("Supplies by Status", statusCount);
    }

    public static Stage showSupplyTypeChart() {
        Map<String, Integer> typeCount = DBUtil.getGroupCount("supplies", "type");
        return showBarChart("Supplies by Type", typeCount);
    }
}
