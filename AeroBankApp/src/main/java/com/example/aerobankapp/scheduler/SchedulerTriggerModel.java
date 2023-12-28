package com.example.aerobankapp.scheduler;

import org.quartz.CronTrigger;
import org.quartz.Trigger;

@Deprecated
public interface SchedulerTriggerModel
{
    Trigger getDailyTrigger();
    Trigger getBiWeeklyTrigger();
    Trigger getWeeklyTrigger();
    Trigger getMonthlyTrigger();
    CronTrigger getEvenDayTrigger();
    CronTrigger getCustomTrigger();
}
