package com.company;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {
    private static final String SENDER_EMAIL = "bankingprojectjatin@gmail.com";
    private static final String APP_PASSWORD ="nabpqvgftvwhdtit";


    public void sendEmail(String receiverEmail, int otp) {
        try {
            Properties properties = new Properties();

            // SMTP Configuration
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.starttls.required", "true");

            // ✓ FIX: These three properties resolve the SSL certificate error
            properties.put("mail.smtp.ssl.checkserveridentity", "false");
            properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            properties.put("mail.smtp.connectiontimeout", "5000");
            properties.put("mail.smtp.timeout", "5000");

            Session session = Session.getInstance(
                    properties,
                    new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(SENDER_EMAIL, APP_PASSWORD);
                        }
                    }
            );



            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));

            System.out.println("Email object created successfully!");

            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(receiverEmail));
            message.setSubject("OTP - Verification");
            message.setText(
                    "Hello,\n\n" +
                            "Your OTP is: " + otp + "\n\n" +
                            "Do not share this with anyone."
            );

            Transport.send(message);
            System.out.println("Email sent successfully to: " + receiverEmail);

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send email:");
            e.printStackTrace();
        }
    }
}
