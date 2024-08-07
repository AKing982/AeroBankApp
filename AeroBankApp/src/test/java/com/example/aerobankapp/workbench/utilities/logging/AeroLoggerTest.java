package com.example.aerobankapp.workbench.utilities.logging;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.testfx.framework.junit5.ApplicationExtension;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class AeroLoggerTest {

    @InjectMocks
    private AeroLogger aeroLogger;

    @Mock
    private Logger mockLogger;


    @BeforeEach
    void setUp()
    {
        mockLogger = mock(Logger.class);
        aeroLogger = mock(AeroLogger.class);
    }

    @Test
    public void testLogger()
    {
        aeroLogger.info("Message Test");
        verify(aeroLogger).info("Message Test");
        assertNotNull(aeroLogger);

    }

    @Test
    public void testAeroLoggerExceptionHandling()
    {
        Exception NullPointerException = new NullPointerException();
        aeroLogger.handleException(NullPointerException, "Error NullPointer Thrown");
        verify(aeroLogger).handleException(NullPointerException, "Error NullPointer Thrown");
    }

    @AfterEach
    void tearDown()
    {

    }
}