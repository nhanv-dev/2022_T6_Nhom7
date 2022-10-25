package com.mapper;

import com.model.Commodity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommodityMapper implements IRowMapper<Commodity> {

    @Override
    public Commodity mapRow(ResultSet rs) {
        Commodity commodity = new Commodity();
        try {
            commodity.setName(rs.getString("name"));
            commodity.setPrice(rs.getBigDecimal("price").doubleValue());
            commodity.setDaily(rs.getBigDecimal("daily").doubleValue());
            commodity.setWeekly(rs.getBigDecimal("weekly").doubleValue());
            commodity.setMonthly(rs.getBigDecimal("monthly").doubleValue());
            commodity.setYearly(rs.getBigDecimal("yearly").doubleValue());
        } catch (SQLException exception) {
            return null;
        }
        return commodity;
    }
}
