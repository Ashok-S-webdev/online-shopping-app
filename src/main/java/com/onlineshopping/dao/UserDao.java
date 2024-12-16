package com.onlineshopping.dao;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.onlineshopping.model.User;
import com.onlineshopping.utils.DBUtils;
import com.onlineshopping.utils.ImageUtils;

// Database Operations on Users
public class UserDao {

    // Method to get user details from database
    public static List<User> getUsersList(int id) {
        List<User> users = new ArrayList<>();
        final String query = "SELECT * FROM users WHERE user_id != ?";
        try(Connection connection = DBUtils.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String username = resultSet.getString("username");
                String passwordHash = resultSet.getString("password");
                String email = resultSet.getString("email");
                String mobile = resultSet.getString("mobile_number");
                String role = resultSet.getString("role");
                Blob imageBlob = resultSet.getBlob("profile_picture");

                String image = ImageUtils.Base64Converter(imageBlob);
                User user = new User(userId, username, passwordHash, email, mobile, role, image);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    // Method to new user to database
    public static boolean addUser(String username, String passwordHash, String email, String mobile, InputStream imageContent) {
        final String query = "INSERT INTO users (username, password, email, mobile_number, profile_picture) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
            stmt.setString(3, email);
            stmt.setString(4, mobile);
            stmt.setBlob(5, imageContent);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("New user created successfully");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to get user details by user id from database
    public static User getUserById(int userId) throws SQLException {
        final String query = "SELECT * FROM users WHERE user_id = ?";
        User user = null;

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                String passwordHash = rs.getString("password");
                String email = rs.getString("email");
                String mobile = rs.getString("mobile_number");
                String role = rs.getString("role");
                Blob imageBlob = rs.getBlob("profile_picture");
                
                String image = ImageUtils.Base64Converter(imageBlob);

                user = new User(userId, username, passwordHash, email, mobile, role, image);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // Method to get user details by username from database
    public static User getUserByUsername(String username) throws SQLException {
        final String query = "SELECT * FROM users WHERE username = ?";
        User user = null;

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String dbUsername = rs.getString("username");
                String passwordHash = rs.getString("password");
                String email = rs.getString("email");
                String mobile = rs.getString("mobile_number");
                String role = rs.getString("role");
                Blob imageBlob = rs.getBlob("profile_picture");
                
                String image = ImageUtils.Base64Converter(imageBlob);

                user = new User(userId, dbUsername, passwordHash, email, mobile, role, image);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return user;
    }

    // Method to update user profile to database
    public static void updateUserProfile(int id, String email, String mobile) {
        final String query = "UPDATE users SET email = ?, mobile_number = ? WHERE user_id = ?";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, mobile);
            statement.setInt(3, id);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Profile details updated");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        // Method to update user profile along with picture in database
    public static void updateUserProfileWithImage(int id, String email, String mobile, InputStream fileContent) {
        final String query = "UPDATE users SET email = ?, mobile_number = ?, profile_picture = ? WHERE user_id = ?";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, mobile);
            statement.setBlob(3, fileContent);
            statement.setInt(4, id);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Profile details updated");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
