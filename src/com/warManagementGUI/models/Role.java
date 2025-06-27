package com.warManagementGUI.models;

import java.util.Set;
import java.util.EnumSet;

/**
 * Enumeration of user roles with their associated permissions
 */
public enum Role {
    ADMIN("Administrator", "Full system access", EnumSet.of(
            Permission.READ_DATA,
            Permission.WRITE_DATA,
            Permission.DELETE_DATA,
            Permission.ANALYZE_DATA,
            Permission.MANAGE_USERS,
            Permission.EXPORT_DATA,
            Permission.IMPORT_DATA,
            Permission.VIEW_ANALYTICS,
            Permission.MANAGE_SETTINGS)),

    ANALYST("Analyst", "Can analyze and modify data but cannot delete", EnumSet.of(
            Permission.READ_DATA,
            Permission.WRITE_DATA,
            Permission.ANALYZE_DATA,
            Permission.EXPORT_DATA,
            Permission.VIEW_ANALYTICS)),

    VIEWER("Viewer", "Read-only access to data", EnumSet.of(
            Permission.READ_DATA,
            Permission.VIEW_ANALYTICS));

    private final String displayName;
    private final String description;
    private final Set<Permission> permissions;

    Role(String displayName, String description, Set<Permission> permissions) {
        this.displayName = displayName;
        this.description = description;
        this.permissions = permissions;
    }

    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
