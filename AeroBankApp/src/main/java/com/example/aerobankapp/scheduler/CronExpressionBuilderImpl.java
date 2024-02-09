package com.example.aerobankapp.scheduler;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CronExpressionBuilderImpl implements CronExpressionBuilder
{
    private final TriggerCriteria triggerCriteria;

    public CronExpressionBuilderImpl(TriggerCriteria triggerCriteria)
    {
        Objects.requireNonNull(triggerCriteria, "TriggerCriteria cannot be null");
        this.triggerCriteria = triggerCriteria;
    }

    @Override
    public String createCronExpression()
    {
        ScheduleType interval = triggerCriteria.getInterval();
        switch(interval)
        {
            case ONCE:
                return getOnceCronSchedule(triggerCriteria);
            case DAILY:
                return getDailyCronSchedule(triggerCriteria);
            case WEEKLY:
                return getWeeklyCronSchedule(triggerCriteria);
            case MONTHLY:
                return getMonthlyCronSchedule(triggerCriteria);

        }
        return null;
    }

    private String getOnceCronSchedule(final TriggerCriteria triggerCriteria)
    {
        return buildBaseCronExpression(triggerCriteria) + " " +
                triggerCriteria.getDay() + " " +
                triggerCriteria.getMonth() + " ? " +
                triggerCriteria.getYear();
    }

    private String getDailyCronSchedule(final TriggerCriteria triggerCriteria)
    {
        return buildBaseCronExpression(triggerCriteria) + " * * ? *";
    }

    private String getWeeklyCronSchedule(final TriggerCriteria triggerCriteria)
    {
        return buildBaseCronExpression(triggerCriteria) + " ? * " +
                triggerCriteria.getDay() + " *";
    }

    private String getMonthlyCronSchedule(final TriggerCriteria triggerCriteria)
    {
        return buildBaseCronExpression(triggerCriteria) + " " + triggerCriteria.getDay() +
                " * ? *";
    }

    private String buildBaseCronExpression(final TriggerCriteria triggerCriteria)
    {
        return triggerCriteria.getSecond() + " " +
                triggerCriteria.getMinute() + " " +
                triggerCriteria.getHour();
    }
}
