package com.service;

import com.model.FileLog;

public interface IFileLogService {
    long insert(FileLog fileLog);

    boolean updateFileLog(long id, String status);

}
