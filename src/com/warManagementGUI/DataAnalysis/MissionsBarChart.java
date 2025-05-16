package com.warManagementGUI.DataAnalysis;

import java.util.Map;

import com.warManagementGUI.util.DBUtil;

public class MissionsBarChart extends AbstractBarChart {
    private static final long serialVersionUID = 1L;

    public MissionsBarChart(Map<String, Integer> data) {
        super(data, "Missions Analysis");
    }
    
    public static void showMissionStatusChart() {
        Map<String, Integer> statusCount = DBUtil.getStatusCount("missions");
        showBarChart("Missions by Status", statusCount);
    }
    
    public static void showMissionTypeChart() {
        Map<String, Integer> typeCount = DBUtil.getGroupCount("missions", "mission_type");
        showBarChart("Missions by Type", typeCount);
    }
}
