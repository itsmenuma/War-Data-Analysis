package com.warManagementGUI.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.util.function.Predicate;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Generic utility class for filtering table data across all screens
 * Provides keyword search, category filtering, and multi-criteria filtering
 */
public class TableFilterUtil<T> {

    private final ObservableList<T> originalData;
    private final FilteredList<T> filteredData;
    private Predicate<T> keywordFilter = item -> true;
    private Predicate<T> categoryFilter = item -> true;
    private Predicate<T> dateFilter = item -> true;
    private Predicate<T> statusFilter = item -> true;
    private Predicate<T> locationFilter = item -> true;

    public TableFilterUtil(ObservableList<T> data) {
        this.originalData = data;
        this.filteredData = new FilteredList<>(data);
    }

    public FilteredList<T> getFilteredData() {
        return filteredData;
    }

    public ObservableList<T> getOriginalData() {
        return originalData;
    }

    public void setKeywordFilter(Predicate<T> filter) {
        this.keywordFilter = filter;
        updateFilter();
    }

    public void setCategoryFilter(Predicate<T> filter) {
        this.categoryFilter = filter;
        updateFilter();
    }

    public void setDateFilter(Predicate<T> filter) {
        this.dateFilter = filter;
        updateFilter();
    }

    public void setStatusFilter(Predicate<T> filter) {
        this.statusFilter = filter;
        updateFilter();
    }

    public void setLocationFilter(Predicate<T> filter) {
        this.locationFilter = filter;
        updateFilter();
    }

    private void updateFilter() {
        filteredData.setPredicate(item -> keywordFilter.test(item) &&
                categoryFilter.test(item) &&
                dateFilter.test(item) &&
                statusFilter.test(item) &&
                locationFilter.test(item));
    }

    public void clearAllFilters() {
        keywordFilter = item -> true;
        categoryFilter = item -> true;
        dateFilter = item -> true;
        statusFilter = item -> true;
        locationFilter = item -> true;
        updateFilter();
    }

    public int getFilteredCount() {
        return filteredData.size();
    }

    public int getTotalCount() {
        return originalData.size();
    }

    /**
     * Extract unique values from a list using a value extractor function
     */
    public static <T> Set<String> extractUniqueValues(List<T> data, java.util.function.Function<T, String> extractor) {
        Set<String> values = new HashSet<>();
        for (T item : data) {
            String value = extractor.apply(item);
            if (value != null && !value.trim().isEmpty()) {
                values.add(value.trim());
            }
        }
        return values;
    }

    /**
     * Setup a ComboBox with filter values including "All" option
     */
    public static void setupComboBoxFilter(ComboBox<String> comboBox, Set<String> values) {
        ObservableList<String> items = FXCollections.observableArrayList();
        items.add("All");
        items.addAll(values.stream().sorted().toList());
        comboBox.setItems(items);
        comboBox.setValue("All");
    }

    /**
     * Check if a text field search matches any of the given text values
     */
    public static boolean matchesKeyword(String searchText, String... values) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return true;
        }

        String lowerSearch = searchText.toLowerCase();
        for (String value : values) {
            if (value != null && value.toLowerCase().contains(lowerSearch)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a value matches the selected filter (handles "All" case)
     */
    public static boolean matchesFilter(String filterValue, String actualValue) {
        return filterValue == null || "All".equals(filterValue) || filterValue.equals(actualValue);
    }
}
