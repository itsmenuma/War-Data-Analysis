package com.warManagementGUI.DataAnalysis;

import java.util.Map;

import javax.swing.JFrame;

import com.warManagementGUI.util.DBUtil;

public class SuppliesBarChart extends AbstractBarChart {
    private static final long serialVersionUID = 1L;

    public SuppliesBarChart(Map<String, Integer> data) {
        super(data, "Supplies Analysis");
    }
    
    public static JFrame showSupplyStatusChart() {
        Map<String, Integer> statusCount = DBUtil.getStatusCount("supplies");
        return showBarChart("Supplies by Status", statusCount);
    }
    
    public static JFrame showSupplyTypeChart() {
        Map<String, Integer> typeCount = DBUtil.getGroupCount("supplies", "supply_type");
        return showBarChart("Supplies by Type", typeCount);
    }
}
