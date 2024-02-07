package com.example.aerobankapp.workbench.utilities.parser;

import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.scheduler.TriggerCriteria;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
@Getter
public class ScheduleParserImpl implements ScheduleParser
{
    private final SchedulerCriteria schedulerCriteria;

    @Autowired
    public ScheduleParserImpl(SchedulerCriteria schedulerCriteria)
    {
        Objects.requireNonNull(schedulerCriteria, "Non Null Criteria required.");
        this.schedulerCriteria = schedulerCriteria;
    }

    private void nullCheck(SchedulerCriteria schedulerCriteria1)
    {

    }

    @Override
    public int getParsedYearSegment() {
         return getDate().getYear();
    }

    @Override
    public int getParsedMonthSegment() {
        return getDate().getMonthValue();
    }

    @Override
    public int getParsedDaySegment() {
        return getTime().getDayOfMonth();
    }

    @Override
    public int getParsedSecondSegment()
    {
        return getTime().getSecond();
    }

    @Override
    public int getParsedHourSegment() {
        return getTime().getHour();
    }

    @Override
    public int getParsedMinuteSegment()
    {
        return getTime().getMinute();
    }

    @Override
    public int getParsedInterval() {
        ScheduleType interval = schedulerCriteria.getScheduleType();
        return switch (interval) {
            case ONCE, DAILY -> 1;
            case WEEKLY -> 7;
            case BIDAILY -> 2;
            case MONTHLY -> 30;
            case BIWEEKLY -> 14;
            default -> 0;
        };

    }

    public TriggerCriteria buildTriggerCriteria()
    {
        int interval = getParsedInterval();
        int day = getParsedDaySegment();
        int month = getParsedMonthSegment();
        int hour = getParsedHourSegment();
        int second = getParsedSecondSegment();
        int year = getParsedYearSegment();
        int minute = getParsedMinuteSegment();

        return new TriggerCriteria(interval, minute, hour, day, month, year);
    }

    private LocalDate getDate()
    {
        return schedulerCriteria.getScheduledDate();
    }

    private LocalDateTime getTime()
    {
        return schedulerCriteria.getScheduledTime();
    }

}
