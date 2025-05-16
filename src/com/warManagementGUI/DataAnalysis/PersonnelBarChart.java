package com.warManagementGUI.DataAnalysis;

import java.util.Map;

import javax.swing.JFrame;

import com.warManagementGUI.util.DBUtil;

public class PersonnelBarChart extends AbstractBarChart {

    public static JFrame showPersonnelStatusChart() {
        Map<String, Integer> statusCount = DBUtil.getStatusCount("personnel");
        return showBarChart("Personnel by Status", statusCount);
    }
    
    public static JFrame showPersonnelPostChart() {
        Map<String, Integer> postCount = DBUtil.getGroupCount("personnel", "post");
        return showBarChart("Personnel by Post", postCount);
    }

    public PersonnelBarChart(Map<String, Integer> data, String title) {
        super(data, title);
    }
}
