package com.dao;

import com.model.Configuration;

public interface IConfigurationDao {
    Configuration findOne(int configId);

}
