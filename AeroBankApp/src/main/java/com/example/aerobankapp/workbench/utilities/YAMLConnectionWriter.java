package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.model.ConnectionDTO;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class YAMLConnectionWriter
{
    private final String configName = "db-config.yaml";
    private final ConnectionDTO connectionDTO;
    private AeroLogger aeroLogger = new AeroLogger(YAMLConnectionWriter.class);

    @Autowired
    public YAMLConnectionWriter(ConnectionDTO connectionDTO)
    {
        if(connectionDTO == null)
        {
            throw new NullPointerException();
        }
        this.connectionDTO = connectionDTO;
    }

    public void writeToYAML()
    {
        if(connectionDTO == null)
        {
            aeroLogger.error("ConnectionDTO Null Reference");
        }
        try
        {
            Yaml yaml = new Yaml();
            FileWriter fw = new FileWriter(configName);
            yaml.dump(connectionDTO, fw);

        }catch(IOException ex)
        {
            aeroLogger.error("Error Writing to YAML");
        }
    }

        public ConnectionDTO readConfigFile(final File config) {
        ConnectionDTO connectionDTO1 = null;
        if(config.exists())
        {
            String fileName = config.getName();
            try(FileReader fileReader = new FileReader(fileName))
            {
                Yaml yaml = new Yaml();
                connectionDTO1 = yaml.loadAs(fileReader, ConnectionDTO.class);

            }catch(IOException ex)
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
        String resourcesPath = getClass().getResource("db-config.yaml").getPath();
        if(resourcesPath.equals(" "))
        {

        }
        return getClass().getResource("db-config.yaml").getPath();
    }

}
