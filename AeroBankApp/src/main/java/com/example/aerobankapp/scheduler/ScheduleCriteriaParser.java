package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Getter
public class ScheduleCriteriaParser
{
    private SchedulerCriteria scheduleCriteria;

    @Autowired
    public ScheduleCriteriaParser(SchedulerCriteria scheduleCriteria)
    {
        initializeCriteria(scheduleCriteria);
    }

    private void initializeCriteria(SchedulerCriteria schedulerCriteria)
    {
        if(schedulerCriteria != null)
        {
            this.scheduleCriteria = schedulerCriteria;
        }
    }

    public LocalDateTime getScheduledTime()
    {
        return getScheduleCriteria().getScheduledTime();
    }

}
