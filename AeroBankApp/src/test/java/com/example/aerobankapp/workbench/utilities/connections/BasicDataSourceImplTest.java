package com.example.aerobankapp.workbench.utilities.connections;

import jakarta.persistence.Basic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class BasicDataSourceImplTest {

    @InjectMocks
    private BasicDataSource basicDataSource;

    @BeforeEach
    void setUp() {
        //basicDataSource = new BasicDataSourceImpl.BasicDataSourceBuilder();
    }

    @Test
    public void testBasicDataSourceInitialization()
    {
        //basicDataSource.
    }

    @AfterEach
    void tearDown() {
    }
}