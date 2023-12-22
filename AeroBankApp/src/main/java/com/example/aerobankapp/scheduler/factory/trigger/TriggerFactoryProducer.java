package com.example.aerobankapp.scheduler.factory.trigger;

import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.scheduler.TriggerCriteria;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TriggerFactoryProducer
{
    private TriggerFactory triggerFactory;

    @Autowired
    private TriggerCriteria triggerCriteria;

    public Trigger getTriggerFactory(ScheduleType type)
    {
        switch(type)
        {
            case BIWEEKLY:
                triggerFactory = new BiWeeklyTriggerFactory();
                return triggerFactory.createTrigger();
            case WEEKLY:
                triggerFactory = (TriggerFactory) new WeeklyTriggerFactory(triggerCriteria);
                return triggerFactory.createTrigger();
            case MONTHLY:
                triggerFactory = (TriggerFactory) new MonthlyTriggerFactory(triggerCriteria);
                return triggerFactory.createTrigger();
            case CUSTOM:
                triggerFactory = (TriggerFactory) new CustomCronTriggerFactory(triggerCriteria);
                return triggerFactory.createTrigger();
            case EVERY_TWO_DAYS:
                triggerFactory = (TriggerFactory) new EvenDayCronTriggerFactory(triggerCriteria);
                return triggerFactory.createTrigger();
            case DAILY:
                triggerFactory = new DailyTriggerFactory(triggerCriteria);
                return triggerFactory.createTrigger();
        }
        return null;
    }
}
