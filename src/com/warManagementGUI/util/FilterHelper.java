package com.warManagementGUI.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.function.Function;

import com.warManagementGUI.components.FilterControls;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

/**
 * Helper class to standardize filter implementation across all controllers
 */
public class FilterHelper {

    public static <T> void setupBasicFilters(
            FilterControls filterControls,
            TableFilterUtil<T> filterUtil,
            ObservableList<T> data,
            Function<T, String> primaryExtractor,
            Function<T, String> statusExtractor,
            String... searchFields) {

        // Setup primary filter
        Set<String> primaryValues = TableFilterUtil.extractUniqueValues(data, primaryExtractor);
        TableFilterUtil.setupComboBoxFilter(filterControls.getPrimaryFilter(), primaryValues);

        // Setup status filter
        Set<String> statusValues = TableFilterUtil.extractUniqueValues(data, statusExtractor);
        TableFilterUtil.setupComboBoxFilter(filterControls.getStatusFilter(), statusValues);
    }

    public static <T> void setupLocationFilter(
            FilterControls filterControls,
            ObservableList<T> data,
            Function<T, String> locationExtractor) {

        Set<String> locationValues = TableFilterUtil.extractUniqueValues(data, locationExtractor);
        TableFilterUtil.setupComboBoxFilter(filterControls.getLocationFilter(), locationValues);
    }

    public static <T> void setupKeywordFilter(
            FilterControls filterControls,
            TableFilterUtil<T> filterUtil,
            Function<T, String[]> searchFieldsExtractor,
            Runnable updateCallback) {

        filterControls.getSearchField().textProperty().addListener((obs, oldVal, newVal) -> {
            filterUtil.setKeywordFilter(item -> {
                String[] fields = searchFieldsExtractor.apply(item);
                return TableFilterUtil.matchesKeyword(newVal, fields);
            });
            updateCallback.run();
        });
    }

    public static <T> void setupCategoryFilter(
            FilterControls filterControls,
            TableFilterUtil<T> filterUtil,
            Function<T, String> categoryExtractor,
            Runnable updateCallback) {

        filterControls.getPrimaryFilter().valueProperty().addListener((obs, oldVal, newVal) -> {
            filterUtil.setCategoryFilter(item -> TableFilterUtil.matchesFilter(newVal, categoryExtractor.apply(item)));
            updateCallback.run();
        });
    }

    public static <T> void setupStatusFilter(
            FilterControls filterControls,
            TableFilterUtil<T> filterUtil,
            Function<T, String> statusExtractor,
            Runnable updateCallback) {

        filterControls.getStatusFilter().valueProperty().addListener((obs, oldVal, newVal) -> {
            filterUtil.setStatusFilter(item -> TableFilterUtil.matchesFilter(newVal, statusExtractor.apply(item)));
            updateCallback.run();
        });
    }

    public static <T> void setupLocationFilterListener(
            FilterControls filterControls,
            TableFilterUtil<T> filterUtil,
            Function<T, String> locationExtractor,
            Runnable updateCallback) {

        filterControls.getLocationFilter().valueProperty().addListener((obs, oldVal, newVal) -> {
            filterUtil.setLocationFilter(item -> TableFilterUtil.matchesFilter(newVal, locationExtractor.apply(item)));
            updateCallback.run();
        });
    }

    public static <T> void setupDateFilter(
            FilterControls filterControls,
            TableFilterUtil<T> filterUtil,
            Function<T, String> startDateExtractor,
            Function<T, String> endDateExtractor,
            DateTimeFormatter dateFormatter,
            Runnable updateCallback) {

        Runnable updateDateFilter = () -> {
            LocalDate filterStartDate = filterControls.getStartDatePicker().getValue();
            LocalDate filterEndDate = filterControls.getEndDatePicker().getValue();

            filterUtil.setDateFilter(item -> {
                if (filterStartDate == null && filterEndDate == null)
                    return true;

                try {
                    String startDateStr = startDateExtractor.apply(item);
                    String endDateStr = endDateExtractor.apply(item);

                    if (startDateStr == null || startDateStr.trim().isEmpty())
                        return true;

                    LocalDate itemStartDate = LocalDate.parse(startDateStr, dateFormatter);
                    LocalDate itemEndDate = endDateStr != null && !endDateStr.trim().isEmpty()
                            ? LocalDate.parse(endDateStr, dateFormatter)
                            : null;

                    if (filterStartDate != null && itemEndDate != null && itemEndDate.isBefore(filterStartDate))
                        return false;
                    if (filterEndDate != null && itemStartDate.isAfter(filterEndDate))
                        return false;

                    return true;
                } catch (DateTimeParseException e) {
                    return true; // Include items with invalid dates in results
                }
            });
            updateCallback.run();
        };

        filterControls.getStartDatePicker().valueProperty()
                .addListener((obs, oldVal, newVal) -> updateDateFilter.run());
        filterControls.getEndDatePicker().valueProperty().addListener((obs, oldVal, newVal) -> updateDateFilter.run());
    }

    public static <T> void setupClearFiltersButton(
            FilterControls filterControls,
            TableFilterUtil<T> filterUtil,
            boolean hasLocationFilter,
            boolean hasDateFilter,
            Runnable updateCallback) {

        filterControls.getClearFiltersButton().setOnAction(e -> {
            filterControls.getSearchField().clear();
            filterControls.getPrimaryFilter().setValue("All");
            filterControls.getStatusFilter().setValue("All");

            if (hasLocationFilter) {
                filterControls.getLocationFilter().setValue("All");
            }

            if (hasDateFilter) {
                filterControls.getStartDatePicker().setValue(null);
                filterControls.getEndDatePicker().setValue(null);
            }

            filterUtil.clearAllFilters();
            updateCallback.run();
        });
    }

    public static void insertFilterControlsIntoContainer(VBox mainContainer, FilterControls filterControls) {
        if (mainContainer != null) {
            mainContainer.getChildren().add(0, filterControls);
        }
    }
}
