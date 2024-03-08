package com.example.aerobankapp.workbench.utilities.parser;

import com.example.aerobankapp.entity.SchedulerCriteriaEntity;
import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.scheduler.TriggerCriteria;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.aerobankapp.workbench.utilities.SysBuilderUtils.buildTriggerCriteria;

@Component
@Getter
@Setter
public class ScheduleParserImpl
{
    private List<SchedulerCriteria> schedulerCriteriaList;
    private ScheduleValidator scheduleValidator;
    private List<TriggerCriteria> triggerCriteriaList;

    @Autowired
    public ScheduleParserImpl(ScheduleValidator scheduleValidator)
    {
        this.scheduleValidator = scheduleValidator;
        this.triggerCriteriaList = new ArrayList<>();
    }

    public ScheduleParserImpl()
    {
        // Empty constructor
    }

    private void criteriaNullCheck(SchedulerCriteria schedulerCriteria)
    {
        if(schedulerCriteria.getScheduledTime() == null || schedulerCriteria.getScheduledDate() == null || schedulerCriteria.getScheduleType() == null)
        {
            throw new NullPointerException("Found Null Scheduled Time or Null Scheduled Date.");
        }
    }

    public List<TriggerCriteria> getTriggerCriteriaList(final List<SchedulerCriteria> schedulerCriteriaList)
    {
        if(!schedulerCriteriaList.isEmpty())
        {
            for(SchedulerCriteria schedulerCriteria : schedulerCriteriaList)
            {
                ScheduleType interval = getValidatedInterval(schedulerCriteria.getScheduleType());
                LocalDate date = schedulerCriteria.getScheduledDate();
                LocalTime time = schedulerCriteria.getScheduledTime();

                TriggerCriteria triggerCriteria = buildTriggerCriteria(interval,
                        getValidatedDate(date.getDayOfMonth()),
                        getValidatedMonth(date.getMonthValue()),
                        getValidatedYear(date.getYear()),
                        getValidatedMinute(time.getMinute()),
                        getValidatedHour(time.getHour()), 0);

               addTriggerCriteriaToList(triggerCriteria);
            }
        }
        return triggerCriteriaList;
    }

    private void addTriggerCriteriaToList(TriggerCriteria triggerCriteria)
    {
        this.triggerCriteriaList.add(triggerCriteria);
    }

    private ScheduleType getValidatedInterval(ScheduleType interval)
    {
        return scheduleValidator.validateInterval(interval);
    }

    private int getValidatedDate(int day)
    {
        return scheduleValidator.validateDay(day);
    }

    private int getValidatedMonth(int month)
    {
        return scheduleValidator.validateMonth(month);
    }

    private int getValidatedHour(int hour)
    {
        return scheduleValidator.validateHour(hour);
    }

    private int getValidatedYear(int year)
    {
        return scheduleValidator.validateYear(year);
    }

    private int getValidatedMinute(int minute)
    {
        return scheduleValidator.validateMinute(minute);
    }


}
