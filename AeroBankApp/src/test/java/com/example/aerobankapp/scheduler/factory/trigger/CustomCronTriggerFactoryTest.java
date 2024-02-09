package com.example.aerobankapp.scheduler.factory.trigger;

import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.scheduler.TriggerCriteria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.ScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.quartz.DateBuilder.dateOf;

@SpringBootTest
@RunWith(SpringRunner.class)
class CustomCronTriggerFactoryTest
{
    @MockBean
    private CustomCronTriggerFactory customCronTriggerFactory;

    @Autowired
    @Mock
    private TriggerCriteria triggerCriteria;

    @BeforeEach
    void setUp()
    {
      //  triggerCriteria = new TriggerCriteria(1, 15, 8, 7, 12, 2023);
        customCronTriggerFactory = new CustomCronTriggerFactory(triggerCriteria);
    }

    @Test
    @DisplayName("Test Default Constructor")
    public void testConstructor()
    {
        TriggerCriteria triggerDetail1 = customCronTriggerFactory.getTriggerCriteria();
        assertNotNull(customCronTriggerFactory);
        assertNotNull(triggerDetail1);
    }

    @Test
    @DisplayName("Custom Cron Trigger")
    public void testCustomCronTrigger()
    {
        CronTrigger customTrigger = customCronTriggerFactory.createCronTrigger();
        String actualExpression = customTrigger.getCronExpression();
        String expectedExpression = "0 8 15 7 12 ? 2023";
        Date expectedStartDate = dateOf(triggerCriteria.getHour(), triggerCriteria.getMinute(), 0, triggerCriteria.getDay(), triggerCriteria.getMonth(), triggerCriteria.getYear());
        Date actualStartDate = customTrigger.getStartTime();
        ScheduleBuilder scheduleBuilder = customTrigger.getScheduleBuilder();
        ScheduleBuilder expectedSchedule = CronScheduleBuilder.cronSchedule(expectedExpression);

        assertNotNull(customTrigger);
        assertNotSame(expectedSchedule, scheduleBuilder);
        //assertEquals(expectedSchedule, scheduleBuilder);
        assertEquals(expectedExpression, actualExpression);
        assertEquals(expectedStartDate, actualStartDate);
        assertNotNull(customTrigger);
    }

    @Test
    @DisplayName("Test Trigger Builder Getter")
    public void testGetTriggerBuilder()
    {
        String emptyCronExpression = "";
        String cronExpression = "0 8 15 7 12 ? 2023";
        CronTrigger customTrigger = customCronTriggerFactory.createCronTrigger();
        CronTrigger triggerBuilder = customCronTriggerFactory.getCustomCronTrigger(cronExpression, triggerCriteria);
        TriggerCriteria nullDetail = null;
        TriggerCriteria partialTriggerDetail = new TriggerCriteria(ScheduleType.ONCE, 15, 8, 7);
        TriggerCriteria noTriggerDetail = new TriggerCriteria(ScheduleType.ONCE, 15, 8);

        assertNotNull(triggerBuilder);
        assertThrows(RuntimeException.class,
                () -> {customCronTriggerFactory.getCustomCronTrigger(emptyCronExpression, triggerCriteria);
        });
        assertThrows(NullPointerException.class,
                () -> {customCronTriggerFactory.getCustomCronTrigger(cronExpression, nullDetail );});

        assertThrows(IllegalArgumentException.class,
                () -> {customCronTriggerFactory.getCustomCronTrigger(cronExpression, partialTriggerDetail);});

        assertThrows(IllegalArgumentException.class,
                () -> {customCronTriggerFactory.getCustomCronTrigger(cronExpression, noTriggerDetail);});
        assertNotNull(cronExpression);
        assertNotNull(customTrigger);
        assertNotNull(triggerBuilder);
    }

    @Test
    @DisplayName("Test Building Cron Expression")
    public void testGetCronExpressionMethod()
    {
        String expectedCronExpression = "0 8 15 7 12 ? 2023";
        String actualCronExpression = customCronTriggerFactory.getCronExpression(triggerCriteria);
        TriggerCriteria nullTriggerDetail = null;
        TriggerCriteria partialDetails = new TriggerCriteria(ScheduleType.ONCE, 15, 8);

        assertNotNull(triggerCriteria);
        assertNotNull(customCronTriggerFactory);
        assertThrows(NullPointerException.class,
                () -> {customCronTriggerFactory.getCronExpression(nullTriggerDetail);});
        assertThrows(IllegalArgumentException.class,
                () -> {customCronTriggerFactory.getCronExpression(partialDetails);});
        assertNotNull(actualCronExpression);
    }

    @Test
    @DisplayName("Test Building Cron Schedule")
    public void testBuildCronSchedule()
    {

    }

    @AfterEach
    void tearDown() {
    }

}