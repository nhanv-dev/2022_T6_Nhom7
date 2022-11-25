package com.dao;

import com.mapper.IRowMapper;

import java.util.List;

public interface GenericDao<T> {
    List<T> query(String sql, String database, IRowMapper<T> rowMapper, Object... parameters);

    List<T> useProcedure(String sql, String database, IRowMapper<T> rowMapper, Object... parameters);
}
