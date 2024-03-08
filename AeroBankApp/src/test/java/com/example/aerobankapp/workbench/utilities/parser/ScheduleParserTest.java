package com.example.aerobankapp.workbench.utilities.parser;

import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class ScheduleParserTest {

    private ScheduleParser scheduleParser;

    private SchedulerCriteria schedulerCriteria;

    private LocalDate today;

    private LocalTime now;

    @Mock
    private ScheduleValidator scheduleValidator;

    @BeforeEach
    void setUp()
    {
        today = LocalDate.now();
        now = LocalTime.now();
        schedulerCriteria = SchedulerCriteria.builder()
                .scheduledDate(today)
                .scheduledTime(now)
                .scheduleType(ScheduleType.DAILY)
                .schedulerUserID(1)
                .build();

     //   scheduleParser = new ScheduleParserImpl(schedulerCriteria, scheduleValidator);

    }

    @Test
    public void testNullConstructor()
    {
        assertThrows(NullPointerException.class, () ->{
          //  scheduleParser = new ScheduleParserImpl(null, null);
        });
    }


    @Test
    public void testGetParsedDaySegment()
    {
        int expectedParsedDay = 7;

        int actualDaySegment = scheduleParser.getParsedDaySegment();

        assertEquals(expectedParsedDay, actualDaySegment);
    }

    @Test
    public void testGetParsedMonthSegment()
    {

        int expectedMonth = 2;
        int actualMonth = scheduleParser.getParsedMonthSegment();

        assertEquals(expectedMonth, actualMonth);
    }

    @Test
    public void testGetParsedYearSegment()
    {
        int expectedYear = 2024;
        int actualYear = scheduleParser.getParsedYearSegment();

        assertEquals(expectedYear, actualYear);
    }

    @Test
    public void testParsedHourSegment()
    {

        int expectdHour = now.getHour();
        int actual = scheduleParser.getParsedHourSegment();

        assertEquals(expectdHour, actual);
    }

    @Test
    public void testGetParsedMinuteSegment()
    {

        int expectedMinute = now.getMinute();
        int actual = scheduleParser.getParsedMinuteSegment();

        assertEquals(expectedMinute, actual);
    }

    @Test
    public void testParsedSecondSegment()
    {
        int expected = now.getSecond();
        int actual = scheduleParser.getParsedSecondSegment();

        assertEquals(expected, actual);
    }



    @Test
    public void testNullTimeAndDate()
    {
        assertThrows(NullPointerException.class, () -> {
            ScheduleParserImpl scheduleParser = new ScheduleParserImpl(scheduleValidator);
        });
    }

    @AfterEach
    void tearDown() {
    }
}