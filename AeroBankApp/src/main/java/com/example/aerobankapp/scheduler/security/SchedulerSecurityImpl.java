package com.example.aerobankapp.scheduler.security;

import com.example.aerobankapp.model.SchedulerSecurityDTO;
import com.example.aerobankapp.workbench.utilities.UserProfile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SchedulerSecurityImpl implements SchedulerSecurity
{
    private UserProfile userProfile;
    private SchedulerSecurityDTO securityAccess;

    @Autowired
    public SchedulerSecurityImpl()
    {
        this.userProfile = new UserProfile(user);
    }

    @Override
    public String getSchedulerUser()
    {
        return userProfile.getUsername();
    }

    @Override
    public ScheduleRole getScheduleRole() {
        return null;
    }

    @Override
    public SchedulerSecurityDTO getSchedulerSecurityAccess() {
        return null;
    }
}
