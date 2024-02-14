package com.example.aerobankapp.scheduler.trigger;

import com.example.aerobankapp.scheduler.CronExpressionBuilder;
import com.example.aerobankapp.scheduler.TriggerCriteria;
import lombok.Getter;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

import static org.quartz.DateBuilder.dateOf;

@Service
@Getter
public class SchedulerTriggerImpl implements SchedulerTriggerModel {
    private final TriggerCriteria triggerCriteria;
    private final CronExpressionBuilder cronExpressionBuilder;
    private final Logger LOGGER = LoggerFactory.getLogger(SchedulerTriggerImpl.class);

    @Autowired
    public SchedulerTriggerImpl(TriggerCriteria triggerCriteria, CronExpressionBuilder cronExpressionBuilder) {
        Objects.requireNonNull(triggerCriteria, "Non Null Trigger Criteria required");
        Objects.requireNonNull(cronExpressionBuilder, "Non Null CronExpression required");
        this.triggerCriteria = triggerCriteria;
        this.cronExpressionBuilder = cronExpressionBuilder;
    }

    private int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(Integer.MAX_VALUE) + 1;
    }

    @Override
    public CronTrigger getRunOnceTrigger() {
        String onceCronExpression = getCronExpressionBuilder().createCronExpression();
        return createCronTrigger(onceCronExpression, "RunOnceTrigger");
    }

    @Override
    public CronTrigger getDailyTrigger() {
        String dailyCron = getCronExpressionBuilder().createCronExpression();
        return createCronTrigger(dailyCron, "DailyTrigger");
    }

    @Override
    public SimpleTrigger getDailyTwoDayTrigger() {
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, getTriggerCriteria().getHour());
        startTime.set(Calendar.MINUTE, getTriggerCriteria().getMinute());
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);

        // Create the interval
        long intervalInHours = 48L;

        SimpleTrigger biDailyTrigger = null;
        try {
            biDailyTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(TriggerKey.triggerKey(String.valueOf(generateRandomNumber()), String.valueOf(generateRandomNumber())))
                    .startAt(startTime.getTime())
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInHours((int) intervalInHours)
                            .repeatForever())
                    .build();

        } catch (Exception e) {
            LOGGER.error("An exception has occurred creating the bi-daily trigger: ", e);
        }

        return biDailyTrigger;
    }

    @Override
    public CronTrigger getWeeklyTrigger() {
        String weeklyCron = getCronExpressionBuilder().createCronExpression();
        return createCronTrigger(weeklyCron, "WeeklyTrigger");
    }

    @Override
    public CronTrigger getMonthlyTrigger() {
        String monthlyCron = getCronExpressionBuilder().createCronExpression();
        return createCronTrigger(monthlyCron, "MonthlyTrigger");
    }

    private CronTrigger createCronTrigger(String cronExpression, String triggerIdentity)
    {
        try
        {
            return TriggerBuilder.newTrigger()
                    .withIdentity(TriggerKey.triggerKey(triggerIdentity, String.valueOf(generateRandomNumber())))
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                    .build();

        }catch(Exception e)
        {
            LOGGER.error("An exception has occurred building trigger: ", e);
            return null;
        }
    }
}
