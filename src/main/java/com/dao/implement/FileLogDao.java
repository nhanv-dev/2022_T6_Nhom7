package com.dao.implement;

import com.dao.IFileLogDao;
import com.model.Commodity;

import java.util.StringJoiner;

public class FileLogDao extends AbstractDao<Commodity> implements IFileLogDao {

    @Override
    public long insert(int configId, String path, String status, int authorId) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("call insert_file_log(?,?,?,?,?)");
        return insert(joiner.toString(), DatabaseConnector.CONTROLLER, configId, path, status, authorId);
    }

    @Override
    public boolean updateFileLog(int id, String name) {
        return false;
    }
}
