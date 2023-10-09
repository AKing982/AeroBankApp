package com.example.aerobankapp.workbench.utilities.logging;

import com.example.aerobankapp.workbench.LoginGUI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.testfx.framework.junit5.ApplicationExtension;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class AeroLoggerTest {

    private AeroLogger aeroLogger;
    private Logger mockLogger;

    @BeforeEach
    void setUp()
    {
        mockLogger = mock(Logger.class);
        aeroLogger = new AeroLogger(mockLogger.getClass());
    }

    @Test
    public void testLogger()
    {
        aeroLogger.info("Message Test");
        verify(aeroLogger).info("Message Test");
        assertNotNull(aeroLogger);


    }

    @AfterEach
    void tearDown()
    {

    }
}