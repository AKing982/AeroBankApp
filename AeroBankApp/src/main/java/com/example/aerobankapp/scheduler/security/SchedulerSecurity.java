package com.example.aerobankapp.scheduler.security;

import com.example.aerobankapp.model.SchedulerSecurityDTO;

public interface SchedulerSecurity
{
    String getSchedulerUser();
    ScheduleRole getScheduleRole();
    SchedulerSecurityDTO getSchedulerSecurityAccess();
}
