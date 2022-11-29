package com.dao.implement;

import com.dao.ICommodityDao;
import com.mapper.CommodityMapper;
import com.model.Commodity;
import com.model.Configuration;

import java.util.List;
import java.util.StringJoiner;

public class CommodityDao extends AbstractDao<Commodity> implements ICommodityDao {

    @Override
    public List<Commodity> findUnexpiredData() {
        return query(Configuration.getProperty("database.find_unexpired_data"), Configuration.getProperty("database.data_warehouse"), new CommodityMapper());
    }

    @Override
    public List<Commodity> findByNaturalKey(String naturalKey) {
        return query(Configuration.getProperty("database.find_by_natural_key"), Configuration.getProperty("database.data_warehouse"), new CommodityMapper(), naturalKey);
    }

    @Override
    public void loadToStaging(String path) {
        useProcedure(Configuration.getProperty("database.insert_into_staging"), Configuration.getProperty("database.staging"), null, path);
    }

    @Override
    public void loadToDataWarehouse() {
        useProcedure(Configuration.getProperty("database.insert_into_data_warehouse"), Configuration.getProperty("database.data_warehouse"), null);
    }

    @Override
    public void transformStaging() {
        useProcedure(Configuration.getProperty("database.delete_rows_null_staging"), Configuration.getProperty("database.staging"), null);
        useProcedure(Configuration.getProperty("database.insert_into_staging_transformed"), Configuration.getProperty("database.staging"), null);
    }

    @Override
    public void truncateStaging() {
        useProcedure(Configuration.getProperty("database.truncate_staging"), Configuration.getProperty("database.staging"), null);
        useProcedure(Configuration.getProperty("database.truncate_staging_transformed"), Configuration.getProperty("database.staging"), null);
    }

}
