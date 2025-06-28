package com.warManagementGUI.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * Reusable filter controls component for all tabular screens
 * Provides search bar, category filters, date filters, and clear button
 */
public class FilterControls extends VBox {

    private TextField searchField;
    private ComboBox<String> primaryFilter;
    private ComboBox<String> statusFilter;
    private ComboBox<String> locationFilter;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private Button clearFiltersButton;
    private Label resultsLabel;

    // Labels for customization
    private String primaryFilterLabel = "Category";
    private String searchPrompt = "Search keywords...";

    public FilterControls() {
        initializeComponents();
        layoutComponents();
        styleComponents();
    }

    public FilterControls(String primaryFilterLabel, String searchPrompt) {
        this.primaryFilterLabel = primaryFilterLabel;
        this.searchPrompt = searchPrompt;
        initializeComponents();
        layoutComponents();
        styleComponents();
    }

    private void initializeComponents() {
        searchField = new TextField();
        searchField.setPromptText(searchPrompt);

        primaryFilter = new ComboBox<>();
        primaryFilter.setPromptText("Filter by " + primaryFilterLabel);

        statusFilter = new ComboBox<>();
        statusFilter.setPromptText("Filter by Status");

        locationFilter = new ComboBox<>();
        locationFilter.setPromptText("Filter by Location");

        startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start Date");

        endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End Date");

        clearFiltersButton = new Button("Clear All Filters");

        resultsLabel = new Label("Showing all results");
    }

    private void layoutComponents() {
        // Search bar row
        HBox searchRow = new HBox(10);
        searchRow.setAlignment(Pos.CENTER_LEFT);

        Label searchLabel = new Label("Search:");
        searchLabel.setMinWidth(60);
        searchLabel.getStyleClass().add("filter-label");

        searchField.setPrefWidth(300);
        searchField.setMaxWidth(400);

        // Add spacer
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        searchRow.getChildren().addAll(searchLabel, searchField, spacer1, resultsLabel);

        // Filter controls row
        HBox filterRow = new HBox(15);
        filterRow.setAlignment(Pos.CENTER_LEFT);

        // Primary filter (customizable)
        Label primaryLabel = new Label(primaryFilterLabel + ":");
        primaryLabel.setMinWidth(60);
        primaryLabel.getStyleClass().add("filter-label");
        primaryFilter.setPrefWidth(150);

        // Status filter
        Label statusLabel = new Label("Status:");
        statusLabel.setMinWidth(50);
        statusLabel.getStyleClass().add("filter-label");
        statusFilter.setPrefWidth(120);

        // Location filter
        Label locationLabel = new Label("Location:");
        locationLabel.setMinWidth(60);
        locationLabel.getStyleClass().add("filter-label");
        locationFilter.setPrefWidth(140);

        // Date range
        Label dateLabel = new Label("Date Range:");
        dateLabel.setMinWidth(80);
        dateLabel.getStyleClass().add("filter-label");
        startDatePicker.setPrefWidth(130);
        endDatePicker.setPrefWidth(130);

        // Add spacer
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        filterRow.getChildren().addAll(
                primaryLabel, primaryFilter,
                statusLabel, statusFilter,
                locationLabel, locationFilter,
                dateLabel, startDatePicker, new Label(" to "), endDatePicker,
                spacer2, clearFiltersButton);

        this.getChildren().addAll(searchRow, filterRow);
        this.setSpacing(10);
        this.setPadding(new Insets(15));
    }

    private void styleComponents() {
        this.getStyleClass().add("filter-controls");

        searchField.getStyleClass().add("search-field");
        clearFiltersButton.getStyleClass().add("clear-button");
        resultsLabel.getStyleClass().add("results-label");

        // Apply inline styles that work for both light and dark modes
        // These will be overridden by CSS if present
        this.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 0 0 1 0;");
        clearFiltersButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-weight: bold;");
        resultsLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #6c757d;");
    }

    public void updateResultsLabel(int filtered, int total) {
        if (filtered == total) {
            resultsLabel.setText("Showing all " + total + " results");
        } else {
            resultsLabel.setText("Showing " + filtered + " of " + total + " results");
        }
    }

    public void hideLocationFilter() {
        // Find and hide location filter components
        HBox filterRow = (HBox) this.getChildren().get(1);
        filterRow.getChildren()
                .removeIf(node -> (node instanceof Label && ((Label) node).getText().equals("Location:")) ||
                        node == locationFilter);
    }

    public void hideDateFilter() {
        // Find and hide date filter components
        HBox filterRow = (HBox) this.getChildren().get(1);
        filterRow.getChildren()
                .removeIf(node -> (node instanceof Label && ((Label) node).getText().equals("Date Range:")) ||
                        node == startDatePicker || node == endDatePicker ||
                        (node instanceof Label && ((Label) node).getText().equals(" to ")));
    }

    // Getters for all components
    public TextField getSearchField() {
        return searchField;
    }

    public ComboBox<String> getPrimaryFilter() {
        return primaryFilter;
    }

    public ComboBox<String> getStatusFilter() {
        return statusFilter;
    }

    public ComboBox<String> getLocationFilter() {
        return locationFilter;
    }

    public DatePicker getStartDatePicker() {
        return startDatePicker;
    }

    public DatePicker getEndDatePicker() {
        return endDatePicker;
    }

    public Button getClearFiltersButton() {
        return clearFiltersButton;
    }

    public Label getResultsLabel() {
        return resultsLabel;
    }
}
