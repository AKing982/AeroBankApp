package com.example.aerobankapp.scheduler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

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

    @Test
    @WithMockUser(roles="ADMIN")
    public void testPauseJob() throws SchedulerException {
        String jobName = "job1";
        String groupName = "group1";
        JobKey jobKey = new JobKey(jobName, groupName);

        schedulerEngine.pauseJob(jobName, groupName);

        verify(mockScheduler).pauseJob(jobKey);
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void testResumeJob() throws SchedulerException {
        String jobName = "job1";
        String groupName = "group1";
        JobKey jobKey = new JobKey(jobName, groupName);

        schedulerEngine.resumeJob(jobName, groupName);

        verify(mockScheduler).resumeJob(jobKey);
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void testDeleteJob() throws SchedulerException
    {
        String jobName = "job1";
        String groupName = "group1";
        JobKey jobKey = new JobKey(jobName, groupName);

        schedulerEngine.deleteJob(jobName, groupName);

        verify(mockScheduler).deleteJob(jobKey);
    }

    @Test
    public void testGetJobDetails() throws SchedulerException {

        String jobName = "job1";
        String groupName = "group1";
        JobDetail mockJobDetail = mock(JobDetail.class);
        JobKey jobKey = new JobKey(jobName, groupName);

        when(mockScheduler.getJobDetail(jobKey)).thenReturn(mockJobDetail);
        JobDetail jobDetail = schedulerEngine.getJobDetails(jobName, groupName);

        verify(mockScheduler).getJobDetail(jobKey);

        assertNotNull(jobDetail);
        assertEquals(mockJobDetail, jobDetail);
        assertNotEquals(jobKey, jobDetail.getKey());
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void testListJobs() throws SchedulerException {
        List<JobDetail> mockJobDetailList = new ArrayList<>();
        JobDetail mockJobDetail1 = mock(JobDetail.class);
        JobDetail mockJobDetail2 = mock(JobDetail.class);

        mockJobDetailList.add(mockJobDetail1);
        mockJobDetailList.add(mockJobDetail2);

        Set<JobKey> jobKeySet = new HashSet<>();
        jobKeySet.add(mockJobDetail1.getKey());
        jobKeySet.add(mockJobDetail2.getKey());

        when(mockScheduler.getJobKeys(GroupMatcher.anyGroup())).thenReturn(jobKeySet);
        when(mockScheduler.getJobDetail(any(JobKey.class))).thenReturn(mockJobDetail1).thenReturn(mockJobDetail2);

        List<JobDetail> actualJobDetails = schedulerEngine.listJobs();

        assertEquals(mockJobDetailList.size(), actualJobDetails.size());
        assertTrue(actualJobDetails.containsAll(mockJobDetailList));

        verify(mockScheduler, times(2)).getJobDetail(any(JobKey.class));
    }

    @Test
    public void testCheckJobExists() throws SchedulerException {
        String jobName = "job1";
        String groupName = "group1";
        JobKey jobKey = new JobKey(jobName, groupName);

        when(mockScheduler.checkExists(jobKey)).thenReturn(true);

        boolean result = schedulerEngine.checkJobExists(jobName,groupName);

        assertTrue(result);
        verify(mockScheduler).checkExists(jobKey);
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void testIsJobRunning() throws SchedulerException {
        String jobName = "job1";
        String groupName = "group1";
        JobKey jobKey = new JobKey(jobName, groupName);
        JobDetail jobDetail = JobBuilder.newJob(Job.class)
                .withIdentity(jobKey)
                .build();

        JobExecutionContext jobExecutionContext = mock(JobExecutionContext.class);
        when(jobExecutionContext.getJobDetail()).thenReturn(jobDetail);

        List<JobExecutionContext> currentlyExecutingJobs = Arrays.asList(jobExecutionContext);

        when(mockScheduler.getCurrentlyExecutingJobs()).thenReturn(currentlyExecutingJobs);

        // Act
        boolean isRunning = schedulerEngine.isJobRunning(jobName, groupName);

        // Assert
        assertTrue(isRunning);
    }

    @AfterEach
    void tearDown() {
    }
}