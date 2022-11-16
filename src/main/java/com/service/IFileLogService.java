package com.service;

import com.model.FileLog;

public interface IFileLogService {
    long insert(FileLog fileLog);

    void updateStatus(long id, String status);

}
