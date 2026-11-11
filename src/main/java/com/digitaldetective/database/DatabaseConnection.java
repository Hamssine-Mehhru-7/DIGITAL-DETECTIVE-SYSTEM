
package com.digitaldetective.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    // SQLite database file
    private static final String URL =
        "jdbc:sqlite:digital_detective.db";

    // Establish database connection
    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(URL);
    }

    // Create the required database tables
    public static void initializeDatabase() {

        String createUsersTable =
            "CREATE TABLE IF NOT EXISTS users (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "full_name TEXT NOT NULL, " +
            "email TEXT NOT NULL UNIQUE, " +
            "password_hash TEXT NOT NULL, " +
            "created_at TEXT DEFAULT CURRENT_TIMESTAMP" +
            ")";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createUsersTable);
            String createCasesTable =
                "CREATE TABLE IF NOT EXISTS cases (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "case_title TEXT NOT NULL, " +
                "description TEXT, " +
                "status TEXT DEFAULT 'Open', " +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (user_id) REFERENCES users(id)" +
                ")";

            statement.execute(createCasesTable);
            String createEvidenceTable =
                    "CREATE TABLE IF NOT EXISTS evidence (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "case_id INTEGER NOT NULL, " +
                    "evidence_type TEXT NOT NULL, " +
                    "evidence_content TEXT NOT NULL, " +
                    "created_at TEXT DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (case_id) REFERENCES cases(id)" +
                    ")";

            statement.execute(createEvidenceTable);
            String createAnalysisTable =
                "CREATE TABLE IF NOT EXISTS analysis_results (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "case_id INTEGER NOT NULL, " +
                "analysis_type TEXT NOT NULL, " +
                "input_content TEXT NOT NULL, " +
                "analysis_result TEXT NOT NULL, " +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (case_id) REFERENCES cases(id)" +
                ")";

            statement.execute(createAnalysisTable);

            String createUserScansTable =
                "CREATE TABLE IF NOT EXISTS user_scans (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "analysis_type TEXT NOT NULL, " +
                "input_content TEXT NOT NULL, " +
                "analysis_result TEXT NOT NULL, " +
                "risk_score INTEGER NOT NULL, " +
                "risk_level TEXT NOT NULL, " +
                "created_at TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (user_id) REFERENCES users(id))";
            statement.execute(createUserScansTable);

            System.out.println("Analysis results table created successfully!");

            System.out.println("Evidence table created successfully!");

            System.out.println("Cases table created successfully!");

            System.out.println("Database connected successfully!");
            System.out.println("Users table created successfully!");

        } catch (SQLException e) {

            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }
}

