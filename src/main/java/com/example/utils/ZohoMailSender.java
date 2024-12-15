package com.example.utils;

import java.util.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;


import io.github.cdimascio.dotenv.Dotenv;

public class ZohoMailSender implements MailSender {

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

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    

}
