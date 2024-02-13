package com.example.aerobankapp.scheduler.trigger;

import org.quartz.CronTrigger;
import org.quartz.SimpleTrigger;

public interface SchedulerTriggerModel
{
    CronTrigger getRunOnceTrigger();
    CronTrigger getDailyTrigger();
    SimpleTrigger getDailyTwoDayTrigger();
    CronTrigger getWeeklyTrigger();
    CronTrigger getMonthlyTrigger();
}
