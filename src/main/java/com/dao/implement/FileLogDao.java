package com.dao.implement;

import com.dao.IFileLogDao;
import com.mapper.FileLogMapper;
import com.model.Commodity;
import com.model.Configuration;
import com.model.FileLog;
import com.util.LoggerUtil;
import com.util.ParameterSetter;
import org.apache.logging.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

public class FileLogDao extends AbstractDao<FileLog> implements IFileLogDao {
    private static final Logger logger = LoggerUtil.getInstance(FileLogDao.class);

    @Override
    public FileLog findOne(long id, Date date) {
        List<FileLog> list = useProcedure(Configuration.getProperty("database.find_log_by_day_and_id"), Configuration.getProperty("database.controller"), new FileLogMapper(), date, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public long insert(FileLog fileLog) {
        try {
            Connection connection = DatabaseConnector.getConnection(Configuration.getProperty("database.controller"));
            if (connection == null) throw new Exception("Connection is null");
            CallableStatement statement = connection.prepareCall(Configuration.getProperty("database.insert_file_log"));
            ParameterSetter.setParameters(statement, fileLog.getConfigId(), fileLog.getPath(), fileLog.getStatus(), fileLog.getAuthorId(), fileLog.getCreatedDate());
            statement.registerOutParameter("id", java.sql.Types.INTEGER);
            statement.executeUpdate();
            long id = statement.getInt("id");
            statement.close();
            return id;
        } catch (Exception e) {
            logger.error(e);
            return -1;
        }
    }

    @Override
    public void updateStatus(long id, String status) {
        useProcedure(Configuration.getProperty("database.update_status_file_log"), Configuration.getProperty("database.controller"), null, status, id);
    }
}
