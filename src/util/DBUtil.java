package util;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return countMap;
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
