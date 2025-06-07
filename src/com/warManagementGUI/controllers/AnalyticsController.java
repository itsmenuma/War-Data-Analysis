package com.warManagementGUI.controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import com.warManagementGUI.DataAnalysis.EquipmentBarChart;
import com.warManagementGUI.DataAnalysis.MissionsBarChart;
import com.warManagementGUI.DataAnalysis.PersonnelBarChart;
import com.warManagementGUI.DataAnalysis.SuppliesBarChart;
import com.warManagementGUI.DataAnalysis.UnitsBarChart;
import com.warManagementGUI.util.DBUtil;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AnalyticsController extends BaseController implements Initializable {

    @FXML
    private Button personnelAnalyticsBtn;
    @FXML
    private Button unitsAnalyticsBtn;
    @FXML
    private Button equipmentAnalyticsBtn;
    @FXML
    private Button suppliesAnalyticsBtn;
    @FXML
    private Button missionsAnalyticsBtn;
    @FXML
    private Button exportCsvBtn;
    @FXML
    private Button exportReportBtn;
    @FXML
    private Button refreshBtn;
    @FXML
    private Button backBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize theme functionality
        initializeTheme();
        Platform.runLater(() -> {
            refreshData();
        });
    }

    @FXML
    private void showPersonnelAnalytics() {
        try {

            PersonnelBarChart.showPersonnelStatusChart();
            PersonnelBarChart.showPersonnelPostChart();

        } catch (Exception e) {
            showErrorAlert("Error displaying personnel analytics: " + e.getMessage());
        }
    }

    @FXML
    private void showUnitsAnalytics() {
        try {
            UnitsBarChart.showUnitTypeChart();
        } catch (Exception e) {
            showErrorAlert("Error displaying units analytics: " + e.getMessage());
        }
    }

    @FXML
    private void showEquipmentAnalytics() {
        try {

            EquipmentBarChart.showEquipmentStatusChart();
            EquipmentBarChart.showEquipmentTypeChart();

        } catch (Exception e) {
            showErrorAlert("Error displaying equipment analytics: " + e.getMessage());
        }
    }

    @FXML
    private void showSuppliesAnalytics() {
        try {

            SuppliesBarChart.showSupplyStatusChart();
            SuppliesBarChart.showSupplyTypeChart();

        } catch (Exception e) {
            showErrorAlert("Error displaying supplies analytics: " + e.getMessage());
        }
    }

    @FXML
    private void showMissionsAnalytics() {
        try {

            MissionsBarChart.showMissionStatusChart();

        } catch (Exception e) {
            showErrorAlert("Error displaying missions analytics: " + e.getMessage());
        }
    }

    @FXML
    private void exportToCSV() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Data to CSV");
            fileChooser.setInitialFileName("war_data_export_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

            Stage stage = (Stage) exportCsvBtn.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                exportAllDataToCSV(file);
                showInfoAlert("Export Successful",
                        "Data has been successfully exported to:\n" + file.getAbsolutePath());
            }
        } catch (Exception e) {
            showErrorAlert("Error exporting to CSV: " + e.getMessage());
        }
    }

    @FXML
    private void generateReport() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Generate Comprehensive Report");
            fileChooser.setInitialFileName("war_data_report_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            Stage stage = (Stage) exportReportBtn.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                generateComprehensiveReport(file);
                showInfoAlert("Report Generated",
                        "Comprehensive report has been generated:\n" + file.getAbsolutePath());
            }
        } catch (Exception e) {
            showErrorAlert("Error generating report: " + e.getMessage());
        }
    }

    @FXML
    private void refreshData() {
        try {

            Platform.runLater(() -> {

                showInfoAlert("Data Refreshed", "Analytics data has been refreshed successfully.");
            });
        } catch (Exception e) {
            showErrorAlert("Error refreshing data: " + e.getMessage());
        }
    }

    @FXML
    private void goBack() {
        try {
            Stage currentStage = (Stage) backBtn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/warManagementGUI/fxml/Dashboard.fxml"));
            Scene scene = new Scene(loader.load());
            // Apply the current theme instead of always using light theme
            themeManager.applyThemeToScene(scene);
            currentStage.setScene(scene);
            currentStage.setTitle("War Management System - Dashboard");
        } catch (IOException e) {
            showErrorAlert("Error navigating to dashboard: " + e.getMessage());
        }
    }

    private void exportAllDataToCSV(File file) throws IOException, SQLException {
        try (FileWriter writer = new FileWriter(file)) {

            writer.append("Table,ID,Name,Status,Type,Additional_Info,Date_Created\n");

            exportTableToCSV(writer, "personnel", "Personnel_ID", "name", "status", "post");

            exportTableToCSV(writer, "units", "Unit_ID", "unit_name", "status", "unit_type");

            exportTableToCSV(writer, "equipment", "Equipment_ID", "equipment_name", "status", "equipment_type");

            exportTableToCSV(writer, "supplies", "Supply_ID", "supply_name", "status", "supply_type");

            exportTableToCSV(writer, "missions", "Mission_ID", "mission_name", "status", "mission_type");
        }
    }

    private void exportTableToCSV(FileWriter writer, String tableName, String idColumn,
            String nameColumn, String statusColumn, String typeColumn)
            throws IOException, SQLException {

        String query = String.format(
                "SELECT %s, %s, %s, %s FROM %s ORDER BY %s",
                idColumn, nameColumn, statusColumn, typeColumn, tableName, idColumn);

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                writer.append(String.format("%s,%d,%s,%s,%s,%s\n",
                        tableName,
                        rs.getInt(1),
                        rs.getString(2) != null ? rs.getString(2).replace(",", ";") : "",
                        rs.getString(3) != null ? rs.getString(3).replace(",", ";") : "",
                        rs.getString(4) != null ? rs.getString(4).replace(",", ";") : "",
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            }
        }
    }

    private void generateComprehensiveReport(File file) throws IOException, SQLException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("WAR DATA ANALYSIS - COMPREHENSIVE REPORT\n");
            writer.write("Generated on: "
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
            writer.write("============================================================\n\n");

            generateSectionReport(writer, "PERSONNEL SUMMARY", "personnel", "status", "post");

            generateSectionReport(writer, "UNITS SUMMARY", "units", "status", "unit_type");

            generateSectionReport(writer, "EQUIPMENT SUMMARY", "equipment", "status", "equipment_type");

            generateSectionReport(writer, "SUPPLIES SUMMARY", "supplies", "status", "supply_type");

            generateSectionReport(writer, "MISSIONS SUMMARY", "missions", "status", "mission_type");

            writer.write("\nOVERALL STATISTICS\n");
            writer.write("------------------------------\n");

            String[] tables = { "personnel", "units", "equipment", "supplies", "missions" };
            for (String table : tables) {
                int count = getTableRowCount(table);
                writer.write(String.format("Total %s: %d\n",
                        table.substring(0, 1).toUpperCase() + table.substring(1), count));
            }

            writer.write("\nReport generated by War Management System Analytics Module\n");
        }
    }

    private void generateSectionReport(FileWriter writer, String sectionTitle,
            String tableName, String statusColumn, String typeColumn)
            throws IOException, SQLException {
        writer.write(sectionTitle + "\n");

        StringBuilder underline = new StringBuilder();
        for (int i = 0; i < sectionTitle.length(); i++) {
            underline.append("-");
        }
        writer.write(underline.toString() + "\n");

        writer.write("By Status:\n");
        String statusQuery = String.format("SELECT %s, COUNT(*) FROM %s GROUP BY %s",
                statusColumn, tableName, statusColumn);
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(statusQuery);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                writer.write(String.format("  %s: %d\n", rs.getString(1), rs.getInt(2)));
            }
        }

        writer.write("By Type:\n");
        String typeQuery = String.format("SELECT %s, COUNT(*) FROM %s GROUP BY %s",
                typeColumn, tableName, typeColumn);
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(typeQuery);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                writer.write(String.format("  %s: %d\n", rs.getString(1), rs.getInt(2)));
            }
        }

        writer.write("\n");
    }

    private int getTableRowCount(String tableName) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName;
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
