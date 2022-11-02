package com.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IRowMapper<T> {
    T mapRow(ResultSet rs);
}
