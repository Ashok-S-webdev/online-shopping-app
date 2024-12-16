package com.onlineshopping.utils;

import java.util.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;

public class GmailSender implements MailSender{
    private static final Logger logger = LoggerFactory.getLogger(GmailSender.class);
    
    // Method for sending mail via gmail
    public void sendMail(String userEmail, byte[] pdf) {
        Dotenv dotenv = DBUtils.dotenv;
        String host = dotenv.get("GMAIL_SMTP_HOST");
        final String username = dotenv.get("GMAIL_MAIL_ID");
        final String password = dotenv.get("GMAIL_PASSKEY");

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject("Bill - Online Shopping");

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Thank you for shopping with us! Your bill has been attached with this mail.");

            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(pdf, "application/pdf");
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName("cart_details.pdf");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentBodyPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email from Gmail sent successfully!");
            logger.info("Mail with Bill from Gmail is sent to the user successfully");

        } catch (MessagingException mex) {
            logger.error("Error sending mail from Gmail", mex.getMessage());
            mex.printStackTrace();
        }
    }
}
