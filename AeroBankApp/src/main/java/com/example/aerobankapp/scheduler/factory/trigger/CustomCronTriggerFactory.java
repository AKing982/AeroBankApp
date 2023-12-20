package com.example.aerobankapp.scheduler.factory.trigger;

import com.example.aerobankapp.exceptions.InvalidCronExpressionException;
import com.example.aerobankapp.exceptions.NullTriggerCriteriaException;
import com.example.aerobankapp.scheduler.CCronBuilderFactory;
import com.example.aerobankapp.scheduler.CronBuilderFactory;
import com.example.aerobankapp.scheduler.CustomCronBuilderFactory;
import com.example.aerobankapp.scheduler.TriggerCriteria;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.Getter;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

import static org.quartz.DateBuilder.dateOf;

@Component
@Getter
public class CustomCronTriggerFactory extends AbstractTriggerBase implements CronTriggerFactory
{
    private AeroLogger aeroLogger = new AeroLogger(CustomCronTriggerFactory.class);
    private final int SEED_RANDOM = 42;
    private final CCronBuilderFactory cronBuilderFactory = new CustomCronBuilderFactory();

    @Autowired
    public CustomCronTriggerFactory(@Qualifier("triggerCriteria") TriggerCriteria triggerCriteria)
    {
        super(triggerCriteria);
    }

    @Override
    public CronTrigger createCronTrigger() {
        CronTrigger customTrigger = null;
        aeroLogger.info("Building Custom Trigger");
        try
        {
            aeroLogger.info("Building Cron Expression ");
            String cronExpression = getCronExpression(triggerCriteria);
            aeroLogger.info("Cron Expression: " + cronExpression);

            aeroLogger.info("Trigger");
            customTrigger = getCustomCronTrigger(cronExpression, triggerCriteria);

        }
        catch (Exception e)
        {
            aeroLogger.error("Error Building Custom Cron Trigger: ", e);
        }
        return customTrigger;
    }

    public String getCronExpression(TriggerCriteria triggerCriteria)
    {
        triggerCriteriaNullCheck(triggerCriteria);
        int interval = triggerCriteria.getInterval();
        int minute = triggerCriteria.getMinute();
        int hour = triggerCriteria.getHour();
        int day = triggerCriteria.getDay();
        int month = triggerCriteria.getMonth();
        int year = triggerCriteria.getYear();

        return cronBuilderFactory.createCron(interval, minute, hour, day, month, year);
    }

    private int getRandomID()
    {
        Random random = new Random(SEED_RANDOM);
        return random.nextInt(Integer.MAX_VALUE) + 1;
    }


    private void nullCheck(String cronExpression, TriggerCriteria triggerDetail)
    {
        if(cronExpression.equals(" ") || cronExpression.equals(""))
        {
            throw new InvalidCronExpressionException("Invalid Cron Expression has been entered!");
        }
        triggerCriteriaNullCheck(triggerDetail);
    }


    private CronScheduleBuilder getCronSchedule(String cronExpression)
    {
        return CronScheduleBuilder.cronSchedule(cronExpression);
    }

    private Date startDate(TriggerCriteria triggerCriteria)
    {
        return dateOf(triggerCriteria.getHour(), triggerCriteria.getMinute(), 0, triggerCriteria.getDay(), triggerCriteria.getMonth(), triggerCriteria.getYear());
    }

    private TriggerKey getTriggerKey(int id)
    {
        return TriggerKey.triggerKey(String.valueOf(id), String.valueOf(id));
    }

    public CronTrigger getCustomCronTrigger(final String cronExpression, final TriggerCriteria triggerCriteria)
    {
        nullCheck(cronExpression, triggerCriteria);

        return TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(getRandomID()))
                .withSchedule(getCronSchedule(cronExpression))
                .startAt(startDate(triggerCriteria))
                .build();
    }
}
