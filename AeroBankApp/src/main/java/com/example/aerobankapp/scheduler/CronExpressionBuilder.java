package com.example.aerobankapp.scheduler;

public interface CronExpressionBuilder
{
    String createDailyCron(TriggerCriteria triggerCriteria);

    String createWeeklyCron(TriggerCriteria triggerCriteria);

    String createBiWeeklyCron(TriggerCriteria triggerCriteria);

    String createBiDailyCron(TriggerCriteria triggerCriteria);

    String createMonthlyCron(TriggerCriteria triggerCriteria);

    String getCronExpression();
}
