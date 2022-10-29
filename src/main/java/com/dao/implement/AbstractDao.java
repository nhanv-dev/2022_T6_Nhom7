package com.dao.implement;

import com.dao.GenericDao;
import com.dao.implement.DatabaseConnector;
import com.mapper.IRowMapper;
import com.util.ParameterSetter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T> implements GenericDao<T> {

    @Override
    public List<T> query(String sql, String database, IRowMapper<T> rowMapper, Object... parameters) {
        List<T> results = new ArrayList<T>();
        try {
            Connection connection = new DatabaseConnector().getConnection(database);
            PreparedStatement statement = connection.prepareStatement(sql);
            ParameterSetter.setParameters(statement, parameters);
            ResultSet resultSet = statement.executeQuery();
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
    public List<T> useProcedure(String sql, String database, IRowMapper<T> rowMapper, Object... parameters) {
        List<T> results = new ArrayList<T>();
        try {
            Connection connection = new DatabaseConnector().getConnection(database);
            CallableStatement statement = connection.prepareCall(sql);
            ParameterSetter.setParameters(statement, parameters);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next() && rowMapper != null)
                results.add(rowMapper.mapRow(resultSet));
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    public void useProcedure(String sql, String database, Object... parameters) {
        Connection connection = null;
        try {
            connection = new DatabaseConnector().getConnection(database);
            CallableStatement statement = connection.prepareCall(sql);
            connection.setAutoCommit(false);
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
}
