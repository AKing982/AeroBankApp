package com.example.aerobankapp.workbench.utilities.parser;

import com.example.aerobankapp.exceptions.InvalidScheduledHourException;
import com.example.aerobankapp.exceptions.InvalidScheduledIntervalException;
import com.example.aerobankapp.exceptions.InvalidScheduledMinuteException;
import com.example.aerobankapp.exceptions.InvalidScheduledYearException;
import com.example.aerobankapp.scheduler.ScheduleType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduleValidatorImpl implements ScheduleValidator
{
    public ScheduleValidatorImpl()
    {
        // Empty Constructor
    }

    @Override
    public int validateDay(final int day)
    {
       if(day > 30)
       {
           return ((day - 1) % 31) + 1;
       }
       else if(day <= 0)
       {
           return 1;
       }
       return day;
    }

    @Override
    public int validateMonth(final int month) {
        if(month > 12)
        {
            return ((month - 1) % 12) + 1;
        }
        else if(month <= 0)
        {
            return 1;
        }
        return month;
    }

    @Override
    public int validateYear(final int year) {
        if(!getYearLengthFourBoolean(year))
        {
            throw new InvalidScheduledYearException("Invalid Year entered");
        }
        return year;
    }

    @Override
    public int validateMinute(final int minute) {
        if(minute > 59)
        {
            return ((minute - 1) % 60) + 1;
        }
        else if(minute <= 0)
        {
            throw new InvalidScheduledMinuteException("Invalid Minute has been entered.");
        }
        return minute;
    }

    @Override
    public int validateHour(final int hour)
    {
        if(hour > 23)
        {
            return ((hour - 1) % 24) + 1;
        }
        if(hour <= 0)
        {
            throw new InvalidScheduledHourException("Invalid Hour has been entered.");
        }
        return hour;
    }

    private boolean getYearLengthFourBoolean(int year)
    {
       return String.valueOf(Math.abs(year)).length() == 4;
    }

    @Override
    public ScheduleType validateInterval(final ScheduleType interval)
    {
        List<ScheduleType> scheduleTypeList = createScheduleTypeList();

        if(!scheduleTypeList.contains(interval))
        {
            throw new InvalidScheduledIntervalException("Invalid Interval found.");
        }
        return interval;
    }

    private List<ScheduleType> createScheduleTypeList()
    {
        List<ScheduleType> scheduleTypeList = new ArrayList<>();
        scheduleTypeList.add(ScheduleType.MONTHLY);
        scheduleTypeList.add(ScheduleType.NONE);
        scheduleTypeList.add(ScheduleType.DAILY);
        scheduleTypeList.add(ScheduleType.WEEKLY);
        scheduleTypeList.add(ScheduleType.BIDAILY);
        scheduleTypeList.add(ScheduleType.BIWEEKLY);
        scheduleTypeList.add(ScheduleType.CUSTOM);
        scheduleTypeList.add(ScheduleType.ONCE);

        return scheduleTypeList;
    }
}
