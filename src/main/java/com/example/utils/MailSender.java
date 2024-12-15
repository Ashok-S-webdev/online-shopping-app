package com.example.utils;

public interface MailSender {
    void sendMail(String userEmail, byte[] pdf);
}
