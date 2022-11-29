package com.dao;

import com.model.Commodity;

import java.util.List;

public interface ICommodityDao {
    List<Commodity> findUnexpiredData();

    List<Commodity> findByNaturalKey(String naturalKey);

    void loadToStaging(String path);

    void loadToDataWarehouse();

    void transformStaging();

    void truncateStaging();
}
