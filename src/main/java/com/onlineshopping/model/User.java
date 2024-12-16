package com.onlineshopping.model;

public class User {
    private String image;
    private int userId;
    private String username;
    private String passwordHash;
    private String email;
    private String mobile;
    private String role;


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
