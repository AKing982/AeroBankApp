package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.SchedulerCriteriaEntity;

import java.util.List;

public interface ScheduleParserService
{
    List<SchedulerCriteriaEntity> getScheduledCriteriaEntityList(int userID);
}
