package DataAnalysis;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Equipment_database {
    private static final String url = "jdbc:mysql://localhost:3306/war";
    private static final String user = "root";
    private static final String password = "rayees@123";

    public static Map<String, Integer> getPersonnelStatusCount() {
        Map<String, Integer> personnelStatusCount = new HashMap<>();

        String query = "SELECT status, COUNT(*) AS count FROM Equipment GROUP BY status";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("count");
                personnelStatusCount.put(status, count);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return personnelStatusCount;
    }
}
