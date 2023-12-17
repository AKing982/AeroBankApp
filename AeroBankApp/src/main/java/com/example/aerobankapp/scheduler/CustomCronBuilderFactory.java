package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class CustomCronBuilderFactory implements CronBuilderFactory
{
    private StringBuilder cronExpression = new StringBuilder();
    private AeroLogger aeroLogger = new AeroLogger(CustomCronBuilderFactory.class);


    @Override
    public String createCron(int interval, int min, int hour, int day, int month, int year) {

        if(interval == 0 || min == 0 || hour == 0 || day == 0 || month == 0 || year == 0)
        {
            throw new IllegalArgumentException();
        }

        switch(interval)
        {
            case 1:
                cronExpression = new StringBuilder();
                cronExpression.append("0 ");
                cronExpression.append(min);
                cronExpression.append(" ");
                cronExpression.append(hour);
                cronExpression.append(" ");
                cronExpression.append(day);
                cronExpression.append(" ");
                cronExpression.append(month);
                cronExpression.append(" ?");
                cronExpression.append(" ");
                cronExpression.append(year);
                return cronExpression.toString();
            case 5:
            case 10:
            case 15:
            case 20:
            case 25:
            case 30:
            case 35:
            case 40:
            case 45:
            case 50:
            case 55:
            case 60:
                StringBuilder sb2 = new StringBuilder();
                sb2.append("0/");
                sb2.append(interval);
                sb2.append(" ");
                sb2.append(min);
                sb2.append(" ");
                sb2.append(hour);
                sb2.append(" ");
                sb2.append(day);
                sb2.append(" ");
                sb2.append(month);
                sb2.append(" ");
                sb2.append("?");
                sb2.append(" ");
                sb2.append(year);
                return sb2.toString();
            default:
                return null;
        }
    }
}
