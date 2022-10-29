package com.mapper;

import com.model.Commodity;
import com.model.Configuration;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfigurationMapper implements IRowMapper<Configuration> {

    @Override
    public Configuration mapRow(ResultSet rs) {
        Configuration configuration = new Configuration();
        try {
            configuration.setSource(rs.getString("source"));
            configuration.addProperty(rs.getString("config_name"), rs.getString("config_value"));
        } catch (SQLException exception) {
            return null;
        }
        return configuration;
    }
}
