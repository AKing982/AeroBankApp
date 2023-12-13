package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.scheduler.jobdetail.DepositJobDetail;
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


import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.scheduler.jobdetail.DepositJobDetail;
import com.example.aerobankapp.workbench.transactions.Deposit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class DepositSchedulerTest {

    @MockBean
    private DepositScheduler depositScheduler;

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

            depositScheduler = new DepositScheduler(deposit, schedulerCriteria);
        }

        @Test
        public void testConstructor() throws SchedulerException {
            SchedulerCriteria schedulerCriteria1 = depositScheduler.getSchedulerCriteria();
            assertNotNull(depositScheduler);
            assertNotNull(schedulerCriteria1);
        }

        @Test
        public void testSchedulerBean() throws SchedulerException {
            Scheduler expected = mock(Scheduler.class);
            Scheduler actual = depositScheduler.getSchedulerBean();

            assertNotNull(actual);
            assertNotEquals(expected, actual);
        }

        @Test
        public void testIsTriggerJobAsTrue() throws SchedulerException {
            trigger = mock(Trigger.class);
            cronTrigger = mock(CronTrigger.class);
            Scheduler scheduler = depositScheduler.getScheduler();
            boolean isTrigger = depositScheduler.isTriggerJob(trigger);
            boolean isNotTrigger = depositScheduler.isTriggerJob(cronTrigger);

            assertNotNull(scheduler);
            assertTrue(isTrigger);
            assertFalse(isNotTrigger);
        }

        @Test
        public void testShutdownMethodWithBoolean() throws SchedulerException {
            depositScheduler.shutdown(true);

            boolean schedulerIsShutdown = depositScheduler.isShutdown();

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
            DepositScheduler mockDepositScheduler = mock(DepositScheduler.class);
            boolean actuallyShutdown = mockDepositScheduler.isShutdown();
            mockDepositScheduler.stop();

            verify(mockDepositScheduler).stop();
            assertFalse(actuallyShutdown);

        }

        @Test
        public void testIsShutDownWithShutdownAsFalse() throws SchedulerException {
            boolean isShutdown = false;
            Scheduler mockScheduler = depositScheduler.getScheduler();
            boolean isActuallyShutdown = depositScheduler.isShutdown = false;
            boolean schedulerIsRunning = depositScheduler.getScheduler().isShutdown();
            boolean isValueShutdown = depositScheduler.isShutdown();

            depositScheduler.stop();

            // verify(mockDepositScheduler).stop();
            assertNotNull(mockScheduler);
            assertFalse(schedulerIsRunning);
            assertTrue(isValueShutdown);
        }

        @Test
        public void testIsPauseMethodWhenPauseIsTrue() throws SchedulerException {
            boolean isPausedExpected = true;
            Scheduler mockScheduler = depositScheduler.getScheduler();
            boolean schedulerIsPaused = mockScheduler.isInStandbyMode();

            // pause the scheduler
            //depositScheduler.pause();

            boolean isActuallyPaused = depositScheduler.isInStandby();

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
            depositScheduler.addJob(mockJobDetail, true);
            depositScheduler.scheduleJob(mockJobDetail, mockTrigger);

            boolean containsJob = depositScheduler.checkExists(mockKey);

            assertTrue(containsJob);
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
            DepositScheduler mockDepositScheduler = mock(DepositScheduler.class);

            TriggerKey triggerKey = mockTrigger.getKey();
            TriggerKey mockTriggerKey = mock(TriggerKey.class);

            mockDepositScheduler.start();
            Date expectedReschedule = new Date();
            when(mockScheduler.rescheduleJob(eq(triggerKey), eq(rescheduledTrigger))).thenReturn(expectedReschedule);
            Date actualReschedule = depositScheduler.rescheduleJob(triggerKey, rescheduledTrigger);


            assertNotNull(depositScheduler.getSchedulerBean());
            assertNotNull(depositScheduler.getScheduler());
            assertNotNull(triggerKey);
            assertNotNull(mockTrigger);
            assertNotEquals(expectedReschedule.getTime(), actualReschedule.getTime());
            assertNotEquals(expectedReschedule, actualReschedule);
        }


        @Test
        public void testCheckExists()
        {
            JobKey expectedJobKey = jobKey;
            boolean exists = depositScheduler.checkExists(expectedJobKey);

            assertTrue(exists);
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
            Scheduler mockScheduler = depositScheduler.getScheduler();
            JobDetail mockJobDetail = mock(JobDetail.class);
            trigger = mock(Trigger.class);

            // depositScheduler.pause();

            depositScheduler.scheduleJob(mockJobDetail, trigger);

        }

        @Test
        public void testResumeMethod() throws SchedulerException {
            Scheduler mockScheduler = depositScheduler.getScheduler();
            boolean schedulerIsStarted = mockScheduler.isStarted();
            boolean isStarted = depositScheduler.isStarted();

            depositScheduler.resume();

            assertNotNull(mockScheduler);
            assertFalse(schedulerIsStarted);
            assertFalse(isStarted);
        }




        @Test
        public void testPauseMethodWhenPauseIsFalse() throws SchedulerException {
            boolean isPauseExpected = false;
            Scheduler mockScheduler = depositScheduler.getScheduler();

            // set the scheduler pause to false
            mockScheduler.start();

            boolean isActuallyPaused = depositScheduler.isInStandby();

            assertNotNull(mockScheduler);
            assertFalse(isActuallyPaused);
        }



        @Test
        public void testDailySimpleScheduler()
        {
            Scheduler dailyScheduler = depositScheduler.getDailySimpleScheduler();

            assertNotNull(dailyScheduler);
        }

        @AfterEach
        void tearDown()
        {

        }

}