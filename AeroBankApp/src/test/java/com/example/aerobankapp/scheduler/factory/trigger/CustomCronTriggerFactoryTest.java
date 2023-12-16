package com.example.aerobankapp.scheduler.factory.trigger;

import com.example.aerobankapp.scheduler.TriggerDetail;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class CustomCronTriggerFactoryTest
{
    @MockBean
    private CustomCronTriggerFactory customCronTriggerFactory;

    @Autowired
    @Mock
    private TriggerDetail triggerDetail;

    @BeforeEach
    void setUp()
    {
        triggerDetail = new TriggerDetail(1, 15, 8, 7, 12, 2023);
        customCronTriggerFactory = new CustomCronTriggerFactory(triggerDetail);
    }

    @Test
    @DisplayName("Test Default Constructor")
    public void testConstructor()
    {
        TriggerDetail triggerDetail1 = customCronTriggerFactory.getTriggerDetail();
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
        Date expectedStartDate = new Date();
        Date actualStartDate = customTrigger.getStartTime();
        ScheduleBuilder scheduleBuilder = customTrigger.getScheduleBuilder();
        ScheduleBuilder expectedSchedule = CronScheduleBuilder.cronSchedule(expectedExpression);

        assertNotSame(expectedSchedule, scheduleBuilder);
        //assertEquals(expectedSchedule, scheduleBuilder);
        assertEquals(expectedExpression, actualExpression);
        assertEquals(expectedStartDate, actualStartDate);
        assertNotNull(customTrigger);
    }

    @AfterEach
    void tearDown() {
    }

}