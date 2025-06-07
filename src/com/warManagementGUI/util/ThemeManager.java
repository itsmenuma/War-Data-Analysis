package com.warManagementGUI.util;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.prefs.Preferences;

/**
 * ThemeManager handles light and dark mode themes for the War Data Analysis
 * System
 */
public class ThemeManager {
    private static ThemeManager instance;
    private boolean isDarkMode = false;
    private Preferences prefs;

    // Dark theme colors
    private static final Color DARK_BG_COLOR = Color.rgb(33, 37, 41);
    private static final Color DARK_TEXT_COLOR = Color.rgb(248, 249, 250);
    private static final Color DARK_CARD_BG = Color.rgb(52, 58, 64);
    private static final Color DARK_BUTTON_BG = Color.rgb(0, 123, 255);

    // Light theme colors (original)
    private static final Color LIGHT_BG_COLOR = Color.rgb(0, 64, 64);
    private static final Color LIGHT_TEXT_COLOR = Color.WHITE;
    private static final Color LIGHT_CARD_BG = Color.WHITE;
    private static final Color LIGHT_BUTTON_BG = Color.BLACK;

    private ThemeManager() {
        prefs = Preferences.userNodeForPackage(ThemeManager.class);
        isDarkMode = prefs.getBoolean("darkMode", false);
    }

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
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

    // Colors for legacy JavaFX components
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
    }

    // Apply theme to modern FXML-based UI
    public void applyThemeToScene(Scene scene) {
        scene.getStylesheets().clear();
        if (isDarkMode) {
            scene.getStylesheets()
                    .add(getClass().getResource("/com/warManagementGUI/css/dark-theme.css").toExternalForm());
        } else {
            scene.getStylesheets()
                    .add(getClass().getResource("/com/warManagementGUI/css/application.css").toExternalForm());
        }
    }

    // Get RGB string for CSS
    public String getRgbString(Color color) {
        return String.format("rgb(%d,%d,%d)",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
