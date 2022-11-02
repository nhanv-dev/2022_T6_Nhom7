package com.dao.implement;

import com.dao.IFileLogDao;
import com.model.Commodity;
import com.model.FileLog;

import java.util.StringJoiner;

public class FileLogDao extends AbstractDao<Commodity> implements IFileLogDao {

    @Override
    public long insert(FileLog fileLog) {
        String sql = "call insert_file_log(?,?,?,?,?,?)";
        return insert(sql, DatabaseConnector.CONTROLLER, 1, fileLog.getPath(), fileLog.getStatus(), fileLog.getAuthorId(), fileLog.getCreatedDate());
    }

    @Override
    public boolean updateFileLog(long id, String name) {
        return false;
    }
}
