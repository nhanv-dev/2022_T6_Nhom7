package com.dao.implement;

import com.dao.IConfigurationDao;
import com.mapper.ConfigurationKeyMapper;
import com.mapper.SourcePatternMapper;
import com.model.Configuration;
import com.model.ConfigurationKey;
import com.model.SourcePattern;

import java.util.List;

public class ConfigurationDao extends AbstractDao<SourcePattern> implements IConfigurationDao {
    private final ConfigurationKeyDao configurationKeyDao = new ConfigurationKeyDao();

    @Override
    public SourcePattern findOne(long configId) {
        List<SourcePattern> configuration = useProcedure(Configuration.getProperty("database.find_file_configuration"), Configuration.getProperty("database.controller"), new SourcePatternMapper(), configId);
        if (configuration.isEmpty() || configuration.get(0) == null) return null;
        SourcePattern sourcePattern = configuration.get(0);
        List<ConfigurationKey> keys = configurationKeyDao.findByConfigId(sourcePattern.getId());
        for (ConfigurationKey key : keys) sourcePattern.put(key.getKey(), key.getValue());
        return sourcePattern.getProperties().entrySet().isEmpty() ? null : sourcePattern;
    }


}
