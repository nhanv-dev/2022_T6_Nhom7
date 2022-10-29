package com.service;

public interface IFileLogService {
    int insert( int configId,String path, String status, int authorId);

    boolean updateFileLog(int id, String status);

}
