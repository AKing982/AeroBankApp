package com.example.aerobankapp.workbench.utilities.parser;

import java.time.LocalDate;

public interface ScheduleParser
{
    int getParsedYearSegment();
    int getParsedMonthSegment();
    int getParsedDaySegment();
    int getParsedSecondSegment();
    int getParsedHourSegment();
    int getParsedMinuteSegment();
    int getParsedInterval();
}
