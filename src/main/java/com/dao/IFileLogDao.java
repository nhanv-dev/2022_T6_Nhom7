package com.dao;

import com.model.FileLog;

public interface IFileLogDao {
    long insert(FileLog fileLog);

    void updateStatus(long id, String status);
}
