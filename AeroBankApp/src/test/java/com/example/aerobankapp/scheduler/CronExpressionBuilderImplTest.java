package com.example.aerobankapp.scheduler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class CronExpressionBuilderImplTest
{
    private CronExpressionBuilderImpl cronExpressionBuilder;

    private TriggerCriteria triggerCriteria;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void testNullConstructor()
    {
        assertThrows(NullPointerException.class, () -> {
            cronExpressionBuilder = new CronExpressionBuilderImpl(null);
        });
    }

    @Test
    public void testCreateOnceCronExpression()
    {
        TriggerCriteria triggerCriteria1 = TriggerCriteria.builder()
                .second(1)
                .interval(ScheduleType.ONCE)
                .year(2024)
                .day(5)
                .hour(8)
                .month(2)
                .minute(15)
                .build();

        cronExpressionBuilder = new CronExpressionBuilderImpl(triggerCriteria1);

        String expectedCron = "1 15 8 5 2 ? 2024";
        String actualCron = cronExpressionBuilder.createCronExpression();

        assertNotNull(cronExpressionBuilder);
        assertEquals(expectedCron, actualCron);
    }

    @Test
    public void testCreateDailyCronSchedule()
    {
        TriggerCriteria triggerCriteria1 = TriggerCriteria.builder()
                .second(1)
                .interval(ScheduleType.DAILY)
                .year(2024)
                .day(5)
                .hour(8)
                .month(2)
                .minute(15)
                .build();

        cronExpressionBuilder = new CronExpressionBuilderImpl(triggerCriteria1);

        String expectedCron = "1 15 8 * * ? *";
        String actualCron = cronExpressionBuilder.createCronExpression();

        assertNotNull(cronExpressionBuilder);
        assertEquals(expectedCron, actualCron);
    }

    @Test
    public void testWeeklyCronSchedule()
    {
        TriggerCriteria triggerCriteria1 = TriggerCriteria.builder()
                .second(1)
                .interval(ScheduleType.WEEKLY)
                .year(2024)
                .day(5)
                .hour(8)
                .month(2)
                .minute(15)
                .build();

        cronExpressionBuilder = new CronExpressionBuilderImpl(triggerCriteria1);

        String expectedCron = "1 15 8 ? * 5 *";
        String actualCron = cronExpressionBuilder.createCronExpression();

        assertNotNull(expectedCron);
        assertEquals(expectedCron, actualCron);
    }

    @Test
    public void testMonthlyCronSchedule()
    {
        TriggerCriteria triggerCriteria1 = TriggerCriteria.builder()
                .second(1)
                .interval(ScheduleType.MONTHLY)
                .year(2024)
                .day(5)
                .hour(8)
                .month(2)
                .minute(15)
                .build();

        cronExpressionBuilder = new CronExpressionBuilderImpl(triggerCriteria1);
        String expectedCron = "1 15 8 5 * ? *";
        String actualCron = cronExpressionBuilder.createCronExpression();

        assertNotNull(actualCron);
        assertEquals(expectedCron, actualCron);
    }

    @Test
    public void testMonthlyCronScheduleWithDateGreaterThanThirtyOne()
    {
        TriggerCriteria triggerCriteria1 = TriggerCriteria.builder()
                .second(1)
                .interval(ScheduleType.MONTHLY)
                .year(2024)
                .day(32)
                .hour(8)
                .month(2)
                .minute(15)
                .build();

        cronExpressionBuilder = new CronExpressionBuilderImpl(triggerCriteria1);

        String expectedCron = "1 15 8 2 * ? *";
        String actualCron = cronExpressionBuilder.createCronExpression();

        assertNotNull(actualCron);
        assertEquals(expectedCron, actualCron);
    }


    @AfterEach
    void tearDown() {
    }
}