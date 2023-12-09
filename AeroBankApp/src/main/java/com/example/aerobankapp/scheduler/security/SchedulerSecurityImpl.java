package com.example.aerobankapp.scheduler.security;

import com.example.aerobankapp.factory.schedulerSecurity.SchedulerSecurityFactoryImpl;
import com.example.aerobankapp.model.SchedulerSecurityDTO;
import com.example.aerobankapp.workbench.utilities.UserProfile;
import com.example.aerobankapp.workbench.utilities.UserProfileCache;
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
    private SchedulerSecurityDTO securityAccess;



    public SchedulerSecurityImpl()
    {

    }


    @Override
    public String getSchedulerUser()
    {
        return null;
    }

    @Override
    public ScheduleRole getScheduleRole()
    {
        return null;
    }

    @Override
    public SchedulerSecurityDTO getSchedulerSecurityAccess()
    {
        return null;
    }
}
