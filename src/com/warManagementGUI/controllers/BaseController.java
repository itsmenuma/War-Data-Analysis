package com.warManagementGUI.controllers;

import java.io.IOException;

import com.warManagementGUI.models.Permission;
import com.warManagementGUI.services.AuthenticationService;
import com.warManagementGUI.util.ThemeManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * Base controller class that provides theme functionality and RBAC for all
 * controllers
 */
public abstract class BaseController {

    @FXML
    protected Button themeToggleBtn;

    protected ThemeManager themeManager = ThemeManager.getInstance();
    protected AuthenticationService authService = AuthenticationService.getInstance();

    /**
     * Initialize theme functionality - call this from the initialize method of
     * subclasses
     */
    protected void initializeTheme() {
        updateThemeToggleButton();

        applyTheme();

        configureUIBasedOnPermissions();
    }

    /**
     * Configure UI elements based on current user permissions
     * Override in subclasses to implement role-specific UI configurations
     */
    protected void configureUIBasedOnPermissions() {

    }

    /**
     * Set visibility and managed state of a node based on permission
     */
    protected void setNodeVisibility(Node node, Permission requiredPermission) {
        if (node != null) {
            boolean hasPermission = authService.hasPermission(requiredPermission);
            node.setVisible(hasPermission);
            node.setManaged(hasPermission);
        }
    }

    /**
     * Set button state (visibility and disable) based on permission
     */
    protected void setButtonState(Button button, Permission requiredPermission) {
        if (button != null) {
            boolean hasPermission = authService.hasPermission(requiredPermission);
            button.setVisible(hasPermission);
            button.setDisable(!hasPermission);
        }
    }

    /**
     * Set menu item state based on permission
     */
    protected void setMenuItemState(MenuItem menuItem, Permission requiredPermission) {
        if (menuItem != null) {
            boolean hasPermission = authService.hasPermission(requiredPermission);
            menuItem.setVisible(hasPermission);
            menuItem.setDisable(!hasPermission);
        }
    }

    /**
     * Check if current user has permission and show error if not
     */
    protected boolean checkPermission(Permission permission, String errorMessage) {
        if (!authService.hasPermission(permission)) {
            showPermissionError(errorMessage);
            return false;
        }
        return true;
    }

    /**
     * Show permission denied error dialog
     */
    private void showPermissionError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Access Denied");
        alert.setHeaderText("Insufficient Permissions");
        alert.setContentText(message != null ? message : "You don't have permission to perform this action.");
        alert.showAndWait();
    }

    /**
     * Toggle between light and dark theme
     */
    @FXML
    protected void toggleTheme() {
        themeManager.toggleTheme();
        updateThemeToggleButton();
        applyTheme();
    }

    /**
     * Update the theme toggle button text based on current theme
     */
    protected void updateThemeToggleButton() {
        if (themeToggleBtn != null) {
            if (themeManager.isDarkMode()) {
                themeToggleBtn.setText("☀️ Light Mode");
            } else {
                themeToggleBtn.setText("🌙 Dark Mode");
            }
        }
    }

    /**
     * Apply the current theme to the scene
     */
    protected void applyTheme() {
        if (themeToggleBtn != null) {
            Scene scene = themeToggleBtn.getScene();
            if (scene != null) {
                themeManager.applyThemeToScene(scene);

                javafx.application.Platform.runLater(() -> {
                    scene.getRoot().applyCss();
                    scene.getRoot().autosize();
                });
            }
        }
    }

    /**
     * Check if current user has the specified permission
     */
    protected boolean hasPermission(Permission permission) {
        return authService.hasPermission(permission);
    }

    /**
     * Require permission and show error if not available
     * 
     * @param permission Required permission
     * @return true if user has permission, false otherwise
     */
    protected boolean requirePermission(Permission permission) {
        return requirePermission(permission, null);
    }

    /**
     * Require permission and show custom error if not available
     * 
     * @param permission    Required permission
     * @param customMessage Custom error message
     * @return true if user has permission, false otherwise
     */
    protected boolean requirePermission(Permission permission, String customMessage) {
        if (!authService.hasPermission(permission)) {
            String message = customMessage != null ? customMessage
                    : "You don't have permission to " + permission.getDescription().toLowerCase() + ".";
            showPermissionError(message);
            return false;
        }
        return true;
    }

    /**
     * Configure button visibility based on permissions
     */
    protected void configureButtonVisibility(Button button, Permission requiredPermission) {
        if (button != null) {
            boolean hasPermission = authService.hasPermission(requiredPermission);
            button.setVisible(hasPermission);
            button.setManaged(hasPermission);
        }
    }

    /**
     * Show a generic alert dialog
     */
    protected void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Navigate to a new FXML scene while maintaining authentication state
     * 
     * @param fxmlPath    Path to the FXML file
     * @param title       Window title
     * @param currentNode Any node from the current scene to get the stage
     * @throws IOException If FXML loading fails
     */
    protected void navigateToScene(String fxmlPath, String title, Node currentNode) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        javafx.scene.Parent root = loader.load();

        BaseController newController = loader.getController();
        if (newController != null) {

            try {
                java.lang.reflect.Method updateUserInfo = newController.getClass().getDeclaredMethod("updateUserInfo");
                updateUserInfo.setAccessible(true);
                updateUserInfo.invoke(newController);
            } catch (Exception e) {

                System.out.println("DEBUG: updateUserInfo method not found or failed to invoke: " + e.getMessage());
            }
        }

        Stage stage = (Stage) currentNode.getScene().getWindow();
        Scene scene = new Scene(root);

        themeManager.applyThemeToScene(scene);

        stage.setScene(scene);
        stage.setTitle(title);

        if (!stage.isMaximized()) {
            stage.centerOnScreen();
        }
    }
}
