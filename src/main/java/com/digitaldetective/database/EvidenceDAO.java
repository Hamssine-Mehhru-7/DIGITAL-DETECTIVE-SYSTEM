package com.digitaldetective.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EvidenceDAO {

    public static boolean addEvidence(
            int caseId, String evidenceType, String content)
            throws SQLException {

        String sql = "INSERT INTO evidence "
                + "(case_id, evidence_type, evidence_content) "
                + "VALUES (?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, caseId);
            statement.setString(2, evidenceType);
            statement.setString(3, content);

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;
        }
    }
    public static List<String> getEvidenceByCaseId(int caseId)
        throws SQLException {

    List<String> evidenceList = new ArrayList<>();

    String sql = "SELECT id, evidence_type, evidence_content, created_at "
            + "FROM evidence WHERE case_id = ?";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, caseId);

        try (ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                String details =
                        "Evidence ID: " + result.getInt("id")
                        + "\nType: " + result.getString("evidence_type")
                        + "\nContent: " + result.getString("evidence_content")
                        + "\nAdded: " + result.getString("created_at");

                evidenceList.add(details);
            }
        }
    }

    return evidenceList;
    }
}