package com.service;

import com.model.Configuration;

public interface IConfigurationService {
    Configuration findOne(int configId);
}
