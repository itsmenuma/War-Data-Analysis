package com.warManagementGUI.DataAnalysis;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jfree.data.category.DefaultCategoryDataset;

import com.warManagementGUI.util.DBUtil;

public class PersonnelDataDAO {

    public DefaultCategoryDataset getPersonnelData() throws SQLException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String query = "SELECT status, COUNT(Personnel_ID) as count FROM Personnel GROUP BY ASC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("count");
                dataset.addValue(count, "Personnel Count", status);
            }
        }
        return dataset;
    }
}