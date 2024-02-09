package com.example.aerobankapp.workbench.utilities.parser;

import com.example.aerobankapp.exceptions.InvalidScheduledHourException;
import com.example.aerobankapp.exceptions.InvalidScheduledMinuteException;
import com.example.aerobankapp.exceptions.InvalidScheduledYearException;
import com.example.aerobankapp.scheduler.ScheduleType;
import org.assertj.core.util.Streams;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class ScheduleValidatorImplTest
{
    private ScheduleValidatorImpl scheduleValidator;

    @BeforeEach
    void setUp() {
        scheduleValidator = new ScheduleValidatorImpl();
    }


    @ParameterizedTest
    @ValueSource(ints ={31, 32, 35, 40, 45, 50, 51, 52, 75, 100, 110, 221})
    public void testValidatingDateGreaterThanThirtyOne(int day)
    {
        int validatedDay = scheduleValidator.validateDay(day);

        int expectedDay = ((day - 1) % 31) + 1; // Calculate the expected day based on the new logic
        assertEquals(expectedDay, validatedDay, "Day should be reset to a number within 1 to 31");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -5, -7})
    public void testValidatingDatesLessThanZero(int day)
    {
        int validatedDay = scheduleValidator.validateDay(day);

        int expectedDay = 1;
        assertEquals(expectedDay, validatedDay);
    }

    @ParameterizedTest
    @ValueSource(ints = {13, 14, 15, 20, 25, 30})
    public void testValidatingMonthGreaterThanTwelve(int month)
    {
        int validatedMonth = scheduleValidator.validateMonth(month);

        int expectedMonth = ((month - 1) % 12) + 1;
        assertEquals(expectedMonth, validatedMonth);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -5, -7, -10, 0})
    public void testValidatingMonthLessThanZero(int month)
    {
        int validatedMonth = scheduleValidator.validateMonth(month);

        int expectedMonth = 1;
        assertEquals(expectedMonth, validatedMonth);
    }

    @ParameterizedTest
    @ValueSource(ints = {2024, 1999}) // Valid years
    public void testValidYear(int year) {
        int validatedYear = scheduleValidator.validateYear(year);
        assertEquals(year, validatedYear, "Valid year should be unchanged");
    }

    @ParameterizedTest
    @ValueSource(ints = {34, 2, 20256}) // Invalid years
    public void testInvalidYearThrowsException(int year) {
        assertThrows(InvalidScheduledYearException.class, () -> {
            scheduleValidator.validateYear(year);
        }, "Invalid year should throw InvalidScheduledYearException");
    }

    @ParameterizedTest
    @ValueSource(ints = {65, 78, 120})
    public void testValidMinutesWithinFiftyNine(int minute)
    {
        int expectedMinute = ((minute - 1) % 60) + 1;
        int validateMinute = scheduleValidator.validateMinute(minute);
        assertEquals(expectedMinute, validateMinute);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -5, -10})
    public void testInvalidMinutesLessThanZero(int minute)
    {
        assertThrows(InvalidScheduledMinuteException.class, () -> {
            scheduleValidator.validateMinute(minute);
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {25, 27, 30, 35, 40, 100, 110, 220})
    public void testInvalidHoursGreaterThanTwentyFour(int hour)
    {
        int expectedHour = ((hour - 1) % 24) + 1;
        int validatedHour = scheduleValidator.validateHour(hour);

        assertEquals(expectedHour, validatedHour);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -4, -5, -10, -110})
    public void testInvalidHoursLessThanZero(int hour)
    {
        assertThrows(InvalidScheduledHourException.class, () -> {
            scheduleValidator.validateHour(hour);
        });
    }

    @ParameterizedTest
    @EnumSource(ScheduleType.class)
    public void testValidateInterval(ScheduleType interval)
    {
        ScheduleType validatedInterval = scheduleValidator.validateInterval(interval);

        if(interval == ScheduleType.DAILY || interval == ScheduleType.MONTHLY || interval == ScheduleType.ONCE || interval == ScheduleType.WEEKLY || interval == ScheduleType.BIWEEKLY || interval == ScheduleType.BIDAILY)
        {
            assertEquals(interval, validatedInterval, "Interval should be valid and unchanged for DAILY or MONTHLY");
        }
        else {
            assertNull(validatedInterval, "Interval should be null for non-DAILY or non-MONTHLY types");
        }
    }


    @AfterEach
    void tearDown() {
    }
}