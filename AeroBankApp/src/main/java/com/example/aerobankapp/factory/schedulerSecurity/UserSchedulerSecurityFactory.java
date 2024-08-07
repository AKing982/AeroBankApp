package com.example.aerobankapp.factory.schedulerSecurity;

import com.example.aerobankapp.model.SchedulerSecurityDTO;
import com.example.aerobankapp.scheduler.security.ScheduleRole;

public class UserSchedulerSecurityFactory implements SchedulerSecurityFactory
{

    @Override
    public SchedulerSecurityDTO createSchedulerSecurity() {
        return SchedulerSecurityDTO.builder()
                .scheduleRole(ScheduleRole.SUSER)
                .isCronTriggerEnabled(true)
                .canUpdateTriggers(false)
                .isScheduleAllowed(true)
                .isStartEnabled(true)
                .isShutdownEnabled(false)
                .isAutoShutdown(true)
                .isAutoStartup(true)
                .isPauseEnabled(false)
                .isTriggerEnabled(true)
                .build();

    }
}
