package com.example.aerobankapp.scheduler;

import java.util.List;

public interface CronBuilderFactory
{
    List<String> createCron(TriggerCriteria triggerCriteria);
}
