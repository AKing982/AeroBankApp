package com.example.aerobankapp.scheduler.factory.trigger;

import com.example.aerobankapp.scheduler.CronBuilderFactory;
import com.example.aerobankapp.scheduler.CustomCronBuilderFactory;
import com.example.aerobankapp.scheduler.TriggerDetail;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.Getter;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Random;

import static org.quartz.DateBuilder.dateOf;

@Component
@Getter
public class CustomCronTriggerFactory implements CronTriggerFactory
{
    private AeroLogger aeroLogger = new AeroLogger(CustomCronTriggerFactory.class);
    private final int SEED_RANDOM = 42;
    private final CronBuilderFactory cronBuilderFactory = new CustomCronBuilderFactory();
    private final TriggerDetail triggerDetail;

    @Autowired
    public CustomCronTriggerFactory(@Qualifier("triggerDetail") TriggerDetail triggerDetail)
    {
        this.triggerDetail = triggerDetail;
    }

    @Override
    public CronTrigger createCronTrigger() {
        CronTrigger customTrigger = null;
        aeroLogger.info("Building Custom Trigger");
        try
        {
            Random random = new Random(SEED_RANDOM);
            int randomID = random.nextInt(Integer.MAX_VALUE) + 1;
            aeroLogger.info("Building Cron Expression ");
            String cronExpression = cronBuilderFactory.createCron(triggerDetail.getInterval(), triggerDetail.getMinute(), triggerDetail.getHour(), triggerDetail.getDay(), triggerDetail.getMonth(), triggerDetail.getYear());
            aeroLogger.info("Cron Expression: " + cronExpression);

            aeroLogger.info("Trigger");
            customTrigger = TriggerBuilder.newTrigger().withIdentity(TriggerKey.triggerKey(String.valueOf(randomID), String.valueOf(randomID)))
                   .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                   .startAt(dateOf(triggerDetail.getHour(), triggerDetail.getMinute(), 0, triggerDetail.getDay(), triggerDetail.getMonth(), triggerDetail.getYear()))
                  .build();

        }
        catch (Exception e)
        {
            aeroLogger.error("Error Building Custom Cron Trigger: ", e);
        }
        return customTrigger;
    }
}
