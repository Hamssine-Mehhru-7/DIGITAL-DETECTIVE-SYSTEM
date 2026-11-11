
package com.digitaldetective.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.sql.ResultSet;
import java.security.MessageDigest;
import java.util.Map;
import java.util.LinkedHashMap;

public class UserDAO {

    // Register a new user
    public static boolean registerUser(
            String name, String email, String password)
            throws SQLException {

        String sql = "INSERT INTO users "
                + "(full_name, email, password_hash) "
                + "VALUES (?, ?, ?)";

        String hashedPassword = hashPassword(password);

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, hashedPassword);

            statement.executeUpdate();

            return true;
        }
    }

    // Convert password into a secure hash
    private static String hashPassword(String password) {

        try {
            byte[] salt = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            KeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    salt,
                    120000,
                    256
            );

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            return Base64.getEncoder().encodeToString(salt)
                    + ":"
                    + Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Password hashing failed.", e);
        }
    }
    
    // Verify login credentials
    public static boolean loginUser(
            String email, String password) throws SQLException {

        String sql = "SELECT password_hash FROM users WHERE email = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    String storedHash = result.getString("password_hash");

                    return verifyPassword(password, storedHash);
                }
            }
        }

        return false;
    }

    // Compare entered password with stored password hash
    private static boolean verifyPassword(
            String password, String storedHash) {

        try {
            String[] parts = storedHash.split(":");

            if (parts.length != 2) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] originalHash = Base64.getDecoder().decode(parts[1]);

            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    salt,
                    120000,
                    256
            );

            try {
                SecretKeyFactory factory =
                        SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

                byte[] enteredHash =
                        factory.generateSecret(spec).getEncoded();

                return MessageDigest.isEqual(originalHash, enteredHash);

            } finally {
                spec.clearPassword();
            }

        } catch (Exception e) {
            return false;
        }
    }
    
    // Get the ID of a registered user using email
    public static int getUserIdByEmail(String email)
            throws SQLException {

        String sql = "SELECT id FROM users WHERE email = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return result.getInt("id");
                }
            }
        }

        return -1;
    }

public static Map<String, String> getProfile(int userId)
        throws SQLException {

    Map<String, String> profile = new LinkedHashMap<>();

    String sql = "SELECT full_name, email FROM users WHERE id = ?";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, userId);

        try (ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                profile.put("name", rs.getString("full_name"));
                profile.put("email", rs.getString("email"));
            }
        }
    }

    return profile;
}

public static boolean updateProfile(
        int userId, String name, String email) throws SQLException {

    String sql = "UPDATE users SET full_name = ?, email = ? WHERE id = ?";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setString(1, name);
        statement.setString(2, email);
        statement.setInt(3, userId);

        return statement.executeUpdate() > 0;
    }
}

public static boolean changePassword(
        int userId, String currentPassword, String newPassword)
        throws SQLException {

    String sql = "SELECT password_hash FROM users WHERE id = ?";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, userId);

        try (ResultSet rs = statement.executeQuery()) {
            if (!rs.next()) {
                return false;
            }

            String storedHash = rs.getString("password_hash");

            if (!verifyPassword(currentPassword, storedHash)) {
                return false;
            }
        }
    }

    String updateSql =
            "UPDATE users SET password_hash = ? WHERE id = ?";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(updateSql)) {

        statement.setString(1, hashPassword(newPassword));
        statement.setInt(2, userId);

        return statement.executeUpdate() > 0;
    }
}



}

