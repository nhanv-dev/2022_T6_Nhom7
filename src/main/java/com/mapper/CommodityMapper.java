package com.mapper;

import com.model.Commodity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommodityMapper implements IRowMapper<Commodity> {
    private static final Logger logger = LogManager.getLogger(SourcePatternMapper.class);

    @Override
    public Commodity mapRow(ResultSet rs) {
        try {
            Commodity commodity = new Commodity();
            commodity.setName(rs.getString("commodity_name"));
            commodity.setPrice(rs.getBigDecimal("price").longValue());
            commodity.setPercent(rs.getBigDecimal("percent").longValue());
            commodity.setCategory(rs.getString("category"));
            commodity.setUnit(rs.getString("unit"));
            commodity.setExpiredDate(rs.getDate("expired_date"));
            commodity.setCreatedDate(rs.getDate("created_date"));
            return commodity;
        } catch (SQLException e) {
            logger.error(e);
            return null;
        }
    }
}
