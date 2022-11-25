package com.mapper;

import com.model.FileLog;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FileLogMapper implements IRowMapper<FileLog> {
    @Override
    public FileLog mapRow(ResultSet rs) {
        try {
            FileLog fileLog = new FileLog();
            fileLog.setId(rs.getLong("file_id"));
            fileLog.setConfigId(rs.getLong("config_id"));
            fileLog.setPath(rs.getString("file_path"));
            fileLog.setStatus(rs.getString("file_status"));
            fileLog.setAuthorId(rs.getLong("author"));
            fileLog.setCreatedDate(rs.getDate("created_date"));
            return fileLog;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
