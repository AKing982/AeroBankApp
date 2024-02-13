package com.example.aerobankapp.scheduler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.SchedulerException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class ScheduleRunnerTest {

    @Mock
    private SchedulerEngineImpl schedulerEngine;

    @InjectMocks
    private ScheduleRunner scheduleRunner;

    @BeforeEach
    void setUp()
    {
        scheduleRunner = new ScheduleRunner(schedulerEngine);
    }

    @Test
    public void testStartScheduler() throws SchedulerException
    {
        scheduleRunner.startScheduler();

        Mockito.verify(schedulerEngine).startScheduler();
    }

    @Test
    public void testSchedulingDepositJob()
    {

    }

    @AfterEach
    void tearDown() {
    }
}