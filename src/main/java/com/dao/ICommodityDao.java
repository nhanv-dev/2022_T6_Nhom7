package com.dao;

import com.model.Commodity;

import java.util.List;

public interface ICommodityDao {

    void loadToStaging(String path);

    void loadToDataWarehouse();

    void transformStaging();

    void truncateStaging();
}
