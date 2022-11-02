package com.dao.implement;

import com.dao.ICommodityDao;
import com.model.Commodity;

import java.util.List;
import java.util.StringJoiner;

public class CommodityDao extends AbstractDao<Commodity> implements ICommodityDao {

    @Override
    public void loadToStaging(String path) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("load data infile ?");
        joiner.add("into table staging");
        joiner.add("FIELDS TERMINATED BY ','");
        joiner.add("(natural_key, commodity_name, price, percent, unit, created_date)");
        useProcedure(joiner.toString(), DatabaseConnector.STAGING, null, path);
    }

    @Override
    public void loadToDataWarehouse() {

    }

    @Override
    public void transformStaging() {

    }

    @Override
    public void truncateStaging() {
        useProcedure("truncate staging", DatabaseConnector.STAGING, null);
    }

}
