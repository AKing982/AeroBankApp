package com.example.aerobankapp.factory.schedulerSecurity;

import com.example.aerobankapp.model.SchedulerSecurityDTO;
import com.example.aerobankapp.scheduler.security.ScheduleRole;

public class AdminSchedulerSecurityFactory implements SchedulerSecurityFactory
{

    @Override
    public SchedulerSecurityDTO createSchedulerSecurity()
    {
        return SchedulerSecurityDTO.builder()
                .scheduleRole(ScheduleRole.SADMIN)
                .isScheduleAllowed(true)
                .isPauseEnabled(true)
                .isCronTriggerEnabled(true)
                .isShutdownEnabled(true)
                .isStartEnabled(true)
                .isPauseEnabled(true)
                .isTriggerEnabled(true)
                .canUpdateTriggers(true)
                .isCronTriggerEnabled(true)
                .isAutoShutdown(false)
                .isAutoStartup(false)
                .build();
    }
}
