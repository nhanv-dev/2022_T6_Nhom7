package com.dao;

public interface IFileLogDao {
    long insert(int configId, String path, String status, int authorId);

    boolean updateFileLog(int id,String name);
}
