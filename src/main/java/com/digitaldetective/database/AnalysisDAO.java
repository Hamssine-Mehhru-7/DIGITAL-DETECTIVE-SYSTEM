package com.digitaldetective.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnalysisDAO {

    public static boolean saveAnalysis(
            int caseId,
            String analysisType,
            String content,
            String result) throws SQLException {

        String sql = "INSERT INTO analysis_results "
                + "(case_id, analysis_type, input_content, analysis_result) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, caseId);
            statement.setString(2, analysisType);
            statement.setString(3, content);
            statement.setString(4, result);

            return statement.executeUpdate() > 0;
        }
    }

    public static List<String> getAnalysisByCaseId(int caseId)
            throws SQLException {

        List<String> results = new ArrayList<>();

        String sql = "SELECT * FROM analysis_results WHERE case_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, caseId);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    String details =
                            "Analysis ID: " + rs.getInt("id")
                            + "\nType: " + rs.getString("analysis_type")
                            + "\nInput: " + rs.getString("input_content")
                            + "\nResult: " + rs.getString("analysis_result")
                            + "\nDate: " + rs.getString("created_at");

                    results.add(details);
                }
            }
        }

        return results;
    }
}
