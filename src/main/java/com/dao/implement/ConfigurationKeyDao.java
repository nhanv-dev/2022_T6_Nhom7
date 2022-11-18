package com.dao.implement;

import com.dao.IConfigurationDao;
import com.dao.IConfigurationKeyDao;
import com.mapper.ConfigurationKeyMapper;
import com.model.Configuration;
import com.model.ConfigurationKey;

import java.util.List;

public class ConfigurationKeyDao extends AbstractDao<ConfigurationKey> implements IConfigurationKeyDao {

    @Override
    public List<ConfigurationKey> findByConfigId(long configId) {
        return useProcedure(Configuration.getProperty("database.find_configuration_key"), Configuration.getProperty("database.controller"), new ConfigurationKeyMapper(), configId);
    }
}
