
package com.digitaldetective;

import com.digitaldetective.database.DatabaseConnection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportGenerator {

    public static String generateReport(int caseId)
            throws SQLException, IOException {
        return generateReport(caseId, "All");
    }

    public static String generateReport(int caseId, String reportType)
            throws SQLException, IOException {

        StringBuilder report = new StringBuilder();

        report.append("DIGITAL DETECTIVE INVESTIGATION REPORT\n");
        report.append("=======================================\n\n");

        try (Connection connection = DatabaseConnection.getConnection()) {

            String caseSql =
                    "SELECT case_title, description, status "
                    + "FROM cases WHERE id = ?";

            try (PreparedStatement statement =
                         connection.prepareStatement(caseSql)) {

                statement.setInt(1, caseId);

                try (ResultSet rs = statement.executeQuery()) {

                    if (!rs.next()) {
                        throw new SQLException("Case not found.");
                    }

                    report.append("Case ID: ").append(caseId).append("\n");
                    report.append("Title: ")
                            .append(rs.getString("case_title")).append("\n");
                    report.append("Description: ")
                            .append(rs.getString("description")).append("\n");
                    report.append("Status: ")
                            .append(rs.getString("status")).append("\n\n");
                }
            }

            if ("All".equals(reportType)) {
                report.append("SAVED EVIDENCE\n");
                report.append("--------------\n");

                String evidenceSql =
                        "SELECT evidence_type, evidence_content, created_at "
                        + "FROM evidence WHERE case_id = ?";

                try (PreparedStatement statement =
                             connection.prepareStatement(evidenceSql)) {

                    statement.setInt(1, caseId);

                    try (ResultSet rs = statement.executeQuery()) {

                        while (rs.next()) {
                            report.append("Type: ")
                                    .append(rs.getString("evidence_type"))
                                    .append("\nContent: ")
                                    .append(rs.getString("evidence_content"))
                                    .append("\nAdded: ")
                                    .append(rs.getString("created_at"))
                                    .append("\n\n");
                        }
                    }
                }
            }

            report.append(reportType.equals("All")
                    ? "ANALYSIS RESULTS\n"
                    : reportType.toUpperCase() + " REPORT\n");

            report.append("----------------\n");

            String analysisSql =
                    "SELECT analysis_type, analysis_result, created_at "
                    + "FROM analysis_results WHERE case_id = ?";

            if (!"All".equals(reportType)) {
                analysisSql += " AND LOWER(analysis_type) = LOWER(?)";
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(analysisSql)) {

                statement.setInt(1, caseId);

                if (!"All".equals(reportType)) {
                    statement.setString(2, reportType);
                }

                try (ResultSet rs = statement.executeQuery()) {

                    boolean found = false;

                    while (rs.next()) {
                        found = true;

                        report.append("Type: ")
                                .append(rs.getString("analysis_type"))
                                .append("\nResult: ")
                                .append(rs.getString("analysis_result"))
                                .append("\nDate: ")
                                .append(rs.getString("created_at"))
                                .append("\n\n");
                    }

                    if (!found) {
                        report.append("No analysis results found for this type.\n");
                    }
                }
            }
        }

        File folder = new File("reports");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fileType = reportType.equals("All")
                ? "Complete"
                : reportType;

        File output = new File(
                folder,
                "Case_" + caseId + "_" + fileType + "_Report.txt"
        );

        try (FileWriter writer = new FileWriter(output)) {
            writer.write(report.toString());
        }

        return output.getAbsolutePath();
    }
}
