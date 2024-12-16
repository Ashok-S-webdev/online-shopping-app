package com.example.utils;

import org.mindrot.jbcrypt.BCrypt;

public class Security {

    // Method to encrypt password string using Bcrypt
    public static String encryptPassword(String password) {
        String salt = BCrypt.gensalt(10);
        return BCrypt.hashpw(password, salt);
    }

    // Method to check if the password matches the encrypted hash
    public static boolean checkPassword(String password, String passwordHash) {
        return BCrypt.checkpw(password, passwordHash);
    }
}
