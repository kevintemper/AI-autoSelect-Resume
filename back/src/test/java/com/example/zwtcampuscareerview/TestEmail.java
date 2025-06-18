package com.example.zwtcampuscareerview;

import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class TestEmail {
    public static void main(String[] args) {
        String host = "smtp.qq.com";
        String port = "465";
        String username = "3422965463@qq.com";
        String password = "ypajcwmxxodadcbb"; // 替换为你的授权码

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");

        Session session = Session.getInstance(props);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("yourtestemail@example.com"));
            message.setSubject("Test Email");
            message.setText("This is a test email from QQ SMTP.");

            Transport transport = session.getTransport("smtp");
            transport.connect(username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            System.out.println("Email sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

