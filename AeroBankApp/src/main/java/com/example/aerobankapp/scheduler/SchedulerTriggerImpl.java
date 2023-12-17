package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.Getter;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Getter
public class SchedulerTriggerImpl implements SchedulerTriggerModel
{
    private static String triggerID = UUID.randomUUID().toString();
    private static String groupID = UUID.randomUUID().toString();
    private AeroLogger aeroLogger = new AeroLogger(SchedulerTriggerImpl.class);
    private TriggerCriteria triggerCriteria;

    @Autowired
    public SchedulerTriggerImpl(TriggerCriteria triggerCriteria)
    {
        this.triggerCriteria = triggerCriteria;
    }

    public int getDay()
    {
        return getTriggerCriteria().getDay();
    }

    public int getMonth()
    {
        return getTriggerCriteria().getMonth();
    }

    public int getYear()
    {
        return getTriggerCriteria().getYear();
    }

    public int getMinute()
    {
        return getTriggerCriteria().getMinute();
    }

    public int getHour()
    {
        return getTriggerCriteria().getHour();
    }

    public int getInterval()
    {
        return getTriggerCriteria().getInterval();
    }


    @Override
    public Trigger getDailyTrigger()
    {
        return null;
    }

    @Override
    public Trigger getBiWeeklyTrigger()
    {
        return null;
    }

    @Override
    public Trigger getWeeklyTrigger()
    {
        return null;
    }

    @Override
    public Trigger getMonthlyTrigger()
    {
        return null;
    }

    @Override
    public CronTrigger getEvenDayTrigger()
    {
        return null;
    }

    @Override
    public CronTrigger getCustomTrigger()
    {

        return null;
    }
}
