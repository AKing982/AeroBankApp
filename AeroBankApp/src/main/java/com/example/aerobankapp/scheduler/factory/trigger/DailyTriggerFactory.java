package com.example.aerobankapp.scheduler.factory.trigger;

import com.example.aerobankapp.exceptions.NullTriggerCriteriaException;
import com.example.aerobankapp.scheduler.TriggerCriteria;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

import static org.quartz.DateBuilder.tomorrowAt;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Component
@Getter
public class DailyTriggerFactory extends AbstractTriggerBase implements TriggerFactory
{
    private final AeroLogger aeroLogger = new AeroLogger(DailyTriggerFactory.class);
    private final int RANDOM_SEED = 42;
    private final int INTERVAL = 24;

    @Autowired
    public DailyTriggerFactory(TriggerCriteria triggerCriteria)
    {
        super(triggerCriteria);
    }

    public TriggerKey getIdentity(String triggerID, String groupID)
    {
        return getTriggerIdentity(triggerID, groupID);
    }

    public Date getStartDate(int hour, int minute)
    {
        return tomorrowAt(hour, minute, 0);
    }

    public SimpleScheduleBuilder getSimpleScheduleBuilder(int interval)
    {
        return simpleSchedule().withIntervalInHours(interval).repeatForever();
    }

    @Override
    public Trigger createTrigger()
    {
       triggerCriteriaNullCheck(triggerCriteria);

       Trigger dailyTrigger = null;
       try
       {
           dailyTrigger = TriggerBuilder.newTrigger()
                   .withIdentity(getTriggerIdentity(triggerID, groupID))
                   .withSchedule(getSimpleScheduleBuilder(INTERVAL))
                   .startAt(getStartDate(triggerCriteria.getHour(), triggerCriteria.getMinute()))
                   .build();

       }catch(Exception e)
       {
            aeroLogger.error("Unable to create Daily Trigger: ", e);
       }
        return dailyTrigger;
    }
}
