package com.service.implement;

import com.dao.ICommodityDao;
import com.dao.implement.CommodityDao;
import com.model.Commodity;
import com.service.ICommodityService;

import java.util.List;

public class CommodityService implements ICommodityService {
    private final ICommodityDao commodityDao = new CommodityDao();

    @Override
    public List<Commodity> findUnexpiredData() {
        return commodityDao.findUnexpiredData();
    }

    @Override
    public List<Commodity> findByNaturalKey(String naturalKey) {
        return commodityDao.findByNaturalKey(naturalKey);
    }

    @Override
    public void loadToStaging(String path) {
        commodityDao.loadToStaging(path);
    }

    @Override
    public void loadToDataWarehouse() {
        commodityDao.loadToDataWarehouse();
    }

    @Override
    public void truncateStaging() {
        commodityDao.truncateStaging();
    }

    @Override
    public void transformStaging() {
        commodityDao.transformStaging();
    }
}
