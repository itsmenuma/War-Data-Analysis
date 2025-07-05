package com.warManagementGUI.models;

/**
 * Enumeration of all permissions in the system
 */
public enum Permission {
    READ_DATA("View Data"),
    WRITE_DATA("Add/Edit Data"),
    DELETE_DATA("Delete Data"),

    ANALYZE_DATA("Analyze Data"),

    MANAGE_USERS("Manage Users"),

    EXPORT_DATA("Export Data"),
    IMPORT_DATA("Import Data"),

    VIEW_ANALYTICS("View Analytics"),
    MANAGE_SETTINGS("Manage Settings");

    private final String description;

    Permission(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
