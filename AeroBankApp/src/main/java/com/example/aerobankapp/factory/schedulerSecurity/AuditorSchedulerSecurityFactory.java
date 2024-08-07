package com.example.aerobankapp.factory.schedulerSecurity;

import com.example.aerobankapp.model.SchedulerSecurityDTO;
import com.example.aerobankapp.scheduler.security.ScheduleRole;

public class AuditorSchedulerSecurityFactory implements SchedulerSecurityFactory
{

    @Override
    public SchedulerSecurityDTO createSchedulerSecurity()
    {
        return SchedulerSecurityDTO.builder()
                .isScheduleAllowed(false)
                .canUpdateTriggers(false)
                .isCronTriggerEnabled(false)
                .isPauseEnabled(false)
                .isShutdownEnabled(false)
                .isStartEnabled(false)
                .isTriggerEnabled(false)
                .scheduleRole(ScheduleRole.SAUDITOR)
                .build();
    }
}
