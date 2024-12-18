package com.example.chatting_room_projectfx;

import java.sql.*;

public class UserDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/chatting_room_project";
    private static final String USER = "root";

    private static final String PASS = "";

    public UserDAO(Connection connection) {

    }

    public UserDAO() {

    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public boolean registerUser(String username, String password) {
        // Query includes status field with default value 'inactive'
        String query = "INSERT INTO users (username, password, status) VALUES (?, ?, 'inactive')";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    public boolean loginUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void updateUserStatus(String username, String status) throws SQLException {
        String query = "UPDATE users SET status = ? WHERE username = ?";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void setUserInactive(String username) throws SQLException {
        updateUserStatus(username, "inactive");
    }

    public void setUserActive(String username) throws SQLException {
        updateUserStatus(username, "active");
    }



}
