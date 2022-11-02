package com.mapper;

import com.model.SourcePattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SourcePatternMapper implements IRowMapper<SourcePattern> {
    private static final Logger logger = LogManager.getLogger(SourcePatternMapper.class);

    @Override
    public SourcePattern mapRow(ResultSet rs) {
        try {
            SourcePattern configuration = new SourcePattern();
            configuration.setId(rs.getLong("config_id"));
            configuration.setSource(rs.getString("source"));
            return configuration;
        } catch (SQLException e) {
            logger.error(e);
            return null;
        }
    }
}
