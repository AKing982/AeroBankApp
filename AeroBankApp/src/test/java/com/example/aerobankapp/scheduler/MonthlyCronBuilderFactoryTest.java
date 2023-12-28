package com.example.aerobankapp.scheduler;

import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnit38ClassRunner.class)
class MonthlyCronBuilderFactoryTest
{
    private MonthlyCronBuilderFactory monthlyCronBuilderFactory;

    private TriggerCriteria triggerCriteria;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("Test Create Con method for normal condition")
    public void testCreateConMethod()
    {
        TriggerCriteria triggerCriteria1 = TriggerCriteria.builder()
                .minute(1)
                .hour(11)
                .day(12)
                .month(5)
                .year(2023)
                .interval(1)
                .build();

        String expectedCron = "0 1 11 12 5 ? 2023";
        monthlyCronBuilderFactory = new MonthlyCronBuilderFactory(triggerCriteria1);
        List<String> actualCronSchedules = monthlyCronBuilderFactory.createCron(triggerCriteria1);

        assertNotNull(monthlyCronBuilderFactory);
        assertEquals(expectedCron, actualCronSchedules.get(0));
    }

    @Test
    @DisplayName("Test Create Con Method for all 0 parameters")
    public void testCreateConMethodForZeroParameters()
    {
        TriggerCriteria triggerCriteria1 = TriggerCriteria.builder()
                .minute(0)
                .second(0)
                .hour(0)
                .day(0)
                .month(0)
                .year(0)
                .interval(0)
                .build();

        String expectedCron = "0 0 0 0 0 ? 0";
        monthlyCronBuilderFactory = new MonthlyCronBuilderFactory(triggerCriteria1);
        List<String> actualCronSchedules = monthlyCronBuilderFactory.createCron(triggerCriteria1);
        String actualSchedule = actualCronSchedules.get(0);

        assertThrows(IllegalArgumentException.class,
                () -> {monthlyCronBuilderFactory.createCron(triggerCriteria1);});
        assertEquals(expectedCron, actualSchedule);

    }

    @Test
    @DisplayName("Null Trigger Criteria parameter for Create Con Method")
    public void testNullTriggerCriteriaForCreateConMethod()
    {
        TriggerCriteria nullTriggerCriteria = null;
        String expectedCron = "0 0 0 0 0 ? 0";
        //List<String> actualCrons = monthlyCronBuilderFactory.createCron(nullTriggerCriteria);

        assertThrows(NullPointerException.class,
                () -> {monthlyCronBuilderFactory.createCron(nullTriggerCriteria);});

    }

    @Test
    @DisplayName("Test Create Cron Schedule for every 5 months")
    public void testCreateCronScheduleForEvery5Months()
    {
        TriggerCriteria triggerCriteria1 = TriggerCriteria.builder()
                .interval(5)
                .month(1)
                .minute(10)
                .hour(8)
                .day(5)
                .second(0)
                .year(2023)
                .build();

        String expectedCronString = "0 10 8 5 1 ? 2023";
        monthlyCronBuilderFactory = new MonthlyCronBuilderFactory(triggerCriteria1);
        List<String> actualCronStrings = monthlyCronBuilderFactory.createCron(triggerCriteria1);
        String actualSchedule = actualCronStrings.get(0);

        assertEquals(expectedCronString, actualSchedule);
        assertEquals(3, actualCronStrings.size());
    }

    @Test
    @DisplayName("Test Create Cron Schedule Every 3 months")
    public void testCreateCronScheduleForEvery3Months()
    {
        TriggerCriteria triggerCriteria1 = TriggerCriteria.builder()
                .interval(3)
                .month(1)
                .minute(10)
                .hour(8)
                .day(5)
                .second(0)
                .year(2023)
                .build();

        String expectedCronString = "0 10 8 1 1 ? 2023";
        String expectedFirstSchedule = "0 10 8 5 1 ? 2023";
        String expectedSecondSchedule = "0 10 8 5 4 ? 2023";
        String expectedThirdSchedule = "0 10 8 5 7 ? 2023";
        monthlyCronBuilderFactory = new MonthlyCronBuilderFactory(triggerCriteria1);
        List<String> actualSchedules = monthlyCronBuilderFactory.createCron(triggerCriteria1);
        String firstSchedule = actualSchedules.get(0);
        String secondSchedule = actualSchedules.get(1);
        String thirdSchedule = actualSchedules.get(2);

        assertEquals(4, actualSchedules.size());
        assertEquals(expectedFirstSchedule, firstSchedule);
        assertEquals(expectedSecondSchedule, secondSchedule);
        assertEquals(expectedThirdSchedule, thirdSchedule);
    }

    @Test
    @DisplayName("Test Create Cron Schedule Every 2 Months")
    public void testCreateCronScheduleEvery2Months()
    {
        TriggerCriteria triggerCriteria1 = TriggerCriteria.builder()
                .interval(2)
                .month(1)
                .minute(10)
                .hour(8)
                .day(5)
                .second(0)
                .year(2023)
                .build();
        String expectedFirstSchedule = "0 10 8 5 1 ? 2023";
        String expectedSecondSchedule = "0 10 8 5 3 ? 2023";
        String expectedThirdSchedule = "0 10 8 5 5 ? 2023";
        String expectedFourthSchedule = "0 10 8 5 7 ? 2023";
        String expectedFifthSchedule = "0 10 8 5 9 ? 2023";
        String expectedSixSchedule = "0 10 8 5 11 ? 2023";

        monthlyCronBuilderFactory = new MonthlyCronBuilderFactory(triggerCriteria1);
        List<String> actualCronSchedules = monthlyCronBuilderFactory.createCron(triggerCriteria1);
        String firstSchedule = actualCronSchedules.get(0);
        String secondSchedule = actualCronSchedules.get(1);
        String thirdSchedule = actualCronSchedules.get(2);
        String fourthSchedule = actualCronSchedules.get(3);
        String fifthSchedule = actualCronSchedules.get(4);
        String sixSchedule = actualCronSchedules.get(5);

        assertEquals(6, actualCronSchedules.size());
        assertEquals(expectedFirstSchedule, firstSchedule);
        assertEquals(expectedSecondSchedule, secondSchedule);
        assertEquals(expectedThirdSchedule, thirdSchedule);
        assertEquals(expectedFourthSchedule, fourthSchedule);
        assertEquals(expectedFifthSchedule, fifthSchedule);
        assertEquals(expectedSixSchedule, sixSchedule);

    }



    @Test
    @DisplayName("Test Monthly Cron Schedules By Month Using Wrong Month")
    public void testMonthlyCronSchedulesByMonthWrongMonth()
    {
        int month = 0;
        int testMonth = -1;
        TriggerCriteria triggerCriteria1 = TriggerCriteria.builder()
                //.isMonthly(true)
                .month(1)
                .second(0)
                .hour(8)
                .minute(15)
                .year(2023)
                .interval(2)
                .day(12)
                .build();

        String expectedCronSchedule = "0 15 8 12 5 ? 2023";
        monthlyCronBuilderFactory = new MonthlyCronBuilderFactory(triggerCriteria1);

        assertThrows(IllegalArgumentException.class,
                () -> {monthlyCronBuilderFactory.getMonthlyCronScheduleByMonth(month);});

        assertThrows(IllegalArgumentException.class,
                () -> {monthlyCronBuilderFactory.getMonthlyCronScheduleByMonth(testMonth);});
    }

    @Test
    @DisplayName("Test Monthly Cron Schedules By Month Using Correct Month")
    public void testMonthlyCronSchedulesByMonthUsingCorrectMonth()
    {
        TriggerCriteria triggerCriteria1 = TriggerCriteria.builder()
              //  .isMonthly(true)
                .month(1)
                .second(0)
                .hour(8)
                .minute(15)
                .year(2023)
                .interval(2)
                .day(12)
                .build();

        monthlyCronBuilderFactory = new MonthlyCronBuilderFactory(triggerCriteria1);

        int startMonth = 2;
        String expectedCronSchedule = "";
    }

    @Test
    @Disabled
    @DisplayName("Validate if a cycle exists with month 5 and interval 5")
    public void testCycleExistsWhenMonthFiveAndIntervalFive()
    {
        int month = 5;
        int interval = 5;

        int month2 = 1;
        int interval2 = 0;

        monthlyCronBuilderFactory = new MonthlyCronBuilderFactory();

     //   boolean isCycle = monthlyCronBuilderFactory.doesCycleToNextYear(month, interval);
     //   boolean isCycle2 = monthlyCronBuilderFactory.doesCycleToNextYear(month2, interval2);
       // assertTrue(isCycle);
      //  assertTrue(isCycle2);

    }

    @Test
    @Disabled
    @DisplayName("Test Increment Year when Year is 2023")
    public void testIncrementYearWithYear2023()
    {
        int year = 2023;
        int month = 5;
        int interval = 5;

        monthlyCronBuilderFactory = new MonthlyCronBuilderFactory();
        int incrementedYear = monthlyCronBuilderFactory.getYearIncrement(year);
        int incrementedYearCycle = monthlyCronBuilderFactory.getYearIncrementWithCycle(year,month,interval);

        assertNotEquals(year, incrementedYear);
        assertNotEquals(incrementedYear, incrementedYearCycle);
    }

    @Test
    @Disabled
    @DisplayName("Test Monthly Cron Schedule Adjustment By Year Role Over")
    public void testMonthlyCronScheduleAdjustmentByYearRoleOver()
    {
        TriggerCriteria triggerCriteria1 = TriggerCriteria.builder()
             ///   .isMonthly(true)
                .month(1)
                .second(0)
                .hour(8)
                .minute(15)
                .year(2023)
                .interval(2)
                .day(12)
                .build();

        monthlyCronBuilderFactory = new MonthlyCronBuilderFactory(triggerCriteria1);
        String schedule = monthlyCronBuilderFactory.getMonthlyCronScheduleAdjustmentByYearRoleOver(2023, 5, 4);

        assertEquals("0 30 10 15 5 ? 2023", schedule);
    }

    @Test
    @DisplayName("Test Generate Schedule for Nth Year")
    public void testGenerateScheduleForNthYear()
    {
        int currentYear = 2023;
        int nthYear = 2025;
        int interval = 5;
        int month = 1;
        int day = 15;
        int min = 30;
        int hour = 8;

        monthlyCronBuilderFactory = new MonthlyCronBuilderFactory();

        List<String> schedules = monthlyCronBuilderFactory.generateScheduleForNthYear(currentYear, nthYear, interval, month, day, min, hour);

        assertEquals(8, schedules.size());
    }

    @ParameterizedTest
    @MethodSource("provideMonthsForTesting")
    @DisplayName("Test isReset to New Year")
    public void testIsResetToNewYear(int prevMonth, int currentMonth, boolean expected)
    {

        monthlyCronBuilderFactory = new MonthlyCronBuilderFactory();
        boolean isReset = monthlyCronBuilderFactory.isResetToNewYear(prevMonth, currentMonth);

        assertEquals(expected, isReset);
    }

    private static Stream<Arguments> provideMonthsForTesting()
    {
        return Stream.of(
                Arguments.of(12, 1, true),
                Arguments.of(4, 7, false),
                Arguments.of(1, 3, false),
                Arguments.of(7, 9, false),
                Arguments.of(9, 3, true),
                Arguments.of(10, 5, true),
                Arguments.of(7, 4, true),
                Arguments.of(4, 1, true),
                Arguments.of(3, 4, false),
                Arguments.of(1, 2, false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideYearsAndIntervalForTesting")
    @DisplayName("Test get Number of Iterations From Current Year to Nth Year")
    public void testGetNumberOfIterationsFromCurrentYearToNthYear(int interval, int currYear, int nthYear, int startMonth, int result)
    {
        monthlyCronBuilderFactory = new MonthlyCronBuilderFactory();
        int totalIterations = monthlyCronBuilderFactory.getNumberOfIterationsFromCurrentYearToNthYear(interval, currYear, nthYear, startMonth);

        assertEquals(result, totalIterations);
    }

    private static Stream<Arguments> provideYearsAndIntervalForTesting()
    {
        return Stream.of(
                Arguments.of(5, 2023, 2025, 5, 7),
                Arguments.of(1, 2023, 2025, 4, 33),
                Arguments.of(2, 2023, 2024, 2, 12),
                Arguments.of(10, 2023, 2025, 8, 3),
                Arguments.of(12, 2023, 2024, 2, 2),
                Arguments.of(12, 2023, 2026, 5, 4)

        );
    }






    @AfterEach
    void tearDown() {
    }
}