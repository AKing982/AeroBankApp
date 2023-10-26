package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.workbench.utilities.connections.BasicDataSource;
import com.example.aerobankapp.workbench.utilities.connections.BasicDataSourceImpl;
import com.example.aerobankapp.workbench.utilities.db.DBType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.parameters.P;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
class YAMLConnectionWriterTest {
    @InjectMocks
    private YAMLConnectionWriter yamlConnectionWriter;
    @Mock
    private BasicDataSourceImpl connectionDTO;

    @BeforeEach
    void setUp()
    {
        connectionDTO = mock(BasicDataSourceImpl.class);
        yamlConnectionWriter = new YAMLConnectionWriter(connectionDTO);
    }

    @Test
    public void testForNullConnection()
    {
       assertThrows(NullPointerException.class, () -> {
           yamlConnectionWriter = new YAMLConnectionWriter(null);
       });
    }

    @Test
    public void testReadConfigMethodFail()
    {
        File testFile = new File("random.yaml");
        BasicDataSourceImpl connectionDTO1 = yamlConnectionWriter.readConfigFile("db-config.yaml");

        assertNull(connectionDTO1);
    }

    @Test
    public void testConversion()
    {
        final String name = "db-config.yaml";

        BasicDataSourceImpl basicDataSource = new BasicDataSourceImpl.BasicDataSourceBuilder()
                .setDBServer("localhost")
                .setDBUser("sa")
                .setDBPass("pass")
                .setDBName("aerobank")
                .setDBPort(1433)
                .setDBType(DBType.SQLSERVER)
                .build();
        YAMLConnectionWriter yamlConnectionWriter1 = new YAMLConnectionWriter(basicDataSource);
        BasicDataSourceImpl dataSource = yamlConnectionWriter1.getConversion(name);

        assertNotNull(dataSource);
        assertEquals("localhost", dataSource.getDBServer());
    }

    @Test
    public void testReadConfigFile()
    {
        File configFile = new File("db-config.yaml");
        final DBType SSQL = DBType.SQLSERVER;
        BasicDataSourceImpl basicDataSource = new BasicDataSourceImpl.BasicDataSourceBuilder()
                .setDBServer("localhost")
                .setDBUser("sa")
                .setDBPass("pass")
                .setDBName("aerobank")
                .setDBPort(1433)
                .setDBType(SSQL)
                .build();
        YAMLConnectionWriter yamlConnectionWriter1 = new YAMLConnectionWriter(basicDataSource);
        BasicDataSourceImpl dataSource = yamlConnectionWriter1.readConfigFile("db-config.yaml");

        assertNull(dataSource);
        assertEquals("localhost", dataSource.getDBServer());
        assertEquals("1433", dataSource.getDBPort());
    }

    @Test
    public void testYAMLResourcesPath()
    {
        String path = " ";
        String actualPath = yamlConnectionWriter.getYAMLResourcesPath();

        assertEquals(path, actualPath);
    }

    @Test
    public void testWriteToYAML()
    {
        final DBType SSQL = DBType.SQLSERVER;
        BasicDataSourceImpl basicDataSource = new BasicDataSourceImpl.BasicDataSourceBuilder()
                .setDBServer("localhost")
                .setDBUser("sa")
                .setDBPass("pass")
                .setDBName("aerobank")
                .setDBPort(1433)
                .setDBType(SSQL)
                .build();
        YAMLConnectionWriter yamlConnectionWriter1 = new YAMLConnectionWriter(basicDataSource);
        assertTrue(yamlConnectionWriter1.writeToYAML());
        //System.out.println(basicDataSource.toString());
    }

    @Test
    public void testConfigurationMap()
    {
        final DBType SSQL = DBType.SQLSERVER;
        BasicDataSourceImpl basicDataSource = new BasicDataSourceImpl.BasicDataSourceBuilder()
                .setDBServer("localhost")
                .setDBUser("sa")
                .setDBPass("pass")
                .setDBName("aerobank")
                .setDBPort(1433)
                .setDBType(SSQL)
                .build();
        YAMLConnectionWriter yamlConnectionWriter1 = new YAMLConnectionWriter(basicDataSource);

        Map<String, Object> configMap = yamlConnectionWriter1.getConfigurationMap();
        assertNull(configMap);
    }

    @AfterEach
    void tearDown() {
    }
}