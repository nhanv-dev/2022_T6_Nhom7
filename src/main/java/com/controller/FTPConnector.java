package com.controller;

import java.io.*;
import java.nio.file.Files;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPConnector {
    private static final String FTP_SERVER_ADDRESS = "192.168.1.123";
    private static final int FTP_TIMEOUT = 60000;
    private static final int FTP_SERVER_PORT_NUMBER = 21;
    private FTPClient ftpClient;

    public static void main(String[] args) {
        FTPConnector connector = new FTPConnector();
        connector.connectFTPServer();
        connector.uploadFile("D:/test.txt", "ad.txt");
    }


    private void connectFTPServer() {
        ftpClient = new FTPClient();
        try {
            System.out.println("Connecting ftp server...");
            // connect to ftp server
            ftpClient.setDefaultTimeout(FTP_TIMEOUT);
            ftpClient.connect(FTP_SERVER_ADDRESS, FTP_SERVER_PORT_NUMBER);
            ftpClient.login("ftp-user", "ftp-user");
            System.out.println(ftpClient.getReplyString());
            // run the passive mode command
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // check reply code
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                disconnectFTPServer();
                throw new IOException("FTP server not respond!");
            } else {
                System.out.println("Connected " + FTP_SERVER_ADDRESS);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void uploadFile(String path, String remote) {
        try {
            File localFile = new File(path);
            if (!localFile.exists()) System.out.println("File not exists");
            InputStream inputStream = Files.newInputStream(localFile.toPath());
            System.out.println("Start uploading first file");
            boolean done = ftpClient.storeFile(remote, inputStream);
            System.out.println(ftpClient.getReplyString());
            inputStream.close();
            if (done) System.out.println("The first file is uploaded successfully.");
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void disconnectFTPServer() {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
