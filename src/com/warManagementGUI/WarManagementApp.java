package com.warManagementGUI;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class WarManagementApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Create and display the main WarManagement window
            WarManagement warManagement = new WarManagement();
            warManagement.display();
} catch (Exception e) {
        System.err.println("Application startup error: " + e.getMessage());
         Alert alert = new Alert(Alert.AlertType.ERROR);
         alert.setTitle("Error");
        alert.setHeaderText("Application Startup Error");
        alert.setContentText("Failed to start application: " + e.getMessage());
         alert.showAndWait();
     }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
