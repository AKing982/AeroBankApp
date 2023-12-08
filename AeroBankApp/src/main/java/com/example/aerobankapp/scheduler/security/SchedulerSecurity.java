package com.example.aerobankapp.scheduler.security;

import com.example.aerobankapp.model.SchedulerSecurityDTO;
import com.example.aerobankapp.workbench.utilities.UserProfile;

public interface SchedulerSecurity
{
    String getSchedulerUser();
    ScheduleRole getScheduleRole();
    SchedulerSecurityDTO getSchedulerSecurityAccess();
}
