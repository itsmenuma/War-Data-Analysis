package com.warManagementGUI.DataAnalysis;

import java.util.Map;

import javax.swing.JFrame;

import com.warManagementGUI.util.DBUtil;

public class MissionsBarChart extends AbstractBarChart {
    private static final long serialVersionUID = 1L;

    public MissionsBarChart(Map<String, Integer> data) {
        super(data, "Missions Analysis");
    }
    
    public static JFrame showMissionStatusChart() {
        Map<String, Integer> statusCount = DBUtil.getStatusCount("missions");
        return showBarChart("Missions by Status", statusCount);
    }
    
    public static JFrame showMissionTypeChart() {
        Map<String, Integer> typeCount = DBUtil.getGroupCount("missions", "mission_type");
        return showBarChart("Missions by Type", typeCount);
    }
}
