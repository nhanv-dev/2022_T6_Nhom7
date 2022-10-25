package com.controller;


import com.dao.ICommodityDao;
import com.dao.implement.CommodityDao;
import com.model.Configuration;
import com.model.Constant;

import java.sql.SQLException;

public class TestCase {
    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        configuration.setSource("https://tradingeconomics.com/commodities");
        configuration.addProperty("source", "https://tradingeconomics.com/commodities");
        configuration.addProperty("method", "jsoup");
        configuration.addProperty("name", "tradingeconomics");
        configuration.addProperty("directory", "C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/data/");
        configuration.addProperty("number_of_column", "6");
        configuration.addProperty("selector_container", ".table.table-hover.table-striped.table-heatmap > tbody");
        configuration.addProperty("selector_row", ".table.table-hover.table-striped.table-heatmap > tbody > tr");
        configuration.addProperty("selector_col_1", "td:nth-child(1) > a > b");
        configuration.addProperty("selector_col_2", "td:nth-child(2)");
        configuration.addProperty("selector_col_3", "td:nth-child(4)");
        configuration.addProperty("selector_col_4", "td:nth-child(5)");
        configuration.addProperty("selector_col_5", "td:nth-child(6)");
        configuration.addProperty("selector_col_6", "td:nth-child(7)");

//        SourceProvider provider = new SourceProvider();
//        System.out.println(provider.extract(configuration));
        ICommodityDao commodityDao = new CommodityDao();
        commodityDao.loadDataInFile("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/tradingeconomics-20221007110622.csv");

        System.out.println("Done...");
    }
}
