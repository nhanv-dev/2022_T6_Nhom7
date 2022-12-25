package com.service.implement;

import com.service.IAuthorService;
import com.service.ISendMailError;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;


import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendErrorService implements ISendMailError {

    private static final String username = "nhom7datawarehouse@gmail.com";
    private static final String password = "flhnvygpzynuzntj";

    @Override
    public void sendError(String error, String filePath, String... recipient) {
        for (String r : recipient) {
            // sendMail(r, "Error DataWarehouse", error);
            sendMail(r, "Error DataWarehouse", error, filePath);
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

    public void sendMail(String to, String subject, String text, String pathFile) {
        Session session = connect();
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "Error DataWarehouse"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            // Phan 1 gom doan tin nhan
            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText(text);

            // phan 2 chua tap tin txt
            MimeBodyPart messageBodyPart2 = new MimeBodyPart();

            DataSource source1 = new FileDataSource(pathFile);
            messageBodyPart2.setDataHandler(new DataHandler(source1));
            messageBodyPart2.setFileName(pathFile);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart1);
            multipart.addBodyPart(messageBodyPart2);
            message.setContent(multipart);
            Transport.send(message);

        } catch (Exception  e) {
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

        List<String> listEmail = new AuthorService().listEmailAuthor();
        System.out.println();

        a.sendError("Test","C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/data/tradingeconomics-20221129.csv",  listEmail.toArray(new String[0]));

    }
}