package com.dao.implement;

import com.dao.GenericDao;
import com.mapper.IRowMapper;
import com.util.LoggerUtil;
import com.util.ParameterSetter;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T> implements GenericDao<T> {
    private static final Logger logger = LoggerUtil.getInstance(AbstractDao.class);

    @Override
    public List<T> query(String sql, String database, IRowMapper<T> rowMapper, Object... parameters) {
        List<T> results = new ArrayList<T>();
        try {
            Connection connection = DatabaseConnector.getConnection(database);
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(sql);
            ParameterSetter.setParameters(statement, parameters);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next() && rowMapper != null)
                results.add(rowMapper.mapRow(resultSet));
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            logger.error(e);
        }
        return results;
    }

    @Override
    public List<T> useProcedure(String sql, String database, IRowMapper<T> rowMapper, Object... parameters) {
        List<T> results = new ArrayList<T>();
        try {
            Connection connection = DatabaseConnector.getConnection(database);
            assert connection != null;
            CallableStatement statement = connection.prepareCall(sql);
            ParameterSetter.setParameters(statement, parameters);
            if (rowMapper == null) {
                statement.executeUpdate();
            } else {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) results.add(rowMapper.mapRow(resultSet));
            }
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
        return results;
    }
    
}
