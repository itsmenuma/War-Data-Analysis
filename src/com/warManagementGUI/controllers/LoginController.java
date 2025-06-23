package com.warManagementGUI.controllers;

import com.warManagementGUI.services.AuthenticationService;
import com.warManagementGUI.util.ThemeManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the login interface
 */
public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Button themeToggleButton;
    private final AuthenticationService authService = AuthenticationService.getInstance();
    private final ThemeManager themeManager = ThemeManager.getInstance();    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Clear any existing error messages
        if (errorLabel != null) {
            errorLabel.setVisible(false);
        }

        // Set focus to username field
        if (usernameField != null) {
            usernameField.requestFocus();
        }

        // Add enter key handlers
        if (usernameField != null) {
            usernameField.setOnAction(e -> handleLogin());
        }
        if (passwordField != null) {
            passwordField.setOnAction(e -> handleLogin());
        }
        // Initialize theme toggle button
        updateThemeButtonText();

        // Apply theme to the scene when the component is ready
        if (loginButton != null) {
            loginButton.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    applyCurrentTheme(newScene);
                }
            });
        }
    }
    
    private void applyCurrentTheme(Scene scene) {
        // Clear existing stylesheets first
        scene.getStylesheets().clear();
        
        // Apply the appropriate theme
        themeManager.applyThemeToScene(scene);
        
        // Force a layout update to ensure all styles are applied
        scene.getRoot().applyCss();
        scene.getRoot().autosize();
    }    @FXML
    private void toggleTheme() {
        themeManager.toggleTheme();
        updateThemeButtonText();
        // Reapply theme to current scene
        Scene scene = loginButton.getScene();
        if (scene != null) {
            applyCurrentTheme(scene);
        }
    }

    private void updateThemeButtonText() {
        if (themeToggleButton != null) {
            themeToggleButton.setText(themeManager.isDarkMode() ? "☀️ Light" : "🌙 Dark");
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // Clear previous error messages
        hideError();

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }

        // Attempt login
        if (authService.login(username, password)) {
            try {
                // Load main dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/warManagementGUI/fxml/Dashboard.fxml"));
                Parent root = loader.load();                Scene scene = new Scene(root, 1200, 800);

                // Apply current theme to the new scene
                applyCurrentTheme(scene);

                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("War Data Analysis System - " + authService.getCurrentUser().getRole().getDisplayName());
                stage.setMaximized(true);

            } catch (IOException e) {
                showError("Failed to load dashboard: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showError("Invalid username or password");
            // Clear password field for security
            passwordField.clear();
        }
    }

    @FXML
    private void handleForgotPassword() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Password Recovery");
        alert.setHeaderText("Password Recovery");
        alert.setContentText("Please contact your system administrator to reset your password.");
        alert.showAndWait();
    }

    @FXML
    private void showDemoCredentials() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Demo Credentials");
        alert.setHeaderText("Available Demo Users");
        alert.setContentText(
                "Administrator:\n" +
                        "  Username: admin\n" +
                        "  Password: admin123\n\n" +
                        "Analyst:\n" +
                        "  Username: analyst\n" +
                        "  Password: analyst123\n\n" +
                        "Viewer:\n" +
                        "  Username: viewer\n" +
                        "  Password: viewer123");
        alert.showAndWait();
    }

    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        }
    }

    private void hideError() {
        if (errorLabel != null) {
            errorLabel.setVisible(false);
        }
    }
}
