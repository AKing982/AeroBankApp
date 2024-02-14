package com.example.aerobankapp.scheduler.trigger;

import com.example.aerobankapp.scheduler.ScheduleType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.quartz.Trigger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class TriggerFactoryTest
{
    @InjectMocks
    private TriggerFactory triggerFactory;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void testWeeklyTriggerInstance()
    {
        Trigger weeklyTrigger = triggerFactory.getTriggerInstance(ScheduleType.WEEKLY);
        assertNotNull(weeklyTrigger);
    }

    @AfterEach
    void tearDown() {
    }
}