package com.example.aerobankapp.scheduler.factory.trigger;

import com.example.aerobankapp.scheduler.TriggerCriteria;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WeeklyTriggerFactory extends AbstractTriggerBase implements CronTriggerFactory
{
    @Autowired
    public WeeklyTriggerFactory(TriggerCriteria triggerCriteria)
    {
        super(triggerCriteria);
    }


    @Override
    public CronTrigger createCronTrigger()
    {
       TriggerCriteria triggerCriteria1 = super.getTriggerCriteria();
       int hour = triggerCriteria1.getHour();
       int minute = triggerCriteria1.getMinute();
       int day = triggerCriteria1.getDay();

       CronTrigger weeklyTrigger = TriggerBuilder.newTrigger()
                   .withIdentity(getTriggerIdentity(triggerID, groupID))
                   .withSchedule(CronScheduleBuilder.weeklyOnDayAndHourAndMinute(day, hour, minute))
                   .build();
       return weeklyTrigger;
    }
}
