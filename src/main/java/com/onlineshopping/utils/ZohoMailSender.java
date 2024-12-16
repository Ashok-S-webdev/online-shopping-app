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

public class ZohoMailSender implements MailSender {
    private static final Logger logger = LoggerFactory.getLogger(ZohoMailSender.class);

    // Method to send mail via zoho mail
    public void sendMail(String userEmail, byte[] pdf) {
        Dotenv dotenv = DBUtils.dotenv;
        String host = dotenv.get("ZOHO_SMTP_HOST");
        final String username = dotenv.get("ZOHO_MAIL_ID");
        final String password = dotenv.get("ZOHO_PASSKEY");

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
            System.out.println("Email from Zoho mail sent successfully!");
            logger.info("Mail with Bill from Zoho Mail is sent to the user successfully");

        } catch (MessagingException mex) {
            logger.error("Error sending mail to the user", mex.getMessage());
            mex.printStackTrace();
        }
    }
    

}
