package com.dao;

public interface IFileLogDao {
    int insert(int configId, String path, String status, int authorId);

    boolean updateFileLog(int id,String name);
}
