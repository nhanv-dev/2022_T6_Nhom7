package com.dao.implement;

import com.dao.GenericDao;
import com.mapper.IRowMapper;
import com.util.ParameterSetter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T> implements GenericDao<T> {

    @Override
    public List<T> query(String sql, IRowMapper<T> rowMapper, Object... parameters) {
        List<T> results = new ArrayList<T>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            Connection connection = StagingConnector.getConnection();
            statement = connection.prepareStatement(sql);
            ParameterSetter.setParameters(statement, parameters);
            resultSet = statement.executeQuery();
            while (resultSet.next() && rowMapper != null)
                results.add(rowMapper.mapRow(resultSet));
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }


    @Override
    public void useProcedure(String sql, Object... parameters) {
        Connection connection = null;
        CallableStatement statement = null;
        try {
            connection = StagingConnector.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareCall(sql);
            ParameterSetter.setParameters(statement, parameters);
            statement.executeUpdate();
            connection.commit();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void load(String sql, Object... parameters) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = StagingConnector.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            ParameterSetter.setParameters(statement, parameters);
            statement.executeUpdate();
            connection.commit();
            if (statement != null) statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }
}
