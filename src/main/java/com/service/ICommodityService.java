package com.service;

public interface ICommodityService {
    void loadToStaging(String path);
    void truncateStaging();
}
