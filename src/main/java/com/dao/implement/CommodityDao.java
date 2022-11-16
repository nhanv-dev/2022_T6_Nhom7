package com.dao.implement;

import com.dao.ICommodityDao;
import com.mapper.CommodityMapper;
import com.model.Commodity;

import java.util.List;
import java.util.StringJoiner;

public class CommodityDao extends AbstractDao<Commodity> implements ICommodityDao {

    @Override
    public List<Commodity> findUnexpiredData() {
        String sql = "call find_unexpired_data()";
        return query(sql, DatabaseConnector.DATA_WAREHOUSE, new CommodityMapper());
    }

    @Override
    public void loadToStaging(String path) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("load data infile ?");
        joiner.add("into table staging");
        joiner.add("FIELDS TERMINATED BY ','");
        joiner.add("(natural_key, commodity_name, price, percent, category, unit, created_date)");
        useProcedure(joiner.toString(), DatabaseConnector.STAGING, null, path);
    }

    @Override
    public void loadToDataWarehouse() {
        String sql = "call insert_data()";
        useProcedure(sql, DatabaseConnector.DATA_WAREHOUSE, null);
    }

    @Override
    public void transformStaging() {

    }

    @Override
    public void truncateStaging() {
        useProcedure("truncate staging", DatabaseConnector.STAGING, null);
    }

}
