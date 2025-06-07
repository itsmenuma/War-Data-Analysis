package com.warManagementGUI.services;

import com.warManagementGUI.models.User;
import com.warManagementGUI.models.Role;
import com.warManagementGUI.models.Permission;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Authentication service that manages user login/logout and permissions
 */
public class AuthenticationService {
    private static AuthenticationService instance;
    private Map<String, User> users;
    private User currentUser;

    private AuthenticationService() {
        users = new HashMap<>();
        initializeDefaultUsers();
    }

    public static AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    /**
     * Initialize default users for demo purposes
     */
    private void initializeDefaultUsers() {
        // Default admin user
        users.put("admin", new User("admin", "admin123", Role.ADMIN,
                "System", "Administrator", "admin@wardata.mil"));

        // Default analyst user
        users.put("analyst", new User("analyst", "analyst123", Role.ANALYST,
                "Data", "Analyst", "analyst@wardata.mil"));

        // Default viewer user
        users.put("viewer", new User("viewer", "viewer123", Role.VIEWER,
                "Data", "Viewer", "viewer@wardata.mil"));

        // Additional sample users
        users.put("john.doe", new User("john.doe", "password123", Role.ANALYST,
                "John", "Doe", "john.doe@wardata.mil"));

        users.put("jane.smith", new User("jane.smith", "password123", Role.VIEWER,
                "Jane", "Smith", "jane.smith@wardata.mil"));
    }

    /**
     * Attempt to login with username and password
     */
    public boolean login(String username, String password) {
        User user = users.get(username.toLowerCase());
        if (user != null && user.getPassword().equals(password) && user.isActive()) {
            currentUser = user;
            currentUser.setLastLogin(LocalDateTime.now());
            return true;
        }
        return false;
    }

    /**
     * Logout the current user
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Get the currently logged in user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Check if a user is currently logged in
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Check if the current user has a specific permission
     */
    public boolean hasPermission(Permission permission) {
        return isLoggedIn() && currentUser.hasPermission(permission);
    }

    /**
     * Get the role of the current user
     */
    public Role getCurrentUserRole() {
        return isLoggedIn() ? currentUser.getRole() : null;
    }

    /**
     * Get the username of the current user
     */
    public String getCurrentUsername() {
        return isLoggedIn() ? currentUser.getUsername() : null;
    }

    /**
     * Get the full name of the current user
     */
    public String getCurrentUserFullName() {
        return isLoggedIn() ? currentUser.getFullName() : null;
    }

    /**
     * Add a new user to the system (Admin only)
     */
    public boolean addUser(User user) {
        if (hasPermission(Permission.MANAGE_USERS)) {
            users.put(user.getUsername().toLowerCase(), user);
            return true;
        }
        return false;
    }

    /**
     * Remove a user from the system (Admin only)
     */
    public boolean removeUser(String username) {
        if (hasPermission(Permission.MANAGE_USERS)) {
            return users.remove(username.toLowerCase()) != null;
        }
        return false;
    }

    /**
     * Update a user's information (Admin only)
     */
    public boolean updateUser(User user) {
        if (hasPermission(Permission.MANAGE_USERS)) {
            users.put(user.getUsername().toLowerCase(), user);
            return true;
        }
        return false;
    }

    /**
     * Get all users (Admin only)
     */
    public Map<String, User> getAllUsers() {
        if (hasPermission(Permission.MANAGE_USERS)) {
            return new HashMap<>(users);
        }
        return new HashMap<>();
    }

    /**
     * Change password for current user
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (isLoggedIn() && currentUser.getPassword().equals(oldPassword)) {
            currentUser.setPassword(newPassword);
            return true;
        }
        return false;
    }
}
