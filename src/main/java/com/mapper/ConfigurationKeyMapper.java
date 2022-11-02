package com.mapper;

import com.model.ConfigurationKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfigurationKeyMapper implements IRowMapper<ConfigurationKey> {
    private static final Logger logger = LogManager.getLogger(ConfigurationKeyMapper.class);

    @Override
    public ConfigurationKey mapRow(ResultSet rs) {
        try {
            ConfigurationKey key = new ConfigurationKey();
            key.setKey(rs.getString("config_name"));
            key.setValue(rs.getString("config_value"));
            return key;
        } catch (SQLException e) {
            logger.error(e);
            return null;
        }
    }
}
