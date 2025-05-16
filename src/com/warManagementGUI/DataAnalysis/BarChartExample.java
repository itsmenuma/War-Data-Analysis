package com.warManagementGUI.DataAnalysis;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

public class BarChartExample extends AbstractBarChart {
    public static void main(String[] args) {
        // Sample data for demonstration
        Map<String, Integer> data = new HashMap<>();
        data.put("Active", 45);
        data.put("Standby", 23);
        data.put("Maintenance", 12);
        data.put("Deployed", 35);
        
        // Display chart in a frame
        JFrame frame = new JFrame("Bar Chart Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        
        // Create the chart with sample data
        AbstractBarChart chart = new AbstractBarChart(data, "Sample Data Analysis");
        frame.getContentPane().add(chart);
        
        frame.setVisible(true);
    }

    public BarChartExample(Map<String, Integer> data, String title) {
        super(data, title);
    }
}
