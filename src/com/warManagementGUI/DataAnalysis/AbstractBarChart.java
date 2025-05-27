package com.warManagementGUI.DataAnalysis;

import java.util.Map;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public abstract class AbstractBarChart {
    
    protected BarChart<String, Number> chart;
    protected String title;
    
    public AbstractBarChart(Map<String, Integer> data, String title) {
        this.title = title;
        createChart(data);
    }
    
    private void createChart(Map<String, Integer> data) {
        // Create axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        
        // Create chart
        chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(title);
        chart.setLegendVisible(false);
        
        // Create data series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        
        chart.getData().add(series);
    }
    
    public BarChart<String, Number> getChart() {
        return chart;
    }
    
    public static Stage showBarChart(String title, Map<String, Integer> data) {
        Platform.runLater(() -> {
            SimpleBarChart chartInstance = new SimpleBarChart(data, title);
            
            Stage stage = new Stage();
            stage.setTitle(title);
            
            Scene scene = new Scene(chartInstance.getChart(), 600, 400);
            stage.setScene(scene);
            stage.show();
        });
        
        return null; // Note: In JavaFX we return null since the stage is shown asynchronously
    }
    
    // Concrete implementation for the static utility method
    private static class SimpleBarChart extends AbstractBarChart {
        public SimpleBarChart(Map<String, Integer> data, String title) {
            super(data, title);
        }
    }
}
