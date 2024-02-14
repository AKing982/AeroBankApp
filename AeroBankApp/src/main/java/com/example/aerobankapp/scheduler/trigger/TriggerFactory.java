package com.example.aerobankapp.scheduler.trigger;

import com.example.aerobankapp.scheduler.ScheduleType;
import lombok.Getter;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TriggerFactory
{
    private SchedulerTriggerModel schedulerTriggerModel;

    @Autowired
    public TriggerFactory(SchedulerTriggerModel schedulerTriggerModel)
    {
        this.schedulerTriggerModel = schedulerTriggerModel;
    }

    public Trigger getTriggerInstance(ScheduleType type)
    {
        switch(type)
        {
            case WEEKLY:
                return getSchedulerTriggerModel().getWeeklyTrigger();
            case ONCE:
                return schedulerTriggerModel.getRunOnceTrigger();
            case MONTHLY:
                return schedulerTriggerModel.getMonthlyTrigger();
            case DAILY:
                return schedulerTriggerModel.getDailyTrigger();
            default:
                throw new IllegalArgumentException("Invalid Schedule Type found.");
        }
    }
}
