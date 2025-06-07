package com.warManagementGUI.models;

/**
 * Enumeration of all permissions in the system
 */
public enum Permission {
    // Data operations
    READ_DATA("View Data"),
    WRITE_DATA("Add/Edit Data"),
    DELETE_DATA("Delete Data"),

    // Analysis operations
    ANALYZE_DATA("Analyze Data"),

    // Administrative operations
    MANAGE_USERS("Manage Users"),

    // Import/Export operations
    EXPORT_DATA("Export Data"),
    IMPORT_DATA("Import Data"),

    // System operations
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
