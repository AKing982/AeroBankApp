package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.SchedulerCriteriaEntity;
import com.example.aerobankapp.exceptions.InvalidSchedulerCriteriaException;
import com.example.aerobankapp.scheduler.TriggerCriteria;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;
import com.example.aerobankapp.workbench.utilities.SysBuilderUtils;
import com.example.aerobankapp.workbench.utilities.parser.ScheduleParserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleParserServiceImpl implements ScheduleParserService
{
    private ScheduleParserImpl scheduleParser;
    private final SchedulerCriteriaService schedulerCriteriaService;

    @Autowired
    public ScheduleParserServiceImpl(ScheduleParserImpl scheduleParser,
                                     SchedulerCriteriaService schedulerCriteriaService)
    {
        this.scheduleParser = scheduleParser;
        this.schedulerCriteriaService = schedulerCriteriaService;
    }

    @Override
    public List<SchedulerCriteriaEntity> getScheduledCriteriaEntityList(int userID) {
        return schedulerCriteriaService.findByUserID(userID);
    }

    @Override
    public List<SchedulerCriteria> getScheduleCriteriaList(final List<SchedulerCriteriaEntity> schedulerCriteriaEntities)
    {
        return schedulerCriteriaEntities.stream()
                .map(SysBuilderUtils::buildSchedulerCriteria)
                .collect(Collectors.toList());
    }

    @Override
    public List<TriggerCriteria> getParsedTriggerCriteria(final List<SchedulerCriteria> schedulerCriteriaList)
    {
        if(schedulerCriteriaList.isEmpty())
        {
            throw new IllegalArgumentException("Found Empty Scheduler Criteria List...");
        }
        validateSchedulerCriteria(schedulerCriteriaList);
        return scheduleParser.getTriggerCriteriaList(schedulerCriteriaList);
    }

    private void validateSchedulerCriteria(final List<SchedulerCriteria> schedulerCriteriaList)
    {
        for(SchedulerCriteria schedulerCriteria1 : schedulerCriteriaList)
        {
            if(schedulerCriteria1.getScheduledDate() == null || schedulerCriteria1.getScheduledTime() == null || schedulerCriteria1.getScheduleType() == null)
            {
                throw new InvalidSchedulerCriteriaException("Invalid Null ScheduleCriteria Found...");
            }
        }
    }
}
