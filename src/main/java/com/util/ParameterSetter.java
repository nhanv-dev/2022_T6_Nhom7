package com.util;

import java.math.BigDecimal;
import java.sql.*;

public class ParameterSetter {
    public static void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            Object param = parameters[i];
            if (param instanceof String)
                statement.setString(i + 1, (String) param);
            else if (param instanceof Integer)
                statement.setInt(i + 1, (Integer) param);
            else if (param instanceof Double)
                statement.setDouble(i + 1, (Double) param);
            else if (param instanceof Float)
                statement.setFloat(i + 1, (Float) param);
            else if (param instanceof Long)
                statement.setLong(i + 1, (Long) param);
            else if (param instanceof Timestamp)
                statement.setTimestamp(i + 1, (Timestamp) param);
            else if (param instanceof Date)
                statement.setDate(i + 1, new Date(((Date) param).getTime()));
            else if (param instanceof java.util.Date)
                statement.setDate(i + 1, new Date(((java.util.Date) param).getTime()));
            else if (param instanceof BigDecimal)
                statement.setBigDecimal(i + 1, (BigDecimal) param);
            else
                statement.setNull(i + 1, Types.NULL);
        }
    }
}
