package com.example.aerobankapp.workbench.utilities;

import com.example.aerobankapp.entity.SchedulerCriteriaEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.scheduler.TriggerCriteria;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;

import java.time.LocalDate;
import java.time.LocalTime;

public final class SysBuilderUtils
{

    public static SchedulerCriteria buildSimpleSchedulerCriteria(LocalTime time, LocalDate date, ScheduleType type)
    {
        SchedulerCriteria schedulerCriteria = new SchedulerCriteria();
        schedulerCriteria.setScheduledTime(time);
        schedulerCriteria.setScheduledDate(date);
        schedulerCriteria.setScheduleType(type);
        schedulerCriteria.setPriority(1);
        schedulerCriteria.setCreatedAt(LocalDate.now());
        return schedulerCriteria;
    }

    public static SchedulerCriteria buildSchedulerCriteria(SchedulerCriteriaEntity schedulerCriteriaEntity)
    {
        SchedulerCriteria schedulerCriteria = new SchedulerCriteria();
        schedulerCriteria.setScheduledTime(schedulerCriteriaEntity.getScheduledTime());
        schedulerCriteria.setScheduledDate(schedulerCriteriaEntity.getScheduledDate());
        schedulerCriteria.setScheduleType(schedulerCriteriaEntity.getScheduleType());
        schedulerCriteria.setPriority(1);
        schedulerCriteria.setCreatedAt(LocalDate.now());
        return schedulerCriteria;
    }

    public static TriggerCriteria buildTriggerCriteria(ScheduleType interval, int day, int month, int year, int minute, int hour, int second)
    {
        TriggerCriteria triggerCriteria = new TriggerCriteria();
        triggerCriteria.setDay(day);
        triggerCriteria.setHour(hour);
        triggerCriteria.setMinute(minute);
        triggerCriteria.setSecond(second);
        triggerCriteria.setInterval(interval);
        triggerCriteria.setMonth(month);
        triggerCriteria.setYear(year);
        return triggerCriteria;
    }


    public static SchedulerCriteriaEntity buildSchedulerCriteriaEntity(int id, int userID, LocalTime scheduledTime, LocalDate scheduledDate, ScheduleType scheduleType)
    {
        SchedulerCriteriaEntity schedulerCriteria = new SchedulerCriteriaEntity();
        schedulerCriteria.setScheduleType(scheduleType);
        schedulerCriteria.setSchedulerCriteriaID(id);
        schedulerCriteria.setScheduledDate(scheduledDate);
        schedulerCriteria.setScheduledTime(scheduledTime);
        schedulerCriteria.setStatus(Status.ACTIVE);
        schedulerCriteria.setLastRunTime(LocalDate.of(2024, 2, 5));
        schedulerCriteria.setNextRunTime(LocalDate.now());
        schedulerCriteria.setSchedulerUser(UserEntity.builder().userID(userID).build());
        return schedulerCriteria;
    }
}
