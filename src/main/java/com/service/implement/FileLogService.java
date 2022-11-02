package com.service.implement;

import com.dao.IFileLogDao;
import com.dao.implement.FileLogDao;
import com.model.FileLog;
import com.service.IFileLogService;

public class FileLogService implements IFileLogService {
    private final IFileLogDao fileLogDao = new FileLogDao();

    @Override
    public long insert(FileLog fileLog) {
        return fileLogDao.insert(fileLog);
    }

    @Override
    public boolean updateFileLog(long id, String status) {
        return fileLogDao.updateFileLog(id, status);
    }
}
