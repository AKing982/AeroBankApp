package com.example.aerobankapp.scheduler.factory.trigger;

import com.example.aerobankapp.scheduler.TriggerCriteria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.quartz.DateBuilder.tomorrowAt;

@RunWith(SpringRunner.class)
@SpringBootTest
class DailyTriggerFactoryTest
{
    @MockBean
    private DailyTriggerFactory dailyTriggerFactory;
    @Autowired
    @Mock
    private TriggerCriteria triggerCriteria;
    private static final String triggerID = UUID.randomUUID().toString();
    private static final String groupID = UUID.randomUUID().toString();
    private static final int INTERVAL = 24;

    @BeforeEach
    void setUp()
    {
        triggerCriteria = TriggerCriteria.builder()
                .hour(5)
                .second(0)
                .minute(10)
                .build();

        dailyTriggerFactory = new DailyTriggerFactory(triggerCriteria);
    }

    @Test
    @DisplayName("Test Constructor")
    public void testConstructor()
    {
        TriggerCriteria triggerCriteria1 = dailyTriggerFactory.getTriggerCriteria();

        assertNotNull(triggerCriteria1);
        assertNotNull(triggerCriteria);
        assertNotNull(dailyTriggerFactory);
    }

    @Test
    @DisplayName("Test Create Trigger")
    public void testCreateTrigger()
    {
        Trigger trigger = dailyTriggerFactory.createTrigger();
        TriggerKey triggerIdentity = TriggerKey.triggerKey(triggerID, groupID);
        TriggerKey actualTriggerIdentity = dailyTriggerFactory.getIdentity(triggerID, groupID);
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(INTERVAL);
        SimpleScheduleBuilder actualSchedule = dailyTriggerFactory.getSimpleScheduleBuilder(INTERVAL);
        Date startDate = tomorrowAt(10, 5, 0);
        Date actualStartDate = dailyTriggerFactory.getStartDate(10, 5);

        assertEquals(startDate, actualStartDate);
        assertNotSame(scheduleBuilder, actualSchedule);
        assertEquals(triggerIdentity, actualTriggerIdentity);
        assertNotNull(trigger);
    }


    @AfterEach
    void tearDown() {
    }
}