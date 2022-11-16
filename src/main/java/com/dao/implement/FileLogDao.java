package com.dao.implement;

import com.dao.IFileLogDao;
import com.model.Commodity;
import com.model.FileLog;

import java.util.StringJoiner;

public class FileLogDao extends AbstractDao<FileLog> implements IFileLogDao {

    @Override
    public long insert(FileLog fileLog) {
        String sql = "call insert_file_log(?,?,?,?,?,?)";
        return insert(sql, DatabaseConnector.CONTROLLER, fileLog.getConfigId(), fileLog.getPath(), fileLog.getStatus(), fileLog.getAuthorId(), fileLog.getCreatedDate());
    }

    @Override
    public void updateStatus(long id, String status) {
        String sql = "update file_log set file_status = ? where file_id = ?";
        useProcedure(sql, DatabaseConnector.CONTROLLER, null, status, id);
    }
}
