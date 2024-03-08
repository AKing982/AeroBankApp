package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.SchedulerCriteriaEntity;
import com.example.aerobankapp.scheduler.TriggerCriteria;
import com.example.aerobankapp.scheduler.criteria.SchedulerCriteria;

import java.util.List;

public interface ScheduleParserService
{
    List<SchedulerCriteriaEntity> getScheduledCriteriaEntityList(int userID);

    List<SchedulerCriteria> getScheduleCriteriaList(List<SchedulerCriteriaEntity> schedulerCriteriaEntityList);

    List<TriggerCriteria> getParsedTriggerCriteria(final List<SchedulerCriteria> schedulerCriteriaEntities);
}
