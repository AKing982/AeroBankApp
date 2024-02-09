package com.example.aerobankapp.scheduler.factory.trigger;

import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.scheduler.TriggerCriteria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class WeeklyTriggerFactoryTest {

    @MockBean
    private WeeklyTriggerFactory weeklyTriggerFactory;

    @Autowired
    private TriggerCriteria triggerCriteria;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Create Trigger")
    public void testCreateTrigger()
    {
        triggerCriteria = TriggerCriteria.builder()
                .month(12)
                .second(0)
                .year(2023)
                .hour(8)
                .day(5)
                .minute(30)
                .interval(ScheduleType.ONCE)
                .build();

        weeklyTriggerFactory = new WeeklyTriggerFactory(triggerCriteria);
        CronTrigger expected = TriggerBuilder.newTrigger()
                .withIdentity("mockTrigger", "mockGroup")
                .withSchedule(CronScheduleBuilder.weeklyOnDayAndHourAndMinute(6, 8, 30))
                .build();

        CronTrigger actual = weeklyTriggerFactory.createCronTrigger();

        assertNotNull(triggerCriteria);
        assertNotNull(weeklyTriggerFactory);
        assertNotNull(actual);
        assertEquals(expected, actual);


    }

    @AfterEach
    void tearDown()
    {
        triggerCriteria = null;
    }
}