package DataAnalysis;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Units_DataSet {
    private static final String url = "jdbc:mysql://localhost:3306/war";
    private static final String user = "root";
    private static final String password = "rayees@123";

    public static Map<String, Integer> getPersonnelStatusCount() {
        Map<String, Integer> personnelStatusCount = new HashMap<>();

        String query = "SELECT unit_type, COUNT(*) AS count FROM Units GROUP BY unit_type";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String type = rs.getString("unit_type");
                int count = rs.getInt("count");
                personnelStatusCount.put(type, count);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return personnelStatusCount;
    }
}
