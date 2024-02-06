package com.example.aerobankapp.scheduler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class SchedulerEngineImplTest
{

    private SchedulerEngineImpl schedulerEngine;

    @Mock
    private Scheduler mockScheduler;

    @Mock
    private CronExpressionBuilder cronExpressionBuilder;



    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
        schedulerEngine = new SchedulerEngineImpl(mockScheduler,cronExpressionBuilder);
    }

    @Test
    public void testScheduleJob() throws SchedulerException
    {
        JobDetail mockJobDetail = mock(JobDetail.class);
        Trigger mockTrigger = mock(Trigger.class);
        TriggerKey triggerKey = new TriggerKey("key");

        when(mockTrigger.getKey()).thenReturn(triggerKey);
        schedulerEngine.scheduleJob(mockJobDetail, mockTrigger);

        verify(mockScheduler).scheduleJob(mockJobDetail, mockTrigger);
    }

    @Test
    public void testUpdateSchedule() throws SchedulerException{
        Trigger mockTrigger = mock(Trigger.class);
        String jobName = "job1";
        String groupName = "group1";

        when(mockScheduler.checkExists(new JobKey(jobName, groupName))).thenReturn(true);

        schedulerEngine.updateJobSchedule(jobName, groupName, mockTrigger);

        verify(mockScheduler).rescheduleJob(TriggerKey.triggerKey(jobName, groupName), mockTrigger);

    }

    @AfterEach
    void tearDown() {
    }
}