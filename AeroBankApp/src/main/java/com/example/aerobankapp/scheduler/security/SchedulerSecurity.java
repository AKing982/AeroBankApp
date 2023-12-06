package com.example.aerobankapp.scheduler.security;

import com.example.aerobankapp.workbench.utilities.BankAuthorization;
import com.example.aerobankapp.workbench.utilities.UserProfile;
import com.example.aerobankapp.workbench.utilities.quartz.Quartz;
import lombok.Getter;
import lombok.Setter;

import java.util.Properties;

@Getter
@Setter
public class SchedulerSecurity
{
    private UserProfile userProfile;
    private boolean isSchedulerUser;
    private boolean isSchedulerAdmin;
    private boolean isScheduleAllowed;
    private boolean isCronScheduled;
    private Properties securityProperties;

}
