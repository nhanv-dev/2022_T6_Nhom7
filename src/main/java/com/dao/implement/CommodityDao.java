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
    public void loadToStaging(String path) {
        useProcedure(Configuration.getProperty("database.load_to_staging"), Configuration.getProperty("database.staging"), null, path);
    }

    @Override
    public void loadToDataWarehouse() {
        useProcedure(Configuration.getProperty("database.load_to_data_warehouse"), Configuration.getProperty("database.data_warehouse"), null);
    }

    @Override
    public void transformStaging() {
        deleteNullColumnRows();
        updateToNewValue();
        insertIntoStagingTransform();
    }

    private void deleteNullColumnRows() {
        String deleteRowsNullColumnSql = "call delete_rows_null_column()";
        useProcedure(deleteRowsNullColumnSql, DatabaseConnector.STAGING, null);
    }

    private void updateToNewValue() {
        String updateNewValue = "call update_to_new_value('Agricultural', 'Agriculture')";
        useProcedure(updateNewValue, DatabaseConnector.STAGING, null);
    }

    private void insertIntoStagingTransform() {
        String insertToStagingTransform = "call insert_into_staging_transformed()";
        useProcedure(insertToStagingTransform, DatabaseConnector.STAGING, null);
    }

    @Override
    public void truncateStaging() {
        useProcedure("truncate staging", DatabaseConnector.STAGING, null);
        useProcedure("truncate staging_transformed", DatabaseConnector.STAGING, null);

    }

}
