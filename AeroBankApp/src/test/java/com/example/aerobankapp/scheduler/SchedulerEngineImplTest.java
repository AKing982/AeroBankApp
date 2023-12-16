package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.workbench.transactions.Deposit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class SchedulerEngineImplTest {

    @MockBean
    private SchedulerEngineImpl schedulerEngine;

    @Autowired
    @Mock
    private Scheduler scheduler;

    @Autowired
    @Mock
    private SchedulerCriteria schedulerCriteria;

    @Autowired
    @Mock
    private Deposit deposit;

    @Mock
    private Trigger trigger;

    @Mock
    private CronTrigger cronTrigger;

    @Mock
    private JobKey jobKey;

    @BeforeEach
    void setUp() {
            scheduler = mock(Scheduler.class);
            schedulerCriteria = mock(SchedulerCriteria.class);
            trigger = mock(Trigger.class);
            jobKey = mock(JobKey.class);

            schedulerEngine = new SchedulerEngineImpl(deposit, schedulerCriteria, ScheduleType.MONTHLY);
        }

        @Test
        public void testConstructor() throws SchedulerException {
            SchedulerCriteria schedulerCriteria1 = schedulerEngine.getSchedulerCriteria();
            assertNotNull(schedulerCriteria1);
            assertNotNull(schedulerCriteria1);
        }

        @Test
        public void testSchedulerBean() throws SchedulerException {
            Scheduler expected = mock(Scheduler.class);
            Scheduler actual = schedulerEngine.getSchedulerBean();

            assertNotNull(actual);
            assertNotEquals(expected, actual);
        }

        @Test
        public void testIsTriggerJobAsTrue() throws SchedulerException {
            trigger = mock(Trigger.class);
            cronTrigger = mock(CronTrigger.class);
            Scheduler scheduler = schedulerEngine.getScheduler();
            boolean isTrigger = schedulerEngine.isTriggerJob(trigger);
            boolean isNotTrigger = schedulerEngine.isTriggerJob(cronTrigger);

            assertNotNull(scheduler);
            assertTrue(isTrigger);
            assertFalse(isNotTrigger);
        }

        @Test
        public void testShutdownMethodWithBoolean() throws SchedulerException {
            schedulerEngine.shutdown(true);

            boolean schedulerIsShutdown = schedulerEngine.isShutdown();

            assertFalse(schedulerIsShutdown);
        }

        @Test
        @Deprecated
        public void testHasAdminAccessRights()
        {

        }


        @Test
        public void testIsShutdownMethod() throws SchedulerException {
            boolean isShutdown = true;
            SchedulerEngineImpl mockDepositScheduler = mock(SchedulerEngineImpl.class);
            boolean actuallyShutdown = mockDepositScheduler.isShutdown();
            mockDepositScheduler.stop();

            verify(mockDepositScheduler).stop();
            assertFalse(actuallyShutdown);

        }

        @Test
        public void testIsShutDownWithShutdownAsFalse() throws SchedulerException {
            boolean isShutdown = false;
            Scheduler mockScheduler = schedulerEngine.getScheduler();
            boolean isActuallyShutdown = schedulerEngine.isShutdown = false;
            boolean schedulerIsRunning = schedulerEngine.getScheduler().isShutdown();
            boolean isValueShutdown = schedulerEngine.isShutdown();

            schedulerEngine.stop();

            // verify(mockDepositScheduler).stop();
            assertNotNull(mockScheduler);
            assertFalse(schedulerIsRunning);
            assertTrue(isValueShutdown);
        }

        @Test
        public void testIsPauseMethodWhenPauseIsTrue() throws SchedulerException {
            boolean isPausedExpected = true;
            Scheduler mockScheduler = schedulerEngine.getScheduler();
            boolean schedulerIsPaused = mockScheduler.isInStandbyMode();

            // pause the scheduler
            //depositScheduler.pause();

            boolean isActuallyPaused = schedulerEngine.isInStandby();

            assertNotNull(mockScheduler);
            assertTrue(schedulerIsPaused);
            assertFalse(isActuallyPaused);
        }

        @Test
        public void testAddJob()
        {
            JobDetail mockJobDetail = JobBuilder.newJob(Job.class)
                    .withIdentity("mockJob", "mockGroup")
                    .storeDurably().build();

            JobKey mockKey = mockJobDetail.getKey();
            Trigger mockTrigger = mock(Trigger.class);
            boolean param = true;
            schedulerEngine.addJob(mockJobDetail, true);
            schedulerEngine.scheduleJob(mockJobDetail, mockTrigger);

            boolean containsJob = schedulerEngine.checkExists(mockKey);

            assertTrue(containsJob);
        }

        @Test
        public void deleteJob() throws SchedulerException {


            JobDetail mockJobDetail = JobBuilder.newJob(Job.class)
                    .withIdentity("mockJob", "mockGroup")
                    .storeDurably().build();

            Trigger mockTrigger = TriggerBuilder.newTrigger()
                    .withIdentity("mockTrigger", "mockTriggerGroup")
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                    .startAt(new Date())
                    .build();

            schedulerEngine.scheduleJob(mockJobDetail, mockTrigger);
            schedulerEngine.start();

            JobKey mockJobKey = mockJobDetail.getKey();

            boolean actualDeleteResult = schedulerEngine.deleteJob(mockJobKey);

            assertNotNull(mockJobKey);
            assertNotNull(mockJobDetail);
            assertNotNull(mockTrigger);
            assertTrue(actualDeleteResult);

        }

        @Test
        public void testCurrentlyExecutingJobs()
        {
            JobDetail mockJobDetail = JobBuilder.newJob(Job.class)
                    .withIdentity("mockJob", "mockGroup")
                    .storeDurably().build();

            Trigger mockTrigger = TriggerBuilder.newTrigger()
                    .withIdentity("mockTrigger", "mockTriggerGroup")
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                    .startAt(new Date())
                    .build();

            schedulerEngine.scheduleJob(mockJobDetail, mockTrigger);
            schedulerEngine.start();

            List<JobExecutionContext> expectedJobs = new ArrayList<>();
            List<JobExecutionContext> actualExecutingJobs = schedulerEngine.getCurrentlyExecutingJobs();

            assertNotNull(mockJobDetail);
            assertNotNull(mockTrigger);
            assertNotNull(actualExecutingJobs);
            assertEquals(0, actualExecutingJobs.size());
            assertEquals(expectedJobs, actualExecutingJobs);
        }

        @Test
        public void testRescheduleJob() throws SchedulerException {
            Date now = new Date();
            Trigger mockTrigger = TriggerBuilder.newTrigger()
                    .withIdentity("mockTrigger")
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever())
                    .startAt(now)
                    .build();

            Trigger rescheduledTrigger = TriggerBuilder.newTrigger()
                    .withIdentity("rTrigger")
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                    .startAt(now)
                    .build();

            Scheduler mockScheduler = mock(Scheduler.class);
            SchedulerEngineImpl mockDepositScheduler = mock(SchedulerEngineImpl.class);

            TriggerKey triggerKey = mockTrigger.getKey();
            TriggerKey mockTriggerKey = mock(TriggerKey.class);

            mockDepositScheduler.start();
            Date expectedReschedule = new Date();
            when(mockScheduler.rescheduleJob(eq(triggerKey), eq(rescheduledTrigger))).thenReturn(expectedReschedule);
            Date actualReschedule = schedulerEngine.rescheduleJob(triggerKey, rescheduledTrigger);


            assertNotNull(schedulerEngine.getSchedulerBean());
            assertNotNull(schedulerEngine.getScheduler());
            assertNotNull(triggerKey);
            assertNotNull(mockTrigger);
            assertNotEquals(expectedReschedule.getTime(), actualReschedule.getTime());
            assertNotEquals(expectedReschedule, actualReschedule);
        }


        @Test
        public void testCheckExists()
        {
            JobKey expectedJobKey = jobKey;
            boolean exists = schedulerEngine.checkExists(expectedJobKey);



            assertFalse(exists);
        }

        @Test
        public void getJobKeys()
        {
            Set<JobKey> jobKeySet = new HashSet<>();
            JobKey testKey = mock(JobKey.class);
            jobKeySet.add(jobKey);
            jobKeySet.add(testKey);
          //  Set<JobKey> actualJobKeys = depositScheduler.getJobKeys();

           // assertEquals(jobKeySet.size(), actualJobKeys.size());
        }

        @Test
        public void testScheduleJobWithTrigger() throws SchedulerException {
            Scheduler mockScheduler = schedulerEngine.getScheduler();
            JobDetail mockJobDetail = mock(JobDetail.class);
            trigger = mock(Trigger.class);

            // depositScheduler.pause();

            schedulerEngine.scheduleJob(mockJobDetail, trigger);

        }

        @Test
        public void testResumeMethod() throws SchedulerException {
            Scheduler mockScheduler = schedulerEngine.getScheduler();
            boolean schedulerIsStarted = mockScheduler.isStarted();
            boolean isStarted = schedulerEngine.isStarted();

            schedulerEngine.resume();

            assertNotNull(mockScheduler);
            assertFalse(schedulerIsStarted);
            assertFalse(isStarted);
        }




        @Test
        public void testPauseMethodWhenPauseIsFalse() throws SchedulerException {
            boolean isPauseExpected = false;
            Scheduler mockScheduler = schedulerEngine.getScheduler();

            // set the scheduler pause to false
            mockScheduler.start();

            boolean isActuallyPaused = schedulerEngine.isInStandby();

            assertNotNull(mockScheduler);
            assertFalse(isActuallyPaused);
        }



        @Test
        public void testDailySimpleScheduler()
        {
            Scheduler dailyScheduler = schedulerEngine.getSchedulerFactoryInstance();

            assertNotNull(dailyScheduler);
        }

        @AfterEach
        void tearDown()
        {

        }

}