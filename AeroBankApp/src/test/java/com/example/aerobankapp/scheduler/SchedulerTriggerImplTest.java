package com.example.aerobankapp.scheduler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class SchedulerTriggerImplTest
{
    @MockBean
    private SchedulerTriggerImpl schedulerTrigger;
    private TriggerDetail triggerDetail;

    @BeforeEach
    void setUp()
    {
        triggerDetail = new TriggerDetail(1, 5, 11, 12, 6, 2023);
        schedulerTrigger = new SchedulerTriggerImpl(triggerDetail);
    }

    @Test
    @DisplayName("Test Constructor")
    public void constructorTest()
    {
        TriggerDetail triggerDetail1 = schedulerTrigger.getTriggerDetail();

        assertNotNull(triggerDetail1);
        assertNotNull(schedulerTrigger);
    }

    @Test
    @DisplayName("Test Trigger Detail values")
    public void testTriggerDetailValues()
    {
        int expected_day = 12;
        int actual_day = schedulerTrigger.getDay();

        int expected_month = 6;
        int actual_month = schedulerTrigger.getMonth();

        int expected_year = 2023;
        int actual_year = schedulerTrigger.getYear();

        int expected_minute = 11;
        int actual_minute = schedulerTrigger.getMinute();

        int expected_hour = 5;
        int actual_hour = schedulerTrigger.getHour();

        int expected_interval = 1;
        int actual_interval = schedulerTrigger.getInterval();

        assertEquals(expected_interval, actual_interval);
        assertEquals(expected_minute, actual_minute);
        assertEquals(expected_year, actual_year);
        assertEquals(expected_month, actual_month);
        assertEquals(expected_hour, actual_hour);
        assertEquals(expected_day, actual_day);
    }

    @Test
    @DisplayName("Test Daily Trigger")
    public void testDailyTrigger()
    {
        Trigger dailyTrigger = schedulerTrigger.getDailyTrigger();

        assertNotNull(dailyTrigger);
    }



    @AfterEach
    void tearDown() {
    }
}