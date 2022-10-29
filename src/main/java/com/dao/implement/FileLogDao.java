package com.dao.implement;

import com.dao.IFileLogDao;
import com.model.Commodity;

import java.util.StringJoiner;

public class FileLogDao extends AbstractDao<Commodity> implements IFileLogDao {

    @Override
    public int insert(int configId, String path, String status, int authorId) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("insert into file_log ()");
        joiner.add("values (?,?,?,?)");
        return 0;
    }

    @Override
    public boolean updateFileLog(int id, String name) {
        return false;
    }
}
