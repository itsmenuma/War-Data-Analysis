package com.warManagementGUI.util;

import java.util.prefs.Preferences;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * ThemeManager handles light and dark mode themes for the War Data Analysis
 * System
 */
public class ThemeManager {
    private static ThemeManager instance;
    private boolean isDarkMode = false;
    private final Preferences prefs;

    // Dark theme colors
    private static final Color DARK_BG_COLOR = Color.rgb(33, 37, 41);
    private static final Color DARK_TEXT_COLOR = Color.rgb(248, 249, 250);
    private static final Color DARK_CARD_BG = Color.rgb(52, 58, 64);
    private static final Color DARK_BUTTON_BG = Color.rgb(0, 123, 255);

    // Light theme colors
    private static final Color LIGHT_BG_COLOR = Color.rgb(0, 64, 64);
    private static final Color LIGHT_TEXT_COLOR = Color.WHITE;
    private static final Color LIGHT_CARD_BG = Color.WHITE;
    private static final Color LIGHT_BUTTON_BG = Color.BLACK;

    private ThemeManager() {
        prefs = Preferences.userNodeForPackage(ThemeManager.class);
        isDarkMode = prefs.getBoolean("darkMode", false);
    }

    public static ThemeManager getInstance() {
        synchronized (ThemeManager.class) {
            if (instance == null) {
                instance = new ThemeManager();
            }
        }
        return instance;
    }

    public boolean isDarkMode() {
        return isDarkMode;
    }

    public void toggleTheme() {
        isDarkMode = !isDarkMode;
        prefs.putBoolean("darkMode", isDarkMode);
    }

    public void setDarkMode(boolean darkMode) {
        this.isDarkMode = darkMode;
        prefs.putBoolean("darkMode", darkMode);
    }

    public Color getBackgroundColor() {
        return isDarkMode ? DARK_BG_COLOR : LIGHT_BG_COLOR;
    }

    public Color getTextColor() {
        return isDarkMode ? DARK_TEXT_COLOR : LIGHT_TEXT_COLOR;
    }

    public Color getCardBackgroundColor() {
        return isDarkMode ? DARK_CARD_BG : LIGHT_CARD_BG;
    }

    public Color getButtonBackgroundColor() {
        return isDarkMode ? DARK_BUTTON_BG : LIGHT_BUTTON_BG;
    }

    // Apply theme to legacy Pane-based UI
    public void applyThemeToPane(Pane pane) {
        Color bgColor = getBackgroundColor();
        String bgColorString = String.format("rgb(%d,%d,%d)",
                (int) (bgColor.getRed() * 255),
                (int) (bgColor.getGreen() * 255),
                (int) (bgColor.getBlue() * 255));
        pane.setStyle("-fx-background-color: " + bgColorString + ";");
    }    public void applyThemeToScene(Scene scene) {
        scene.getStylesheets().clear();
        
        // Set the scene's background fill based on the theme
        if (isDarkMode) {
            scene.setFill(DARK_BG_COLOR);
            var darkThemeUrl = getClass().getResource("/com/warManagementGUI/css/dark-theme.css");
            if (darkThemeUrl != null) {
                scene.getStylesheets().add(darkThemeUrl.toExternalForm());
            }
        } else {
            scene.setFill(LIGHT_BG_COLOR);
            var lightThemeUrl = getClass().getResource("/com/warManagementGUI/css/application.css");
            if (lightThemeUrl != null) {
                scene.getStylesheets().add(lightThemeUrl.toExternalForm());
            }
        }
        
        // Force a style refresh
        scene.getRoot().applyCss();
    }

    // Get RGB string for CSS
    public String getRgbString(Color color) {
        return String.format("rgb(%d,%d,%d)",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
