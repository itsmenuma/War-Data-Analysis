package com.warManagementGUI.DataAnalysis;

import java.util.Map;

import com.warManagementGUI.util.DBUtil;

import javafx.stage.Stage;

public class PersonnelBarChart extends AbstractBarChart {

    public static Stage showPersonnelStatusChart() {
        Map<String, Integer> statusCount = DBUtil.getStatusCount("personnel");
        return showBarChart("Personnel by Status", statusCount);
    }
    
    public static Stage showPersonnelPostChart() {
        Map<String, Integer> postCount = DBUtil.getGroupCount("personnel", "post");
        return showBarChart("Personnel by Post", postCount);
    }

    public PersonnelBarChart(Map<String, Integer> data, String title) {
        super(data, title);
    }
}
