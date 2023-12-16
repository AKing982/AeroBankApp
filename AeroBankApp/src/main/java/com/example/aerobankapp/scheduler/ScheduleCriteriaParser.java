package com.example.aerobankapp.scheduler;

import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public String getScheduledTime()
    {
        return getScheduleCriteria().getScheduledTime();
    }

}
