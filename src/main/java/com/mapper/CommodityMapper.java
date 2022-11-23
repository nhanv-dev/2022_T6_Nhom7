package com.mapper;

import com.model.Commodity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class CommodityMapper implements IRowMapper<Commodity> {
    private static final Logger logger = LogManager.getLogger(CommodityMapper.class);

    @Override
    public Commodity mapRow(ResultSet rs) {
        try {
            Commodity commodity = new Commodity();
            commodity.setNaturalKey(rs.getString("natural_key"));
            commodity.setName(rs.getString("commodity_name"));
            commodity.setPrice(rs.getBigDecimal("price").floatValue());
            commodity.setPercent(rs.getBigDecimal("percent").floatValue());
            commodity.setCategory(rs.getString("cate_name"));
            commodity.setUnit(rs.getString("unit_key"));
            commodity.setExpiredDate(rs.getDate("expired_date"));
            commodity.setCreatedDate(rs.getDate("created_date"));
            return commodity;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        }
    }
}
