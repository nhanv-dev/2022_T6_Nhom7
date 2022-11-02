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
        } catch (SQLException exception) {
            return null;
        }
        return commodity;
    }
}
