package test.java;

import com.warManagementGUI.DataAnalysis.PersonnelBarChart;
import com.warManagementGUI.util.DBUtil;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Simple test to verify Analytics charts work correctly
 */
public class AnalyticsTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Test Personnel Analytics
        System.out.println("Testing Personnel Analytics Charts...");

        try {
            // Test if database connection works
            boolean dbConnected = DBUtil.getConnection() != null;
            System.out.println("Database connection: " + (dbConnected ? "SUCCESS" : "FAILED"));

            // Test chart creation
            PersonnelBarChart.showPersonnelStatusChart();
            System.out.println("Personnel Status Chart: SUCCESS");

            PersonnelBarChart.showPersonnelPostChart();
            System.out.println("Personnel Post Chart: SUCCESS");

            System.out.println("Analytics Test Completed Successfully!");

        } catch (Exception e) {
            System.err.println("Analytics Test Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
