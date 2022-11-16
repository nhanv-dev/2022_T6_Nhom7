package com.controller;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.util.FormatterUtil;
import com.util.LoggerUtil;
import org.apache.commons.net.ftp.*;

public class FTPConnector {
    private static final String FTP_SERVER_ADDRESS = "103.97.126.21";
    private static final int FTP_TIMEOUT = 60000;
    private static final int FTP_SERVER_PORT_NUMBER = 21;
    private static final String FTP_USERNAME = "ftp-user@group7datawarehouse.tk";
    private static final String FTP_PASSWORD = "ftp-user";
    private FTPClient ftpClient;



    public static void main(String[] args) {
        FTPConnector connector = new FTPConnector();
        connector.connectFTPServer();
        File directory = new File("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\data\\");
        for (File file : directory.listFiles()) {
            String path = FormatterUtil.formatDirectoryPathFTP(file.getName());
            connector.uploadFile(file.getPath(), path, file.getName());
        }
    }

    private void connectFTPServer()   {
        ftpClient = new FTPClient();
        try {
            System.out.println("Connecting ftp server...");
            // connect to ftp server
            ftpClient.setDefaultTimeout(FTP_TIMEOUT);
            ftpClient.connect(FTP_SERVER_ADDRESS, FTP_SERVER_PORT_NUMBER);
            ftpClient.login(FTP_USERNAME, FTP_PASSWORD);
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

    public boolean containDirectory(String directory) throws IOException {
        ftpClient.changeWorkingDirectory(directory);
        int returnCode = ftpClient.getReplyCode();
        return returnCode != 550;
    }

    public void uploadFile(String path, String directory, String remote) {
        try {
            File localFile = new File(path);
            if (!localFile.exists()) throw new Exception("File not exists");
            InputStream inputStream = Files.newInputStream(localFile.toPath());
            boolean createdFile = false;
            boolean createdDir = containDirectory(directory);
            if (createdDir) {
                createdFile = ftpClient.storeFile(remote, inputStream);
                System.out.println(ftpClient.getReplyString());
            } else {
                createdDir = ftpClient.makeDirectory(directory);
                if (!createdDir) throw new Exception("Create directory in ftp server is fail");
                ftpClient.changeWorkingDirectory(directory);
                createdFile = ftpClient.storeFile(remote, inputStream);
                System.out.println(ftpClient.getReplyString());
            }
            inputStream.close();
            if (!createdFile)
                throw new Exception("Create file " + directory + "/" + remote + " in ftp server is fail");
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.getInstance(FTPConnector.class).error(e);
        }

    }

    private boolean createFile(String path, String remote) {
        try {
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    private List<FTPFile> getListFileFromFTPServer(String path, final String ext) {
        List<FTPFile> listFiles = new ArrayList<>();
        try {
            FTPFile[] ftpFiles = ftpClient.listFiles(path, new FTPFileFilter() {
                public boolean accept(FTPFile file) {
                    return file.getName().endsWith(ext);
                }
            });
            if (ftpFiles.length > 0) {
                for (FTPFile ftpFile : ftpFiles) {
                    // add file to listFiles
                    if (ftpFile.isFile()) {
                        listFiles.add(ftpFile);
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return listFiles;
    }

    public void downloadFile() {

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
