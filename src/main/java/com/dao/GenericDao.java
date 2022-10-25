package com.dao;

import com.mapper.IRowMapper;

import java.util.List;

public interface GenericDao<T> {
    List<T> query(String sql, IRowMapper<T> rowMapper, Object... parameters);

    void useProcedure(String sql, Object... parameters);

    void load(String sql, Object... parameters);

}
