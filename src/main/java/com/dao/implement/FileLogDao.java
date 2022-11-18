package com.dao.implement;

import com.dao.IFileLogDao;
import com.model.Commodity;
import com.model.Configuration;
import com.model.FileLog;

import java.util.StringJoiner;

public class FileLogDao extends AbstractDao<FileLog> implements IFileLogDao {

    @Override
    public long insert(FileLog fileLog) {
        return insert(Configuration.getProperty("database.insert_file_log"), Configuration.getProperty("database.controller"), fileLog.getConfigId(), fileLog.getPath(), fileLog.getStatus(), fileLog.getAuthorId(), fileLog.getCreatedDate());
    }

    @Override
    public void updateStatus(long id, String status) {
        useProcedure(Configuration.getProperty("database.update_status_file_log"), Configuration.getProperty("database.controller"), null, status, id);
    }
}
