package com.service;

import com.model.Commodity;

import java.util.List;

public interface ICommodityService {
    List<Commodity> findUnexpiredData();

    void loadToStaging(String path);

    void loadToDataWarehouse();

    void truncateStaging();

    void transformStaging();
}
