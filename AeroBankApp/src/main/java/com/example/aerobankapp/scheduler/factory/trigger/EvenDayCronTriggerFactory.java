package com.example.aerobankapp.scheduler.factory.trigger;

import com.example.aerobankapp.scheduler.TriggerCriteria;
import org.quartz.CronTrigger;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Component;

@Component
public class EvenDayCronTriggerFactory extends AbstractTriggerBase implements TriggerFactory
{


    public EvenDayCronTriggerFactory(TriggerCriteria triggerCriteria)
    {
        super(triggerCriteria);
    }


    @Override
    public Trigger createTrigger() {

        return TriggerBuilder.newTrigger()
                .withIdentity(getTriggerIdentity(triggerID, groupID))
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInHours(48)
                        .repeatForever())
                .build();
    }
}
