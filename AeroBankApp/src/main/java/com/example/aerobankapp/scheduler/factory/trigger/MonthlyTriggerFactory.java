package com.example.aerobankapp.scheduler.factory.trigger;

import com.example.aerobankapp.scheduler.TriggerCriteria;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.Getter;
import org.quartz.CronTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class MonthlyTriggerFactory extends AbstractTriggerBase implements CronTriggerFactory
{
    private final AeroLogger aeroLogger = new AeroLogger(MonthlyTriggerFactory.class);
    private final int RANDOM_SEED = 42;
    private final int INTERVAL = 24;

    @Autowired
    public MonthlyTriggerFactory(TriggerCriteria triggerCriteria)
    {
        super(triggerCriteria);
    }



    @Override
    public CronTrigger createCronTrigger()
    {
        CronTrigger monthlyTrigger = null;
        try
        {

        }catch(Exception e)
        {

        }
        return monthlyTrigger;
    }
}
