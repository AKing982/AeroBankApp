package com.example.aerobankapp.scheduler;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Getter
public class CronExpressionBuilderImpl implements CronExpressionBuilder
{
    private final TriggerCriteria triggerCriteria;
    private Logger LOGGER = LoggerFactory.getLogger(CronExpressionBuilderImpl.class);


    public CronExpressionBuilderImpl() {
        this.triggerCriteria = new TriggerCriteria();
        Objects.requireNonNull(triggerCriteria, "TriggerCriteria cannot be null");
    }

    @Override
    public String createCronExpression()
    {
        ScheduleType interval = getTriggerCriteria().getInterval();
        LOGGER.debug("Interval Selected: " + interval);
        return switch (interval) {
            case Once -> {
                LOGGER.debug("Scheduling Cron for Once");
                yield getOnceCronSchedule(triggerCriteria);
            }
            case Daily -> getDailyCronSchedule(triggerCriteria);
            case Weekly -> getWeeklyCronSchedule(triggerCriteria);
            case Monthly -> getMonthlyCronSchedule(triggerCriteria);
            default -> throw new IllegalArgumentException("Unsupported Schedule Type: " + interval);
        };
    }

    private String getOnceCronSchedule(final TriggerCriteria triggerCriteria)
    {
        return buildBaseCronExpression(triggerCriteria) + " " +
                triggerCriteria.getDay() + " " +
                triggerCriteria.getMonth() + " ? " +
                triggerCriteria.getYear();
    }

    private String getDailyCronSchedule(final TriggerCriteria triggerCriteria)
    {
        return buildBaseCronExpression(triggerCriteria) + " * * ? *";
    }

    private String getWeeklyCronSchedule(final TriggerCriteria triggerCriteria)
    {
        return buildBaseCronExpression(triggerCriteria) + " ? * " +
                triggerCriteria.getDay() + " *";
    }

    private String getMonthlyCronSchedule(final TriggerCriteria triggerCriteria)
    {
        return buildBaseCronExpression(triggerCriteria) + " " + triggerCriteria.getDay() +
                " * ? *";
    }

    private String buildBaseCronExpression(final TriggerCriteria triggerCriteria)
    {
        return triggerCriteria.getSecond() + " " +
                triggerCriteria.getMinute() + " " +
                triggerCriteria.getHour();
    }
}
