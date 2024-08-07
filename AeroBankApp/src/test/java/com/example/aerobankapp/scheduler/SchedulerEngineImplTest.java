package com.example.aerobankapp.scheduler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
@RunWith(SpringRunner.class)
class SchedulerEngineImplTest
{

    private SchedulerEngineImpl schedulerEngine;

    @Mock
    private Scheduler mockScheduler;

    @Captor
    private ArgumentCaptor<JobDetail> jobDetailCaptor;

    @Captor
    private ArgumentCaptor<Trigger> triggerCaptor;



    @BeforeEach
    void setUp()
    {
        openMocks(this);
        schedulerEngine = new SchedulerEngineImpl(mockScheduler);
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

    @Test
    @DisplayName("Test scheduler when not started should start scheduler")
    public void testStartScheduler_WhenNotStarted_ShouldStartScheduler() throws SchedulerException, InterruptedException {
        when(mockScheduler.isStarted()).thenReturn(false);

        schedulerEngine.startScheduler();
        verify(mockScheduler).start();
    }

    @Test
    @DisplayName("Test scheduler whenAlreadyStarted_ShouldNotStartAgain")
    public void testStartScheduler_WhenAlreadyStarted_ShouldNotStartAgain() throws SchedulerException, InterruptedException {
        when(mockScheduler.isStarted()).thenReturn(true);

        schedulerEngine.startScheduler();

        verify(mockScheduler, never()).start();
    }

    @Test
    @DisplayName("Test start scheduler when exception occurs should retry and eventually succeed")
    public void testStartScheduler_WhenException_ShouldRetryAndEventuallySucceed() throws SchedulerException, InterruptedException {
        when(mockScheduler.isStarted())
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(true);
        doThrow(new SchedulerException("Error"))
                .doThrow(new SchedulerException("Error"))
                .doNothing()
                .when(mockScheduler).start();

        schedulerEngine.startScheduler();

        verify(mockScheduler, times(2)).start();
    }

    @Test
    void startScheduler_ShouldRespectInterruption() throws SchedulerException {
        when(mockScheduler.isStarted()).thenReturn(false);
        doThrow(new SchedulerException("Error")).when(mockScheduler).start();

        Thread.currentThread().interrupt();

        assertThrows(InterruptedException.class, () -> schedulerEngine.startScheduler());
        assertTrue(Thread.currentThread().isInterrupted());  // Check if interrupted flag is still set
    }

    @Test
    void stopScheduler_WhenStarted_ShouldStopScheduler() throws SchedulerException {
        when(mockScheduler.isStarted())
                .thenReturn(true)
                .thenReturn(false);

        schedulerEngine.stopScheduler();

        verify(mockScheduler).shutdown(true);
    }

    @Test
    void stopScheduler_WhenNotStarted_ShouldStillAttemptToStop() throws SchedulerException {
        when(mockScheduler.isStarted()).thenReturn(false);

        schedulerEngine.stopScheduler();

        verify(mockScheduler).shutdown(true);
    }

    @Test
    void stopScheduler_WhenExceptionOccurs_ShouldRetryUntilSuccess() throws SchedulerException {
        when(mockScheduler.isStarted())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        doThrow(new SchedulerException("Error"))
                .doThrow(new SchedulerException("Error"))
                .doNothing()
                .when(mockScheduler).shutdown(true);

        schedulerEngine.stopScheduler();

        verify(mockScheduler, times(3)).shutdown(true);
    }

    @Test
    void stopScheduler_WhenExceptionsPersist_ShouldStopWhenSchedulerNotStarted() throws SchedulerException {
        when(mockScheduler.isStarted())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);
        doThrow(new SchedulerException("Persistent error")).when(mockScheduler).shutdown(true);

        schedulerEngine.stopScheduler();

        verify(mockScheduler, times(3)).shutdown(true);
    }

    @Test
    void deleteJob_WhenJobExists_ShouldDeleteSuccessfully() throws SchedulerException {
        String jobName = "testJob";
        String groupName = "testGroup";
        JobKey jobKey = new JobKey(jobName, groupName);

        when(mockScheduler.checkExists(jobKey)).thenReturn(true);
        when(mockScheduler.deleteJob(jobKey)).thenReturn(true);

        boolean result = schedulerEngine.deleteJob(jobName, groupName);

        assertTrue(result);
        verify(mockScheduler).checkExists(jobKey);
        verify(mockScheduler).deleteJob(jobKey);
    }

    @Test
    void deleteJob_WhenJobDoesNotExist_ShouldReturnFalse() throws SchedulerException {
        String jobName = "nonExistentJob";
        String groupName = "testGroup";
        JobKey jobKey = new JobKey(jobName, groupName);

        when(mockScheduler.checkExists(jobKey)).thenReturn(false);

        boolean result = schedulerEngine.deleteJob(jobName, groupName);

        assertFalse(result);
        verify(mockScheduler).checkExists(jobKey);
        verify(mockScheduler, never()).deleteJob(jobKey);
    }

    @Test
    void deleteJob_WhenSchedulerThrowsException_ShouldThrowJobDeletionException() throws SchedulerException {
        String jobName = "testJob";
        String groupName = "testGroup";
        JobKey jobKey = new JobKey(jobName, groupName);

        when(mockScheduler.checkExists(jobKey)).thenReturn(true);
        when(mockScheduler.deleteJob(jobKey)).thenThrow(new SchedulerException("Test exception"));

        assertFalse(schedulerEngine.deleteJob(jobName, groupName));
        verify(mockScheduler).checkExists(jobKey);
        verify(mockScheduler).deleteJob(jobKey);
    }

    @Test
    void deleteJob_WhenJobExistsButDeletionFails_ShouldReturnFalse() throws SchedulerException {
        String jobName = "testJob";
        String groupName = "testGroup";
        JobKey jobKey = new JobKey(jobName, groupName);

        when(mockScheduler.checkExists(jobKey)).thenReturn(true);
        when(mockScheduler.deleteJob(jobKey)).thenReturn(false);

        boolean result = schedulerEngine.deleteJob(jobName, groupName);

        assertFalse(result);
        verify(mockScheduler).checkExists(jobKey);
        verify(mockScheduler).deleteJob(jobKey);
    }

    @Test
    public void testResumeJob_WhenJobExistsAndSchedulerNotInStandBy_ShouldResumeSuccessfully() throws SchedulerException {
        String jobName = "testJob";
        String groupName = "testGroup";
        JobKey jobKey = new JobKey(jobName, groupName);

        when(mockScheduler.isInStandbyMode()).thenReturn(false);
        when(mockScheduler.checkExists(jobKey)).thenReturn(true);

        boolean result = schedulerEngine.resumeJob(jobName, groupName);
        assertTrue(result);
        verify(mockScheduler).checkExists(jobKey);
    }

    @Test
    public void testResumeJob_WhenJobDoesNotExist_ShouldReturnFalse() throws SchedulerException {
        String jobName = "testJob";
        String groupName = "testGroup";
        JobKey jobKey = new JobKey(jobName, groupName);

        when(mockScheduler.isInStandbyMode()).thenReturn(false);
        when(mockScheduler.checkExists(jobKey)).thenReturn(false);

        boolean result = schedulerEngine.resumeJob(jobName, groupName);
        assertFalse(result);
        verify(mockScheduler, never()).resumeJob(jobKey);
    }


    @Test
    void resumeJob_WhenSchedulerInStandbyMode_ShouldRetryAndEventuallySucceed() throws SchedulerException {
        String jobName = "testJob";
        String groupName = "testGroup";
        JobKey jobKey = new JobKey(jobName, groupName);

        when(mockScheduler.isInStandbyMode())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);
        when(mockScheduler.checkExists(jobKey)).thenReturn(true);

        boolean result = schedulerEngine.resumeJob(jobName, groupName);

        assertTrue(result);
        verify(mockScheduler, times(3)).isInStandbyMode();
        verify(mockScheduler).resumeJob(jobKey);
    }

    @Test
    public void resumeJob_WhenSchedulerThrowsException_ShouldThrowSchedulerException() throws SchedulerException {
        String jobName = "testJob";
        String groupName = "testGroup";
        JobKey jobKey = new JobKey(jobName, groupName);

        when(mockScheduler.isInStandbyMode()).thenReturn(false);
        when(mockScheduler.checkExists(jobKey)).thenReturn(true);
        doThrow(new SchedulerException("Test exception")).when(mockScheduler).resumeJob(jobKey);

        assertThrows(SchedulerException.class, () -> schedulerEngine.resumeJob(jobName, groupName));
    }

    @Test
    public void resumeJob_WhenSchedulerInStandbyModePersists_ShouldReturnFalse() throws SchedulerException
    {
        String jobName = "testJob";
        String groupName = "testGroup";

        when(mockScheduler.isInStandbyMode()).thenReturn(true);

        boolean result = schedulerEngine.resumeJob(jobName, groupName);

        assertFalse(result);
        verify(mockScheduler, times(5)).isInStandbyMode();
        verify(mockScheduler, never()).resumeJob(any(JobKey.class));

    }

    @Test
    public void pauseJob_ShouldPauseExistingJob() throws SchedulerException
    {
        String jobName = "testJob";
        String groupName = "testGroup";
        JobKey jobKey = new JobKey(jobName, groupName);
        schedulerEngine.pauseJob(jobName, groupName);

        verify(mockScheduler).pauseJob(jobKey);
    }

    @Test
    public void pauseJob_ShouldHandleSchedulerException() throws SchedulerException
    {
        String jobName = "testJob";
        String groupName = "testGroup";
        JobKey jobKey = new JobKey(jobName, groupName);

        doThrow(new SchedulerException("Test exception")).when(mockScheduler).pauseJob(jobKey);

        assertDoesNotThrow(() -> schedulerEngine.pauseJob(jobName, groupName));
    }

    @Test
    public void pauseJob_ShouldNotPauseNonExistentJob() throws SchedulerException
    {
        String jobName = "testJob";
        String groupName = "testGroup";
        JobKey jobKey = new JobKey(jobName, groupName);

        when(mockScheduler.checkExists(jobKey)).thenReturn(false);

        schedulerEngine.pauseJob(jobName, groupName);

        verify(mockScheduler, never()).pauseJob(jobKey);
    }

    @Test
    public void pauseJob_ShouldReturnTrueWhenJobPaused() throws SchedulerException
    {
        String jobName = "testJob";
        String groupName = "testGroup";
        JobKey jobKey = new JobKey(jobName, groupName);

        when(mockScheduler.checkExists(jobKey)).thenReturn(true);
        boolean result = schedulerEngine.pauseJob(jobName, groupName);
        assertTrue(result);
        verify(mockScheduler).pauseJob(jobKey);
    }






    @AfterEach
    void tearDown() {
    }
}