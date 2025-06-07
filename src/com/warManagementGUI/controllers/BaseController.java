package com.warManagementGUI.controllers;

import com.warManagementGUI.util.ThemeManager;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

/**
 * Base controller class that provides theme functionality for all controllers
 */
public abstract class BaseController {

    @FXML
    protected Button themeToggleBtn;

    protected ThemeManager themeManager = ThemeManager.getInstance();

    /**
     * Initialize theme functionality - call this from the initialize method of
     * subclasses
     */
    protected void initializeTheme() {
        updateThemeToggleButton();
        // Apply theme immediately when the controller is initialized
        applyTheme();
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
            }
        }
    }
}
