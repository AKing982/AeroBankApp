package com.example.aerobankapp.scheduler;

import org.springframework.stereotype.Component;

@Component
public class CronExpressionBuilderImpl implements CronExpressionBuilder
{
    private final TriggerCriteria triggerCriteria;

    public CronExpressionBuilderImpl(TriggerCriteria triggerCriteria)
    {
        this.triggerCriteria = triggerCriteria;
    }


    @Override
    public String createDailyCron(TriggerCriteria triggerCriteria) {
        return null;
    }

    @Override
    public String createWeeklyCron(TriggerCriteria triggerCriteria) {
        return null;
    }

    @Override
    public String createBiWeeklyCron(TriggerCriteria triggerCriteria) {
        return null;
    }

    @Override
    public String createBiDailyCron(TriggerCriteria triggerCriteria) {
        return null;
    }

    @Override
    public String createMonthlyCron(TriggerCriteria triggerCriteria) {
        return null;
    }

    @Override
    public String getCronExpression()
    {
        return createCronExpression(triggerCriteria);
    }
}
