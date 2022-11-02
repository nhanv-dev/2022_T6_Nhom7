package com.dao;

import com.model.FileLog;

public interface IFileLogDao {
    long insert(FileLog fileLog);

    boolean updateFileLog(long id, String name);
}
