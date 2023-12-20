package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.scheduler.factory.trigger.AbstractTriggerBase;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Component
@Getter
public class CustomCronBuilderFactory implements CCronBuilderFactory
{
    private StringBuilder cronExpression = new StringBuilder();
    private AeroLogger aeroLogger = new AeroLogger(CustomCronBuilderFactory.class);
    private TriggerCriteria triggerCriteria;
    private String customCronExpression;
    @Autowired
    public CustomCronBuilderFactory(TriggerCriteria triggerCriteria)
    {
        if(triggerCriteria != null)
        {
            this.triggerCriteria = triggerCriteria;
           // this.customCronExpression = getCronExpression(triggerCriteria);
        }
    }

    public String getCronExpression(TriggerCriteria triggerCriteria)
    {
        int interval = triggerCriteria.getInterval();
        int min = triggerCriteria.getMinute();
        int hour = triggerCriteria.getHour();
        int day = triggerCriteria.getDay();
        int month = triggerCriteria.getMonth();
        int year = triggerCriteria.getYear();

        return createCron(interval, min, hour, day, month, year);
    }


    @Override
    public String createCron(int interval, int min, int hour, int day, int month, int year)
    {
        StringBuilder cronExpression = new StringBuilder();

        if(interval < 1 || min < 0 || hour < 0 || day < 0 || month < 0 || year < 0)
        {
            throw new IllegalArgumentException();
        }

        cronExpression.append("0 ");
        cronExpression.append(min);
        cronExpression.append(" ");
        cronExpression.append(hour);
        cronExpression.append(" ");
        cronExpression.append(day);
        cronExpression.append(" ");
        cronExpression.append(month);
        cronExpression.append(" ? ");
        cronExpression.append(year);

        return cronExpression.toString();
    }
}
