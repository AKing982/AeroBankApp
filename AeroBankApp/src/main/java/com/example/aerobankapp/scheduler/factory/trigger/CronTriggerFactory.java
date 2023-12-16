package com.example.aerobankapp.scheduler.factory.trigger;

import org.quartz.CronTrigger;

public interface CronTriggerFactory
{
    CronTrigger createCronTrigger();
}
