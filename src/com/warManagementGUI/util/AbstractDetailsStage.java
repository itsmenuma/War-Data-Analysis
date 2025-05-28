package com.warManagementGUI.util;

import java.lang.reflect.InvocationTargetException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public abstract class AbstractDetailsStage {
    protected static final Font LABEL_FONT = Font.font("Times New Roman", FontWeight.BOLD, 20);
    protected static final Font FIELD_FONT = Font.font("Times New Roman", FontWeight.BOLD, 20);
    protected static final Font BUTTON_FONT = Font.font("Times New Roman", FontWeight.BOLD, 15);
    protected static final Font TITLE_FONT = Font.font("Times New Roman", FontWeight.BOLD, 50);
    protected static final Color TEXT_COLOR = Color.WHITE;
    protected static final Color BG_COLOR = Color.rgb(0, 64, 64);
    protected static final Color BUTTON_COLOR = Color.BLACK;
    
    protected Stage stage;
    protected Pane rootPane;
    protected Scene scene;
    
    public AbstractDetailsStage(String title, int width, int height) {
        stage = new Stage();
        stage.setTitle(title);
        stage.setWidth(width);
        stage.setHeight(height);
        
        // Create root pane with absolute positioning
        rootPane = new Pane();
        rootPane.setStyle("-fx-background-color: rgb(0, 64, 64);");
        
        scene = new Scene(rootPane, width, height);
        stage.setScene(scene);
    }

    protected Label createLabel(String text, double x, double y, double w, double h) {
        Label label = new Label(text);
        label.setFont(LABEL_FONT);
        label.setTextFill(TEXT_COLOR);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setPrefWidth(w);
        label.setPrefHeight(h);
        rootPane.getChildren().add(label);
        return label;
    }
    
    protected Label createLabel(String text, Font font, double x, double y, double w, double h) {
        Label label = createLabel(text, x, y, w, h);
        label.setFont(font);
        return label;
    }
    
    protected Label createLabel(String text, Font font, Color textColor, double x, double y, double w, double h) {
        Label label = createLabel(text, font, x, y, w, h);
        label.setTextFill(textColor);
        return label;
    }
    
    protected TextField createTextField(double x, double y, double w, double h) {
        TextField textField = new TextField();
        textField.setLayoutX(x);
        textField.setLayoutY(y);
        textField.setPrefWidth(w);
        textField.setPrefHeight(h);
        rootPane.getChildren().add(textField);
        return textField;
    }
    
    protected TextField createTextField(double x, double y, double w, double h, int columns) {
        TextField textField = createTextField(x, y, w, h);
        textField.setPrefColumnCount(columns);
        return textField;
    }
    
    protected Button createButton(String text, double x, double y, double w, double h, EventHandler<ActionEvent> handler) {
        Button button = new Button(text);
        button.setFont(BUTTON_FONT);
        button.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setPrefWidth(w);
        button.setPrefHeight(h);
        button.setOnAction(handler);
        rootPane.getChildren().add(button);
        return button;
    }
    
    protected Button createButton(String text, Font font, double x, double y, double w, double h, EventHandler<ActionEvent> handler) {
        Button button = createButton(text, x, y, w, h, handler);
        button.setFont(font);
        return button;
    }
    
    protected Button createButton(String text, Font font, Color textColor, Color bgColor, double x, double y, double w, double h, EventHandler<ActionEvent> handler) {
        Button button = createButton(text, font, x, y, w, h, handler);
        String bgColorString = String.format("-fx-background-color: rgb(%d,%d,%d);", 
            (int)(bgColor.getRed() * 255), 
            (int)(bgColor.getGreen() * 255), 
            (int)(bgColor.getBlue() * 255));
        String textColorString = String.format("-fx-text-fill: rgb(%d,%d,%d);", 
            (int)(textColor.getRed() * 255), 
            (int)(textColor.getGreen() * 255), 
            (int)(textColor.getBlue() * 255));
        button.setStyle(bgColorString + textColorString);
        return button;
    }

    protected <T extends AbstractDetailsStage> void createNavButton(String name, Class<T> cls, double x, double y, double w, double h) {
        createButton(name, BUTTON_FONT, TEXT_COLOR, BUTTON_COLOR, x, y, w, h, e -> {
            try {
                T detailsStage = cls.getDeclaredConstructor().newInstance();
                detailsStage.display();
                dispose();
            } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
                System.err.println("Navigation error: " + ex.getMessage());
                handleDatabaseError(ex, "Navigation error");
            }
        });
    }
    
    protected ComboBox<String> createComboBox(String[] items, double x, double y, double w, double h) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(items);
        comboBox.setLayoutX(x);
        comboBox.setLayoutY(y);
        comboBox.setPrefWidth(w);
        comboBox.setPrefHeight(h);
        rootPane.getChildren().add(comboBox);
        return comboBox;
    }
    
protected ImageView createIconLabel(String path, double x, double y, double w, double h) {
     try {
         Image image = new Image(getClass().getResourceAsStream(path));
         ImageView imageView = new ImageView(image);
         imageView.setLayoutX(x);
         imageView.setLayoutY(y);
         imageView.setFitWidth(w);
         imageView.setFitHeight(h);
         imageView.setPreserveRatio(false);
         rootPane.getChildren().add(imageView);
         return imageView;
     } catch (Exception e) {
         System.err.println("Could not load image: " + path);
        // Create a placeholder ImageView with a default image or transparent background
        ImageView placeholder = new ImageView();
        placeholder.setLayoutX(x);
        placeholder.setLayoutY(y);
        placeholder.setFitWidth(w);
        placeholder.setFitHeight(h);
        placeholder.setStyle("-fx-background-color: lightgray;");
        rootPane.getChildren().add(placeholder);
        return placeholder;
     }
 }
    
    protected Label createTextLabel(String text, Font font, Color textColor, double x, double y, double w, double h) {
        Label label = new Label(text);
        label.setFont(font);
        label.setTextFill(textColor);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setPrefWidth(w);
        label.setPrefHeight(h);
        label.setAlignment(Pos.CENTER);
        rootPane.getChildren().add(label);
        return label;
    }
    
    protected void handleDatabaseError(Exception e, String message) {
        System.err.println(message + ": " + e.getMessage());
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database Error");
        alert.setHeaderText("Database Error");
        alert.setContentText(message + ": " + e.getMessage());
        alert.showAndWait();
    }
    
    protected void showInformationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    protected void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    protected boolean showConfirmationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
    
    public void display() {
        stage.show();
    }
    
    public void dispose() {
        stage.close();
    }
}
