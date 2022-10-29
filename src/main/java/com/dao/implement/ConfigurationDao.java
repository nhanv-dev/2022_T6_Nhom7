package com.dao.implement;

import com.dao.IConfigurationDao;
import com.mapper.ConfigurationMapper;
import com.model.Configuration;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class ConfigurationDao extends AbstractDao<Configuration> implements IConfigurationDao {

    @Override
    public Configuration findOne(int configId) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("call find_file_configuration(?);");
        List<Configuration> list = useProcedure(joiner.toString(), DatabaseConnector.CONTROLLER, new ConfigurationMapper(), configId);
        Configuration configuration = new Configuration();
        for (Configuration config : list) {
            configuration.setSource(config.getSource());
            Map<String, String> map = config.getProperties();
            for (Map.Entry<String, String> entry : map.entrySet())
                configuration.addProperty(entry.getKey(), entry.getValue());

        }
        return configuration;
    }
}
