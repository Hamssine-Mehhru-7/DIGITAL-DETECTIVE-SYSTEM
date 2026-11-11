package com.digitaldetective.database;

import java.sql.*;
import java.util.*;

public final class ScanDAO {
    private ScanDAO() {}

    public static int saveScan(int userId, String type, String content, String result, int score, String level) throws SQLException {
        String sql = "INSERT INTO user_scans (user_id, analysis_type, input_content, analysis_result, risk_score, risk_level) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setInt(1, userId); p.setString(2, type); p.setString(3, content); p.setString(4, result); p.setInt(5, score); p.setString(6, level);
            p.executeUpdate();
            try (ResultSet keys = p.getGeneratedKeys()) { return keys.next() ? keys.getInt(1) : -1; }
        }
    }

    public static int countScans(int userId) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement p = c.prepareStatement("SELECT COUNT(*) FROM user_scans WHERE user_id = ?")) {
            p.setInt(1, userId); try (ResultSet r = p.executeQuery()) { return r.next() ? r.getInt(1) : 0; }
        }
    }

    public static int countCases(int userId) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement p = c.prepareStatement("SELECT COUNT(*) FROM cases WHERE user_id = ?")) {
            p.setInt(1, userId); try (ResultSet r = p.executeQuery()) { return r.next() ? r.getInt(1) : 0; }
        }
    }

    public static int countEvidence(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM evidence e JOIN cases c ON e.case_id = c.id WHERE c.user_id = ?";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, userId); try (ResultSet r = p.executeQuery()) { return r.next() ? r.getInt(1) : 0; }
        }
    }

    public static int countHighRisk(int userId) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement p = c.prepareStatement("SELECT COUNT(*) FROM user_scans WHERE user_id = ? AND risk_level = 'HIGH'")) {
            p.setInt(1, userId); try (ResultSet r = p.executeQuery()) { return r.next() ? r.getInt(1) : 0; }
        }
    }

    public static List<Map<String,Object>> getRecentScans(int userId) throws SQLException {
        List<Map<String,Object>> rows = new ArrayList<>();
        String sql = "SELECT id, analysis_type, input_content, risk_score, risk_level, created_at FROM user_scans WHERE user_id = ? ORDER BY id DESC LIMIT 8";
        try (Connection c = DatabaseConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, userId); try (ResultSet r = p.executeQuery()) {
                while (r.next()) { Map<String,Object> row = new LinkedHashMap<>(); row.put("id", r.getInt("id")); row.put("type", r.getString("analysis_type"));
                    String input = r.getString("input_content"); row.put("preview", input.length() > 90 ? input.substring(0,90) + "…" : input);
                    row.put("score", r.getInt("risk_score")); row.put("level", r.getString("risk_level")); row.put("createdAt", r.getString("created_at")); rows.add(row); }
            }
        }
        return rows;
    }
}
