package com.service.implement;

import com.dao.IFileLogDao;
import com.dao.implement.FileLogDao;
import com.service.IFileLogService;

public class FileLogService implements IFileLogService {
    private final IFileLogDao fileLogDao = new FileLogDao();

    @Override
    public int insert(int configId, String path, String status, int authorId) {
        return fileLogDao.insert(configId,path, status,  authorId);
    }

    @Override
    public boolean updateFileLog(int id, String status) {
        return fileLogDao.updateFileLog(id, status);
    }
}
