package com.example.aerobankapp.scheduler.factory.trigger;

import com.example.aerobankapp.scheduler.CronBuilderFactory;
import com.example.aerobankapp.scheduler.MonthlyCronBuilderFactory;
import com.example.aerobankapp.scheduler.TriggerCriteria;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.Getter;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.TriggerBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class MonthlyTriggerFactory extends AbstractTriggerBase implements CronTriggerFactory
{
    private final AeroLogger aeroLogger = new AeroLogger(MonthlyTriggerFactory.class);
    private final CronBuilderFactory cronBuilderFactory;
    private List<String> cronSchedules = new ArrayList<>();
    private final int RANDOM_SEED = 42;
    private final int INTERVAL = 24;

    @Autowired
    public MonthlyTriggerFactory(final TriggerCriteria triggerCriteria)
    {
        super(triggerCriteria);
        this.cronBuilderFactory = new MonthlyCronBuilderFactory(triggerCriteria);
    }

    public List<String> getMonthlyCronSchedulesList()
    {
        TriggerCriteria triggerCriteria1 = super.getTriggerCriteria();
        return getCronBuilderFactory().createCron(triggerCriteria1);
    }

    private String getCronExpression()
    {
        TriggerCriteria triggerCriteria1 = super.getTriggerCriteria();
        return cronBuilderFactory.createCron(triggerCriteria1).get(0);
    }

    public CronScheduleBuilder getCronSchedule()
    {
        final String cronExpression = getCronExpression();
        return CronScheduleBuilder.cronSchedule(cronExpression);
    }


    @Override
    public CronTrigger createCronTrigger()
    {
        CronTrigger monthlyTrigger = null;
        try
        {
            monthlyTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerID, groupID)
                    .withSchedule(getCronSchedule())
                    .build();

        }catch(Exception e)
        {

        }
        return monthlyTrigger;
    }
}
