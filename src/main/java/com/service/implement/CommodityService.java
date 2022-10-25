package com.service.implement;

import com.dao.ICommodityDao;
import com.dao.implement.CommodityDao;
import com.service.ICommodityService;

public class CommodityService implements ICommodityService {
    private final ICommodityDao commodityDao = new CommodityDao();

    @Override
    public void insertAll() {
        commodityDao.insertAll(null);
    }
}
