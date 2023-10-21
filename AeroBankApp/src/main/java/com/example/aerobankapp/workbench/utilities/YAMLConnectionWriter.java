package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.workbench.utilities.connections.BasicDataSource;
import com.example.aerobankapp.workbench.utilities.connections.BasicDataSourceImpl;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
        }
        try
        {
            Yaml yaml = new Yaml();
            FileWriter fw = new FileWriter(configName);
            yaml.dump(dataSource, fw);
            isWritten = true;

        }catch(IOException ex)
        {
            aeroLogger.error("Error Writing to YAML");
        }
        return isWritten;
    }

        public BasicDataSourceImpl readConfigFile(final File config)
        {
            BasicDataSourceImpl connectionDTO1 = null;
            if(config.exists())
            {
                String fileName = config.getName();
                try(FileReader fileReader = new FileReader(fileName))
                {
                    Yaml yaml = new Yaml();
                    connectionDTO1 = yaml.loadAs(fileReader, BasicDataSourceImpl.class);

                }
                catch(IOException ex)
                {
                    aeroLogger.error("Error Reading db-config.yaml file: " + ex);
                }
        }
        else
        {
            aeroLogger.error("No File Exists with name: " + config.getName());
        }

        return connectionDTO1;
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
