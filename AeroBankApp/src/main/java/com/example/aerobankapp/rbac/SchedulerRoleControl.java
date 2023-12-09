package com.example.aerobankapp.rbac;

import com.example.aerobankapp.scheduler.security.ScheduleRole;
import com.example.aerobankapp.scheduler.security.SchedulerSecurityImpl;
import com.example.aerobankapp.workbench.utilities.UserType;
import org.quartz.Scheduler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SchedulerRoleControl extends SystemRoleControl
{
    private ScheduleRole scheduleRole;
    private SchedulerSecurityImpl schedulerSecurity;
    private Scheduler scheduler;

    public SchedulerRoleControl(Scheduler scheduler, SchedulerSecurityImpl schedulerSecurity, ScheduleRole scheduleRole, UserType userRole, Set<SystemPermission> userPermissions)
    {
        super(userRole, userPermissions);
        this.scheduleRole = scheduleRole;
        this.scheduler = scheduler;
        this.schedulerSecurity = schedulerSecurity;
    }

    @Override
    public Set<SystemPermission> getSystemPermissions(int userID, UserType userType)
    {
        return null;
    }

    @Override
    public Set<SystemRoleControl> getSystemAccess(Map<Integer, Set<SystemPermission>> userPermissions)
    {
        Set<SystemRoleControl> access = new HashSet<>();

        for(Map.Entry<Integer, Set<SystemPermission>> entry : userPermissions.entrySet())
        {
            for(SystemPermission permission : entry.getValue())
            {
                if(permission.equals)
            }

        }

    }

    @Override
    public boolean isAllowDelete() {
        return false;
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public boolean isAllowAccess() {
        return false;
    }

    @Override
    public boolean isAllowCreate() {
        return false;
    }

    @Override
    public boolean isAllowUpdate() {
        return false;
    }

    @Override
    public void disable() {

    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void create() {

    }

    public boolean isRead()
    {
        return false;
    }


}
