package com.dao.implement;

import com.dao.ICommodityDao;
import com.model.Commodity;

import java.util.List;
import java.util.StringJoiner;

public class CommodityDao extends AbstractDao<Commodity> implements ICommodityDao {


    @Override
    public void insertAll(List<Commodity> commodities) {

    }

    @Override
    public void loadToStaging(String path) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("load data infile ?");
        joiner.add("into table staging");
        joiner.add("FIELDS TERMINATED BY ','");
        joiner.add("(natural_key, commodity_name, price, percent, created_date)");
        useProcedure(joiner.toString(), path);
    }

    @Override
    public void transformStaging() {

    }
}
