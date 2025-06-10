package com.warManagementGUI.controllers;

import com.warManagementGUI.util.ThemeManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Modern War Management Application with FXML and SceneBuilder support
 * This replaces the old WarManagementApp with a modern, maintainable approach
 */
public class ModernWarManagementApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {

            System.out.println("Looking for FXML at: /com/warManagementGUI/fxml/Dashboard.fxml");
            System.out.println("Looking for CSS at: /com/warManagementGUI/css/application.css");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/warManagementGUI/fxml/Dashboard.fxml"));

            if (loader.getLocation() == null) {
                System.err.println("Could not find Dashboard.fxml");
                throw new RuntimeException("Dashboard.fxml not found");
            }
            Parent root = loader.load();

            Scene scene = new Scene(root, 1200, 800);

            // Apply initial theme using ThemeManager
            ThemeManager themeManager = ThemeManager.getInstance();
            themeManager.applyThemeToScene(scene);

            primaryStage.setTitle("War Data Analysis System - Modern UI");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(700);
            primaryStage.setMaximized(true);

            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Application startup error: " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Application Error");
            alert.setHeaderText("Failed to start application");
            alert.setContentText("Error: " + e.getMessage() + "\n\nFalling back to legacy interface...");
            alert.showAndWait();

            try {
                com.warManagementGUI.WarManagement warManagement = new com.warManagementGUI.WarManagement();
                warManagement.display();
            } catch (Exception ex) {
                System.err.println("Legacy fallback also failed: " + ex.getMessage());
                System.exit(1);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
