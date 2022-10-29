package com.service.implement;

import com.dao.IConfigurationDao;
import com.dao.implement.ConfigurationDao;
import com.model.Configuration;
import com.service.IConfigurationService;

public class ConfigurationService implements IConfigurationService {
    private final IConfigurationDao configurationDao = new ConfigurationDao();

    @Override
    public Configuration findOne(int configId) {
        return configurationDao.findOne(configId);
    }
}
