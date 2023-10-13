package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.workbench.utilities.connections.BasicDataSourceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;

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
        BasicDataSourceImpl connectionDTO1 = yamlConnectionWriter.readConfigFile(testFile);

        assertNull(connectionDTO1);
    }

    @Test
    public void testYAMLResourcesPath()
    {
        String path = " ";
        String actualPath = yamlConnectionWriter.getYAMLResourcesPath();

        assertEquals(path, actualPath);
    }

    @AfterEach
    void tearDown() {
    }
}