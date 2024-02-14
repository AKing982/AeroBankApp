package com.example.aerobankapp.scheduler.factory;

import com.example.aerobankapp.scheduler.CronExpressionBuilderImpl;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.scheduler.TriggerCriteria;
import com.example.aerobankapp.scheduler.trigger.SchedulerTriggerImpl;
import lombok.Getter;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TriggerFactoryImpl
{
    private TriggerCriteria triggerCriteria;
    private SchedulerTriggerImpl schedulerTrigger;
    private CronExpressionBuilderImpl cronExpressionBuilder;

    private ScheduleType scheduleType;

    @Autowired
    public TriggerFactoryImpl(TriggerCriteria criteria)
    {
        this.cronExpressionBuilder = new CronExpressionBuilderImpl(criteria);
        this.schedulerTrigger = new SchedulerTriggerImpl(criteria, getCronExpressionBuilder());
        this.scheduleType = criteria.getInterval();
    }

    public Trigger getTriggerInstance()
    {
        switch(getScheduleType())
        {
            case Once:
                return getSchedulerTrigger().getRunOnceTrigger();
            case Daily:
                return getSchedulerTrigger().getDailyTrigger();
            case Monthly:
                return getSchedulerTrigger().getMonthlyTrigger();
            case Weekly:
                return getSchedulerTrigger().getWeeklyTrigger();
            default:
                throw new IllegalArgumentException("Invalid Schedule Type found.");
        }
    }
}
