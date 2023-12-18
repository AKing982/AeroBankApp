package com.example.aerobankapp.scheduler.factory.trigger;

import com.example.aerobankapp.scheduler.TriggerCriteria;
import org.quartz.CronTrigger;
import org.springframework.stereotype.Component;

@Component
public class EvenDayCronTriggerFactory extends AbstractTriggerBase implements CronTriggerFactory
{
    public EvenDayCronTriggerFactory(TriggerCriteria triggerCriteria)
    {
        super(triggerCriteria);
    }


    @Override
    public CronTrigger createCronTrigger()
    {
        return null;
    }
}
