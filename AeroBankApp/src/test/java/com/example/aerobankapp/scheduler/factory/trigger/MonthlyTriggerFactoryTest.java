package com.example.aerobankapp.scheduler.factory.trigger;

import com.example.aerobankapp.exceptions.NullTriggerCriteriaException;
import com.example.aerobankapp.scheduler.TriggerCriteria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.quartz.CronTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class MonthlyTriggerFactoryTest
{
    @MockBean
    private MonthlyTriggerFactory monthlyTriggerFactory;
    private static final int INTERVAL = 24;

    @Autowired
    private TriggerCriteria triggerCriteria;

    @Autowired
    private TriggerCriteria nullTriggerCriteria;


    @BeforeEach
    void setUp()
    {
        triggerCriteria = TriggerCriteria.builder()
                .day(15)
                .month(7)
                .isCron(true)
                .year(2023)
                .minute(5)
                .hour(12)
                .second(0)
                .interval(1)
                .repeat(0)
                .build();

        nullTriggerCriteria = null;

        monthlyTriggerFactory = new MonthlyTriggerFactory(triggerCriteria);
    }

    @Test
    @DisplayName("Test Constructor")
    public void testConstructor()
    {
        TriggerCriteria triggerCriteria1 = monthlyTriggerFactory.getTriggerCriteria();

        assertNotNull(triggerCriteria1);
        assertThrows(NullTriggerCriteriaException.class,
                () -> {monthlyTriggerFactory = new MonthlyTriggerFactory(nullTriggerCriteria);});
        assertNull(nullTriggerCriteria);
        assertNotNull(monthlyTriggerFactory);
    }

    @Test
    @DisplayName("Test Monthly Trigger")
    public void testMonthlyTrigger()
    {
        CronTrigger monthlyTrigger = monthlyTriggerFactory.createCronTrigger();
    }

    @AfterEach
    void tearDown() {
    }
}