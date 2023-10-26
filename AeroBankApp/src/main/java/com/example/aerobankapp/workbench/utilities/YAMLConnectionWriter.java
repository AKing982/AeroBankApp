package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.exceptions.DataSourceNullException;
import com.example.aerobankapp.workbench.utilities.connections.BasicDataSource;
import com.example.aerobankapp.workbench.utilities.connections.BasicDataSourceImpl;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component
public class YAMLConnectionWriter
{
    private final String configName = "db-config.yaml";
    private final BasicDataSource dataSource;
    private AeroLogger aeroLogger = new AeroLogger(YAMLConnectionWriter.class);

    @Autowired
    public YAMLConnectionWriter(BasicDataSourceImpl dataSource)
    {
        if(dataSource == null)
        {
           // throw new NullPointerException();
        }
        this.dataSource = dataSource;
    }

    public boolean writeToYAML()
    {
        boolean isWritten = false;
        if(dataSource == null)
        {
            aeroLogger.error("ConnectionDTO Null Reference");
            return isWritten;
        }
        try(FileWriter fw = new FileWriter(configName))
        {
            Yaml yaml = new Yaml();
            yaml.dump(dataSource, fw);
            isWritten = true;
            aeroLogger.info("Data Written to file successfully.");

        }catch(IOException ex)
        {
            aeroLogger.error("Error Writing to YAML");
        }
        return isWritten;
    }

    public BasicDataSourceImpl getConversion(String name)
    {

        BasicDataSourceImpl dataSource1 = null;
        try(Reader reader = new FileReader(name))
        {
            Yaml yaml = new Yaml();
            dataSource1 = yaml.load(reader);
        }
        catch(IOException ex)
        {
            aeroLogger.error("Error Reading db-config.yaml file: " + ex);
        }
        return dataSource1;
    }

    public BasicDataSourceImpl readConfigFile(final String fileName)
    {
            File config = new File(fileName);
            BasicDataSourceImpl dataSource1 = null;
            Map<String, Object> data = null;
            if(config.exists() && config.getName().equals("db-config.yaml") || config.getName().equals("db-config.xml"))
            {
                String name = config.getName();
                System.out.println(fileName);
                dataSource1 = getConversion(name);
            }
        else
        {
            aeroLogger.error("No File Exists with name: " + config.getName());
        }

        return dataSource1;
    }

    public Map<String, Object> getConfigurationMap()
    {
        BasicDataSourceImpl dataSourceFromConfig = readConfigFile(configName);
        Map<String, Object> configMap = new HashMap<>();
        if(dataSourceFromConfig == null)
        {
            throw new DataSourceNullException("Configuration DataSource is Null");
        }
        configMap.put("name", "default");
        configMap.put("dbServer", dataSourceFromConfig.getDBServer());
        configMap.put("dbName", dataSourceFromConfig.getDBName());
        configMap.put("dbPort", dataSourceFromConfig.getDBPort());
        configMap.put("dbDriver", dataSourceFromConfig.getDBDriver());
        configMap.put("dbUser", dataSourceFromConfig.getDBUser());
        configMap.put("dbPass", dataSourceFromConfig.getDBPass());
        configMap.put("dbProtocol", dataSourceFromConfig.getDBProtocol());
        configMap.put("dbURL", dataSourceFromConfig.getDBURL());

        return configMap;
    }

    public String getYAMLResourcesPath()
    {
        String rPath = "";
        URL resourcesURL = getClass().getClassLoader().getResource(configName);
        try
        {
            if(resourcesURL == null)
            {
                // Create a new File for db-config.yaml
                rPath = "src/main/resources/" + configName;
                resourcesURL = new URL(rPath);
            }
        }catch(MalformedURLException ex)
        {
            aeroLogger.error("Invalid Resources URL: " + ex);
        }
        rPath = resourcesURL.getPath();
        return rPath;
    }

    public File createYAMLFile(String name) {
        File yamlFile = null;
        String resourcePath = getYAMLResourcesPath();
        if (resourcePath != null)
        {
            yamlFile = new File(name);
        }
        return yamlFile;
    }

}
