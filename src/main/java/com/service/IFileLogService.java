package com.service;

public interface IFileLogService {
    long insert(int configId, String path, String status, int authorId);

    boolean updateFileLog(int id, String status);

}
