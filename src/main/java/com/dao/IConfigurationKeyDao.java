package com.dao;

import com.model.ConfigurationKey;

import java.util.List;

public interface IConfigurationKeyDao {
    List<ConfigurationKey> findByConfigId(long configId);

}
