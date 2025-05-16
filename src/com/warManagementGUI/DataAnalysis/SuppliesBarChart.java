package com.warManagementGUI.DataAnalysis;

import java.util.Map;

import com.warManagementGUI.util.DBUtil;

public class SuppliesBarChart extends AbstractBarChart {
    private static final long serialVersionUID = 1L;

    public SuppliesBarChart(Map<String, Integer> data) {
        super(data, "Supplies Analysis");
    }
    
    public static void showSupplyStatusChart() {
        Map<String, Integer> statusCount = DBUtil.getStatusCount("supplies");
        showBarChart("Supplies by Status", statusCount);
    }
    
    public static void showSupplyTypeChart() {
        Map<String, Integer> typeCount = DBUtil.getGroupCount("supplies", "supply_type");
        showBarChart("Supplies by Type", typeCount);
    }
}
