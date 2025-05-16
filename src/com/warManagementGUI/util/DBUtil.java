package com.warManagementGUI.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/war";
    private static final String USER = "root";
    private static final String PASSWORD = "SP1234sp()";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static Map<String, Integer> getStatusCount(String tableName) {
        Map<String, Integer> statusCount = new HashMap<>();
        String query = String.format("SELECT status, COUNT(*) AS count FROM %s GROUP BY status", tableName);

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("count");
                statusCount.put(status, count);
            }

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            JOptionPane.showMessageDialog(new JPanel(), "Database connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return statusCount;
    }

    public static Map<String, Integer> getGroupCount(String tableName, String columnName) {
        Map<String, Integer> countMap = new HashMap<>();
        String query = String.format("SELECT %s AS label, COUNT(*) AS count FROM %s GROUP BY %s", columnName, tableName, columnName);
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String label = rs.getString("label");
                int count = rs.getInt("count");
                countMap.put(label, count);
            }

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            JOptionPane.showMessageDialog(new JPanel(), "Database connection error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return countMap;
    }
    
    public static void executeUpdate(String query, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            
            pstmt.executeUpdate();
        }
    }
    
    public static ResultSet executeQuery(String query, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        
        return pstmt.executeQuery();
    }

    public static Map<String, Integer> getPersonnelStatusCount() {
        return getStatusCount("Personnel");
    }

    public static Map<String, Integer> getEquipmentStatusCount() {
        return getStatusCount("Equipment");
    }

    public static Map<String, Integer> getMissionsStatusCount() {
        return getStatusCount("Missions");
    }

    public static Map<String, Integer> getSuppliesStatusCount() {
        return getStatusCount("Supplies");
    }

    public static Map<String, Integer> getUnitsByTypeCount() {
        return getGroupCount("Units", "unit_type");
    }
}
