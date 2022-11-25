package com.service;

import com.model.SourcePattern;

public interface IConfigurationService {
    SourcePattern findOne(long configId) ;
}
