package com.service.implement;

import com.dao.IFileLogDao;
import com.dao.implement.FileLogDao;
import com.model.FileLog;
import com.service.IFileLogService;

import java.util.Date;

public class FileLogService implements IFileLogService {
    private final IFileLogDao fileLogDao = new FileLogDao();

    @Override
    public FileLog findOne(long id, Date date) {
        return fileLogDao.findOne(id, date);
    }

    @Override
    public long insert(FileLog fileLog) {
        return fileLogDao.insert(fileLog);
    }

    @Override
    public void updateStatus(long id, String status) {
        fileLogDao.updateStatus(id, status);
    }

}
