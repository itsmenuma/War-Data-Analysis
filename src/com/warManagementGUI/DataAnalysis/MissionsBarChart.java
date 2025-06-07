package com.warManagementGUI.DataAnalysis;

import java.util.Map;

import com.warManagementGUI.util.DBUtil;

import javafx.stage.Stage;

public class MissionsBarChart extends AbstractBarChart {

    public MissionsBarChart(Map<String, Integer> data) {
        super(data, "Missions Analysis");
    }

    public static Stage showMissionStatusChart() {
        Map<String, Integer> statusCount = DBUtil.getStatusCount("missions");
        return showBarChart("Missions by Status", statusCount);
    }

    /* Depreacted method as Missions have no column such as type */
    // public static Stage showMissionTypeChart() {
    // Map<String, Integer> typeCount = DBUtil.getGroupCount("missions",
    // "mission_type");
    // return showBarChart("Missions by Type", typeCount);
    // }
}
