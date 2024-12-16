package com.example.utils;

// Interface for MailSender
public interface MailSender {
    void sendMail(String userEmail, byte[] pdf);
}
