package com.onlineshopping.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User details")
public class User {

    @Schema(description = "Unique Identifier of User", example = "1")
    private int userId;

    @Schema(description = "Username of the user", example = "user1")
    private String username;

    @Schema(description = "Hashed password of the user", example = "$2a$10$EixZaYVK1fsbw1Zfbx3OpO")
    private String passwordHash;

    @Schema(description = "Email ID of the user", example = "user1@gmail.com")
    private String email;

    @Schema(description = "Mobile Number of the user", example = "9876543210")
    private String mobile;

    @Schema(description = "Role of the user(admin/user)", example = "user")
    private String role;

    @Schema(description = "Base64 string of user profile picture")
    private String image;


    public User(String username, String passwordHash, String email, String mobile, String role, String image) {
        this.image = image;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.mobile = mobile;
        this.role = role;
    }

    public User(int userId, String username, String passwordHash, String email, String mobile, String role, String image) {
        this.userId = userId;
        this.image = image;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.mobile = mobile;
        this.role = role;
    }

    public void setUserImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "UserId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", Mobile=" + mobile +
                ", Role=" + role +
                ", Image=" + image +
                '}';
    }
}
