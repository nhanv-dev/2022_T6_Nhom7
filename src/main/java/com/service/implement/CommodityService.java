package com.service.implement;

import com.dao.ICommodityDao;
import com.dao.implement.CommodityDao;
import com.service.ICommodityService;

public class CommodityService implements ICommodityService {
    private final ICommodityDao commodityDao = new CommodityDao();

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
