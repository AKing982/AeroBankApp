package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.DepositObjectCreator;
import com.example.aerobankapp.scheduler.trigger.SchedulerTriggerImpl;
import com.example.aerobankapp.workbench.transactions.Deposit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@SpringBootTest
@RunWith(SpringRunner.class)
class ScheduleRunnerTest {

    @Mock
    private SchedulerEngineImpl schedulerEngine;

    @Autowired
    @Qualifier("depositJobDetail")
    private JobDetail jobDetail;

    @InjectMocks
    private ScheduleRunner scheduleRunner;

    @BeforeEach
    void setUp()
    {

    }

    @Test
    public void testStartScheduler() throws SchedulerException
    {
        scheduleRunner.startScheduler();

     //   Mockito.verify(schedulerEngine).startScheduler();
    }

    @Test
    @WithMockUser(roles="USER")
    public void testSchedulingDepositJob() throws SchedulerException {
        // Create the Deposit
        Deposit depositObjectCreator = DepositObjectCreator.createRandomDeposit();

        // Create the TriggerCriteria
        TriggerCriteria triggerCriteria = TriggerCriteria.builder()
                .hour(1)
                .interval(ScheduleType.ONCE)
                .year(2024)
                .minute(15)
                .month(2)
                .day(4)
                .second(1)
                .build();
        CronExpressionBuilder cronExpressionBuilder = new CronExpressionBuilderImpl(triggerCriteria);
        SchedulerTriggerImpl schedulerTrigger1 = new SchedulerTriggerImpl(triggerCriteria, cronExpressionBuilder);
        Trigger onceTrigger = schedulerTrigger1.getRunOnceTrigger();

        scheduleRunner.scheduleJob(jobDetail, onceTrigger);

        Mockito.verify(schedulerEngine).scheduleJob(jobDetail, onceTrigger);
        Mockito.verify(schedulerEngine, times(1)).scheduleJob(jobDetail, onceTrigger);
    }

    @AfterEach
    void tearDown() {
    }
}