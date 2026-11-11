
package com.digitaldetective.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class CaseDAO {

    // Create and save a new investigation case
    public static boolean createCase(
            int userId, String title, String description)
            throws SQLException {

        String sql = "INSERT INTO cases "
                + "(user_id, case_title, description) "
                + "VALUES (?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setString(2, title);
            statement.setString(3, description);

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;
        }
    }
    public static List<String> getCasesByUserId(int userId)
        throws SQLException {

    List<String> cases = new ArrayList<>();

    String sql = "SELECT id, case_title, description, status "
            + "FROM cases WHERE user_id = ?";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, userId);

        try (ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                String caseDetails =
                        "Case ID: " + result.getInt("id")
                        + "\nTitle: " + result.getString("case_title")
                        + "\nDescription: " + result.getString("description")
                        + "\nStatus: " + result.getString("status");

                cases.add(caseDetails);
            }
        }
    }

    return cases;
    }
    public static Map<Integer, String> getCaseTitlesByUserId(int userId)
        throws SQLException {

    Map<Integer, String> cases = new LinkedHashMap<>();

    String sql = "SELECT id, case_title FROM cases WHERE user_id = ?";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, userId);

        try (ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                cases.put(
                        result.getInt("id"),
                        result.getString("case_title")
                );
            }
        }
    }

    return cases;
}
public static void checkCases(int userId) throws SQLException {

    String sql = "SELECT id, user_id, case_title FROM cases";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet result = statement.executeQuery()) {

        System.out.println("\n--- ALL SAVED CASES ---");

        while (result.next()) {
            System.out.println(
                "ID: " + result.getInt("id") +
                " | User ID: " + result.getInt("user_id") +
                " | Title: " + result.getString("case_title")
            );
        }
    }
}
}

