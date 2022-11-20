package com.controller;

import java.io.*;
import java.nio.file.Files;

import com.model.Configuration;
import com.util.DateFormatter;
import com.util.LoggerUtil;
import org.apache.commons.net.ftp.*;
import org.apache.logging.log4j.Logger;

public class FTPConnector {
    private static FTPClient ftpClient;
    private final Logger logger = LoggerUtil.getInstance(FTPConnector.class);

    public void connect() {
        ftpClient = new FTPClient();
        try {
            String timeout = Configuration.getProperty("ftp.timeout");
            String port = Configuration.getProperty("ftp.server_port_number");
            // connect to ftp server
            if (timeout != null)
                ftpClient.setDefaultTimeout(Integer.parseInt(timeout));
            if (port != null)
                ftpClient.connect(Configuration.getProperty("ftp.server_address"), Integer.parseInt(port));
            ftpClient.login(Configuration.getProperty("ftp.username"), Configuration.getProperty("ftp.password"));
            // run the passive mode command
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // check reply code
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                disconnectFTPServer();
                throw new IOException("FTP server not respond!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
    }

    public boolean containFile(String remotePath) {
        try {
            FTPFile remoteFile = ftpClient.mlistFile(remotePath);
            if (remoteFile != null) logger.info("File " + remoteFile.getName() + " exists in ftp server");
            else logger.info("File " + remotePath + " does not exists in ftp server");
            return remoteFile != null;
        } catch (Exception e) {
            logger.error(e);
            return false;
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
            boolean hasDirectory = containDirectory(directory);
            if (!hasDirectory) {
                boolean createdDir = ftpClient.makeDirectory(directory);
                if (!createdDir) throw new Exception("Create directory in ftp server is failed");
                ftpClient.changeWorkingDirectory(directory);
            }
            boolean createdFile = ftpClient.storeFile(remote, inputStream);
            inputStream.close();
            if (!createdFile) throw new Exception("Create file " + directory + "/" + remote + " in ftp server is fail");
            else logger.info("Upload file into ftp server successfully");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }

    }

    public void downloadFile(String remoteFile, String localFile) throws Exception {
        try {
            File downloadFile = new File(localFile);
            OutputStream output = new BufferedOutputStream(Files.newOutputStream(downloadFile.toPath()));
            boolean success = ftpClient.retrieveFile(remoteFile, output);
            if (success) logger.info("Download file " + localFile + " successfully");
            else logger.info("Download file " + localFile + " failed");
            output.close();
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    public void disconnectFTPServer() {
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
