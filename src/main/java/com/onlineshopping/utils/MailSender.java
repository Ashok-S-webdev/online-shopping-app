package com.onlineshopping.utils;

// Interface for MailSender
public interface MailSender {
    void sendMail(String userEmail, byte[] pdf);
}
