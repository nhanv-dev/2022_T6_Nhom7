package com.controller;


import com.dao.ICommodityDao;
import com.dao.implement.CommodityDao;
import com.model.Configuration;
import com.model.Constant;

import java.sql.SQLException;

public class TestCase {
    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        configuration.setSource("https://markets.businessinsider.com/commodities");
        configuration.addProperty("source", "https://markets.businessinsider.com/commodities");
        configuration.addProperty("method", "jsoup");
        configuration.addProperty("name", "businessinsider");
        configuration.addProperty("directory", "C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/data/");
        configuration.addProperty("number_of_column", "4");
        configuration.addProperty("selector_container", "table.table.table--col-1-font-color-black.table--suppresses-line-breaks:not(:first-child) > tbody");
        configuration.addProperty("selector_row", "table.table.table--col-1-font-color-black.table--suppresses-line-breaks:not(:first-child) > tbody > tr");
        configuration.addProperty("selector_col_1", "td:nth-child(1) a");
        configuration.addProperty("selector_col_2", "td:nth-child(2) span");
        configuration.addProperty("selector_col_3", "td:nth-child(3) span");
        configuration.addProperty("selector_col_4", "td:nth-child(5)");

        SourceProvider provider = new SourceProvider();
        System.out.println(provider.extract(configuration,""));
//        ICommodityDao commodityDao = new CommodityDao();
//        commodityDao.loadDataInFile("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/tradingeconomics-20221007110622.csv");

        System.out.println("Done...");
    }
}
