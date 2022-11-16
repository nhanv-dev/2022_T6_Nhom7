package com.service.implement;

import com.service.ISendMailError;
import java.io.UnsupportedEncodingException;
import java.util.Properties;


import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendErrorService implements ISendMailError {

    private static final String username = "nhom7datawarehouse@gmail.com";
    private static final String password = "flhnvygpzynuzntj";

    @Override
    public void sendError(String error, String... recipient) {
        for(String r : recipient){
            sendMail(r, "Error DataWarehouse",error);
        }
    }


    public void sendMail(String to, String subject, String content) {
        Session session = connect();
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "Error DataWarehouse"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            //  messmage.setText(content);
            message.setContent(content, "text/html; charset=UTF-8");
            Transport.send(message);

        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();

        }
    }

    private Session connect() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public static void main(String[] args) {
        SendErrorService a = new SendErrorService();
        a.sendError("sendError", "gianglqs07@gmail.com");
    }
}
