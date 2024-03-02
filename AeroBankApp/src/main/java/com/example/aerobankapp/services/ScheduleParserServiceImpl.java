package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.SchedulerCriteriaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleParserServiceImpl implements ScheduleParserService
{
    private final UserLogService userLogService;
    private final SchedulerCriteriaService schedulerCriteriaService;

    @Autowired
    public ScheduleParserServiceImpl(UserLogService userLogService,
                                     SchedulerCriteriaService schedulerCriteriaService)
    {
        this.userLogService = userLogService;
        this.schedulerCriteriaService = schedulerCriteriaService;
    }

    @Override
    public List<SchedulerCriteriaEntity> getScheduledCriteriaEntityList(int userID) {
        return null;
    }
}
