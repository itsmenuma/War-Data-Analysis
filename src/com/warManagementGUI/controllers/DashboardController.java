package com.warManagementGUI.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Dashboard Controller for the War Data Analysis System
 * Manages the main navigation and dashboard functionality
 */
public class DashboardController implements Initializable {

    @FXML
    private Label titleLabel;
    @FXML
    private Button personnelBtn;
    @FXML
    private Button unitsBtn;
    @FXML
    private Button missionsBtn;
    @FXML
    private Button equipmentBtn;
    @FXML
    private Button suppliesBtn;
    @FXML
    private Button analyticsBtn;

    @FXML
    private ImageView personnelIcon;
    @FXML
    private ImageView unitsIcon;
    @FXML
    private ImageView missionsIcon;
    @FXML
    private ImageView equipmentIcon;
    @FXML
    private ImageView suppliesIcon;
    @FXML
    private ImageView analyticsIcon;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadIcons();
        setupAnimations();
    }

    /**
     * Load icons for all modules
     */
    private void loadIcons() {
        try {
            personnelIcon.setImage(new Image(getClass().getResourceAsStream("/pics/pesonnel_icon.jpg")));
            unitsIcon.setImage(new Image(getClass().getResourceAsStream("/pics/units_icon.jpg")));
            missionsIcon.setImage(new Image(getClass().getResourceAsStream("/pics/missions_icon.jpg")));
            equipmentIcon.setImage(new Image(getClass().getResourceAsStream("/pics/equipment-icon.jpg")));
            suppliesIcon.setImage(new Image(getClass().getResourceAsStream("/pics/supplies_icon.png")));

            analyticsIcon.setImage(new Image(getClass().getResourceAsStream("/pics/MI.png")));
        } catch (Exception e) {
            System.err.println("Error loading icons: " + e.getMessage());
        }
    }

    /**
     * Setup hover animations and effects
     */
    private void setupAnimations() {

        setupButtonHover(personnelBtn);
        setupButtonHover(unitsBtn);
        setupButtonHover(missionsBtn);
        setupButtonHover(equipmentBtn);
        setupButtonHover(suppliesBtn);
        setupButtonHover(analyticsBtn);
    }

    private void setupButtonHover(Button button) {
        button.setOnMouseEntered(e -> {
            button.setStyle(button.getStyle() + "; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
        });

        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace("; -fx-scale-x: 1.05; -fx-scale-y: 1.05;", ""));
        });
    }

    @FXML
    private void openPersonnel() {
        System.out.println("DEBUG: openPersonnel() method called");
        try {
            openWindow("/com/warManagementGUI/fxml/Personnel.fxml", "Personnel Management");
        } catch (IOException e) {
            System.err.println("ERROR: Failed to open Personnel module: " + e.getMessage());
            e.printStackTrace();
            showError("Error opening Personnel module: " + e.getMessage());
        }
    }

    @FXML
    private void openUnits() {
        try {
            openWindow("/com/warManagementGUI/fxml/Units.fxml", "Units Management");
        } catch (IOException e) {
            showError("Error opening Units module: " + e.getMessage());
        }
    }

    @FXML
    private void openMissions() {
        try {
            openWindow("/com/warManagementGUI/fxml/Missions.fxml", "Missions Management");
        } catch (IOException e) {
            showError("Error opening Missions module: " + e.getMessage());
        }
    }

    @FXML
    private void openEquipment() {
        try {
            openWindow("/com/warManagementGUI/fxml/Equipment.fxml", "Equipment Management");
        } catch (IOException e) {
            showError("Error opening Equipment module: " + e.getMessage());

        }
    }

    @FXML
    private void openSupplies() {
        try {
            openWindow("/com/warManagementGUI/fxml/Supplies.fxml", "Supplies Management");
        } catch (IOException e) {
            showError("Error opening Supplies module: " + e.getMessage());
        }
    }

    @FXML
    private void openAnalytics() {
        try {
            openWindow("/com/warManagementGUI/fxml/Analytics.fxml", "Analytics Dashboard");
        } catch (Exception e) {
            showError("Error opening Analytics module: " + e.getMessage());
        }
    }

    /**
     * Helper method to open modules in the same window
     */
    private void openWindow(String fxmlPath, String title) throws IOException {
        System.out.println("DEBUG: Opening window with FXML path: " + fxmlPath);
        System.out.println("DEBUG: Setting title to: " + title);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        System.out.println("DEBUG: FXMLLoader created, loading FXML...");

        Parent root = loader.load();
        System.out.println("DEBUG: FXML loaded successfully");

        Stage currentStage = (Stage) titleLabel.getScene().getWindow();

        boolean wasMaximized = currentStage.isMaximized();

        if (wasMaximized) {
            currentStage.setMaximized(false);
        }

        currentStage.setTitle(title);

        currentStage.setMinWidth(1200);
        currentStage.setMinHeight(800);

        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets()
                .add(getClass().getResource("/com/warManagementGUI/css/application.css").toExternalForm());

        currentStage.setScene(scene);

        currentStage.show();

        javafx.application.Platform.runLater(() -> {
            try {

                root.applyCss();
                root.layout();

                javafx.geometry.Bounds contentBounds = root.getLayoutBounds();

                double requiredWidth = Math.max(1200, contentBounds.getWidth() + 100);
                double requiredHeight = Math.max(800, contentBounds.getHeight() + 150);

                System.out.println("DEBUG: Content bounds: " + contentBounds);
                System.out.println("DEBUG: Required size: " + requiredWidth + "x" + requiredHeight);

                currentStage.setWidth(requiredWidth);
                currentStage.setHeight(requiredHeight);

                if (currentStage.getWidth() < 1200) {
                    currentStage.setWidth(1200);
                }
                if (currentStage.getHeight() < 800) {
                    currentStage.setHeight(800);
                }

                if (wasMaximized) {

                    Timeline timeline = new Timeline(
                            new KeyFrame(Duration.millis(100), e -> {
                                currentStage.setMaximized(true);
                            }));
                    timeline.play();
                } else {
                    currentStage.centerOnScreen();
                }

                System.out.println(
                        "DEBUG: Final stage size: " + currentStage.getWidth() + "x" + currentStage.getHeight());

            } catch (Exception e) {
                System.err.println("Error in sizing calculation: " + e.getMessage());
                e.printStackTrace();
            }
        });

        System.out.println(
                "DEBUG: Scene set successfully with initial size: " + scene.getWidth() + "x" + scene.getHeight());
    }

    /**
     * Show error dialog
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
