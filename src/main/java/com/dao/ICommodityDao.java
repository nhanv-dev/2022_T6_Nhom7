package com.dao;

import com.model.Commodity;

import java.util.List;

public interface ICommodityDao {
    void insertAll(List<Commodity> commodities);

    void loadDataInFile(String path);
}
