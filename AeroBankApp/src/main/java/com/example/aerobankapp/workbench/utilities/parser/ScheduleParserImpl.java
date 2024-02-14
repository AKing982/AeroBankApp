package com.example.aerobankapp.workbench.utilities.parser;

import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.scheduler.TriggerCriteria;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Component
@Getter
public class ScheduleParserImpl implements ScheduleParser
{
    private final SchedulerCriteria schedulerCriteria;
    private final ScheduleValidator scheduleValidator;

    @Autowired
    public ScheduleParserImpl(SchedulerCriteria schedulerCriteria, ScheduleValidator scheduleValidator)
    {
        initializeCriteria(schedulerCriteria, scheduleValidator);
        this.schedulerCriteria = schedulerCriteria;
        this.scheduleValidator = scheduleValidator;
    }

    private void initializeCriteria(SchedulerCriteria criteria, ScheduleValidator scheduleValidator)
    {
    //    Objects.requireNonNull(criteria, "Non Null ScheduleCriteria required");
      //  Objects.requireNonNull(scheduleValidator, "Non Null ScheduleValidator required");
     //   criteriaNullCheck(criteria);
    }

    private void criteriaNullCheck(SchedulerCriteria schedulerCriteria)
    {
        if(schedulerCriteria.getScheduledTime() == null || schedulerCriteria.getScheduledDate() == null || schedulerCriteria.getScheduleType() == null)
        {
            throw new NullPointerException("Found Null Scheduled Time or Null Scheduled Date.");
        }
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
        return getDate().getDayOfMonth();
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

    public TriggerCriteria buildTriggerCriteria()
    {
        ScheduleType interval = getValidatedInterval(schedulerCriteria.getScheduleType());

        int day = getValidatedDate(getParsedDaySegment());
        int month = getValidatedMonth(getParsedMonthSegment());
        int hour = getValidatedHour(getParsedHourSegment());
        int second = getParsedSecondSegment();
        int year = getValidatedYear(getParsedYearSegment());
        int minute = getValidatedMinute(getParsedMinuteSegment());

        return new TriggerCriteria(interval, minute, hour, day, month, year);
    }

    private ScheduleType getValidatedInterval(ScheduleType interval)
    {
        return scheduleValidator.validateInterval(interval);
    }

    private int getValidatedDate(int day)
    {
        return scheduleValidator.validateDay(day);
    }

    private int getValidatedMonth(int month)
    {
        return scheduleValidator.validateMonth(month);
    }

    private int getValidatedHour(int hour)
    {
        return scheduleValidator.validateHour(hour);
    }

    private int getValidatedYear(int year)
    {
        return scheduleValidator.validateYear(year);
    }

    private int getValidatedMinute(int minute)
    {
        return scheduleValidator.validateMinute(minute);
    }

    private LocalDate getDate()
    {
        return schedulerCriteria.getScheduledDate();
    }

    private LocalTime getTime()
    {
        return schedulerCriteria.getScheduledTime();
    }

}
