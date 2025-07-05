package com.warManagementGUI.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import com.warManagementGUI.services.AuthenticationService;
import com.warManagementGUI.util.ThemeManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    private final ThemeManager themeManager = ThemeManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (errorLabel != null) {
            errorLabel.setVisible(false);
        }

        if (usernameField != null) {
            usernameField.requestFocus();
        }

        if (usernameField != null) {
            usernameField.setOnAction(e -> handleLogin());
        }
        if (passwordField != null) {
            passwordField.setOnAction(e -> handleLogin());
        }

        updateThemeButtonText();

        if (loginButton != null) {
            loginButton.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    applyCurrentTheme(newScene);
                }
            });
        }
    }

    private void applyCurrentTheme(Scene scene) {

        scene.getStylesheets().clear();

        themeManager.applyThemeToScene(scene);

        scene.getRoot().applyCss();
        scene.getRoot().autosize();
    }

    @FXML
    private void toggleTheme() {
        themeManager.toggleTheme();
        updateThemeButtonText();

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

        hideError();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }

        if (authService.login(username, password)) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/warManagementGUI/fxml/Dashboard.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 1200, 800);

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
        String credentialsText = loadDemoCredentialsFromProperties();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Demo Credentials");
        alert.setHeaderText("Available Demo Users");
        alert.setContentText(credentialsText);
        alert.showAndWait();
    }

    /**
     * Load demo credentials from the same properties file used by
     * AuthenticationService
     */
    private String loadDemoCredentialsFromProperties() {
        StringBuilder credentials = new StringBuilder();
        Properties props = new Properties();

        try (InputStream input = getClass().getResourceAsStream("/com/warManagementGUI/config/demo-users.properties")) {

            props.load(input);

            // Group users by role for better display
            StringBuilder adminUsers = new StringBuilder();
            StringBuilder analystUsers = new StringBuilder();
            StringBuilder viewerUsers = new StringBuilder();

            for (String username : props.stringPropertyNames()) {
                String userInfo = props.getProperty(username);
                String[] parts = userInfo.split(",");

                if (parts.length >= 6) {
                    String password = parts[0].trim();
                    String role = parts[1].trim().toUpperCase();
                    String firstName = parts[2].trim();
                    String lastName = parts[3].trim();
                    boolean active = Boolean.parseBoolean(parts[5].trim());

                    if (active) { // Only show active users
                        String userLine = String.format("  Username: %s  |  Password: %s  |  Name: %s %s\n",
                                username, password, firstName, lastName);

                        switch (role) {
                            case "ADMIN" -> adminUsers.append(userLine);
                            case "ANALYST" -> analystUsers.append(userLine);
                            case "VIEWER" -> viewerUsers.append(userLine);
                        }
                    }
                }
            }

            // Build the final display text
            if (adminUsers.length() > 0) {
                credentials.append("ADMINISTRATORS:\n").append(adminUsers).append("\n");
            }
            if (analystUsers.length() > 0) {
                credentials.append("ANALYSTS:\n").append(analystUsers).append("\n");
            }
            if (viewerUsers.length() > 0) {
                credentials.append("VIEWERS:\n").append(viewerUsers);
            }

            return credentials.toString();
        } catch (IOException e) {
            System.err.println("Error loading demo credentials from properties: " + e.getMessage());
            return "Error loading demo credentials. Please check the properties file.";
        }
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
