package com.warManagementGUI.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/war";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";
    private static final List<String> TABLE_NAMES = new ArrayList<>(Arrays.asList("personnel", "equipment", "missions", "supplies", "units"));

    public static Connection getConnection() throws SQLException {
        if (PASSWORD.equals("your_password")) {
            throw new SQLException("Database password not set. Please update the DBUtil class.");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }    
    
    private static boolean isValidTableName(String tableName) {
        if (tableName == null || tableName.isEmpty()) {
            return false;
        }
        return TABLE_NAMES.contains(tableName.toLowerCase());
    }
    
    public static Map<String, Integer> getStatusCount(String tableName) {
        Map<String, Integer> statusCount = new HashMap<>();
       
        if (!isValidTableName(tableName)) {
            System.err.println("Invalid table name: " + tableName);
            return statusCount;
        }
        
        String query = "SELECT status, COUNT(*) AS count FROM " + tableName + " GROUP BY status";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

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
    }    public static Map<String, Integer> getGroupCount(String tableName, String columnName) {
        Map<String, Integer> countMap = new HashMap<>();
        
       
        if (!isValidTableName(tableName) || !columnName.matches("^[a-zA-Z0-9_]+$")) {
            System.err.println("Invalid table or column name: " + tableName + "." + columnName);
            return countMap;
        }
        
        String query = "SELECT " + columnName + " AS label, COUNT(*) AS count FROM " + tableName + " GROUP BY " + columnName;
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

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
    
    /**
     * Executes a query and returns a Query object that handles resource management
     */
    public static Query executeQuery(String query, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query);
        
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        
        ResultSet rs = pstmt.executeQuery();
        return new Query(conn, pstmt, rs);
    }
    
    /**
     * Wrapper class for query results that handles resource management
     */
    public static class Query implements AutoCloseable {
        private final Connection conn;
        private final PreparedStatement pstmt;
        private final ResultSet rs;
        
        public Query(Connection conn, PreparedStatement pstmt, ResultSet rs) {
            this.conn = conn;
            this.pstmt = pstmt;
            this.rs = rs;
        }
        
        public ResultSet getResultSet() {
            return rs;
        }
        
        @Override
        public void close() throws Exception {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
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
