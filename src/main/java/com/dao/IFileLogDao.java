package com.dao;

import com.model.FileLog;

import java.util.Date;

public interface IFileLogDao {
    FileLog findOne(long id, Date date);

    long insert(FileLog fileLog);

    void updateStatus(long id, String status);
}
