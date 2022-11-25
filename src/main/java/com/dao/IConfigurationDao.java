package com.dao;

import com.model.SourcePattern;

public interface IConfigurationDao {
    SourcePattern findOne(long configId);

}
