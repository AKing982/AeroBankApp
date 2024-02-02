package com.example.aerobankapp.workbench.utilities.quartz;

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

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class QuartzDataSourceImplTest {

    @InjectMocks
    private QuartzDataSourceImpl quartzDataSource;

    @Mock
    private BasicDataSourceImpl basicDataSource;

    private BasicDataSourceImpl basicDataSourceSQLServer;

    @BeforeEach
    void setUp()
    {
        basicDataSource = new BasicDataSourceImpl.BasicDataSourceBuilder()
                .setDBName("aerobankapp")
                .setDBPass("pass")
                .setDBPort(3306)
                .setDBUser("root")
                .setDBServer("localhost")
                .setDBType(DBType.MYSQL)
                .build();

        basicDataSourceSQLServer = new BasicDataSourceImpl.BasicDataSourceBuilder()
                .setDBName("aerobankapp")
                .setDBPass("pass")
                .setDBPort(3306)
                .setDBUser("root")
                .setDBServer("localhost")
                .setDBType(DBType.SSQL)
                .build();

       // quartzDataSource = new QuartzDataSourceImpl(basicDataSource, DBType.MYSQL);

    }

    @Test
    public void testDataSourceNotNullCheck()
    {
        quartzDataSource = new QuartzDataSourceImpl(basicDataSource, DBType.MYSQL);
        quartzDataSource.nullCheck(basicDataSource);
        when(quartzDataSource.nullCheck(basicDataSource)).thenReturn(false);

        assertNotNull(quartzDataSource);
        verify(quartzDataSource).nullCheck(basicDataSource);
    }

    @Test
    public void testDataSourceNullCheck()
    {

        assertThrows(NullPointerException.class, () -> {
            quartzDataSource = new QuartzDataSourceImpl(null, null);
            quartzDataSource.nullCheck(null);
        });

        assertThrows(NullPointerException.class, () -> {
            quartzDataSource = new QuartzDataSourceImpl(basicDataSource, null);
            quartzDataSource.nullCheck(basicDataSource);});
    }


    @Test
    public void testDataSourceURLMethod()
    {
        quartzDataSource = new QuartzDataSourceImpl(basicDataSource, DBType.MYSQL);
        String expectedProtocol = "mysql";
        String expectedServer = "localhost";
        int expectedPort = 3306;
        String expectedDBName = "aerobankapp";
        String actualQuartzDBURL = quartzDataSource.getDbQuartzURL();
        String expectedQuartzDBURL = "mysql://localhost:" + 3306 + "/" + "aerobankapp";

        assertNotNull(quartzDataSource);
        assertEquals(expectedProtocol, "mysql");
        assertEquals(expectedServer, "localhost");
        assertEquals(expectedPort, 3306);
        assertEquals(expectedDBName, "aerobankapp");
        assertEquals(expectedQuartzDBURL, actualQuartzDBURL);
       // assertNull(expectedQuartzDBURL);
    }

    @Test
    public void testDataSourceJobStoreMethodSuccess()
    {
        quartzDataSource = new QuartzDataSourceImpl(basicDataSource, DBType.MYSQL);
        String expectedJobStore = "org.quartz.impl.jdbcjobstore.StdJDBCDelegate";
        when(quartzDataSource.getJobStore()).thenReturn(expectedJobStore);
        String actualJobStore = quartzDataSource.getJobStore();

        assertNotNull(quartzDataSource);
        assertEquals(expectedJobStore, actualJobStore);
        verify(quartzDataSource, times(1)).getJobStore();
    }

    @Test
    public void testDataSourceJobStoreMethodFail()
    {
        quartzDataSource = new QuartzDataSourceImpl(basicDataSource, DBType.MYSQL);
        String expectedJobStore = "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate";
        String expectedJobStore2 = "org.quartz.impl.jdbcjobstore.StdJDBCDelegate";
        String actualJobStore = quartzDataSource.getJobStore();

        assertNotNull(quartzDataSource);
        assertNotEquals(expectedJobStore, actualJobStore);
        assertEquals(expectedJobStore2, actualJobStore);
    }

    @Test
    public void testQuartzDBURLWithSQLServer()
    {
       // quartzDataSource = new QuartzDataSourceImpl(basicDataSource, DBType.SQLSERVER);

        BasicDataSourceImpl basicDataSourceSQLServer = new BasicDataSourceImpl.BasicDataSourceBuilder()
                .setDBType(DBType.SSQL)
                .setDBName("aerobankapp")
                .setDBPass("pass")
                .setDBPort(3306)
                .setDBUser("root")
                .setDBServer("localhost")
                .build();

        QuartzDataSourceImpl quartzDataSource1 = new QuartzDataSourceImpl(basicDataSourceSQLServer);
        String expectedDBURL = "sqlserver://localhost:1433;databaseName=aerobankapp;integratedSecurity=false;encrypt=true;trustServerCertificate=true";
        String actualDBURL = quartzDataSource1.getQuartzURL();
        DBType actualDBType = quartzDataSource1.getDbType();

        System.out.println(quartzDataSource1.getDbSource().toString());

      //  assertNotNull(quartzDataSource1);
        assertEquals(DBType.SSQL, actualDBType);
       // assertEquals(expectedDBURL, actualDBURL);
        //assertEquals("sqlserver", quartzDataSource.getDbSource().getDBProtocol());
    }

    @AfterEach
    void tearDown()
    {
        quartzDataSource = null;
        basicDataSource = null;
    }
}