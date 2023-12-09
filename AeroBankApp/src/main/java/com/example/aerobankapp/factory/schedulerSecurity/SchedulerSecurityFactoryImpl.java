package com.example.aerobankapp.factory.schedulerSecurity;

import com.example.aerobankapp.model.SchedulerSecurityDTO;
import com.example.aerobankapp.scheduler.security.ScheduleRole;

public class SchedulerSecurityFactoryImpl
{
    private SchedulerSecurityFactory schedulerSecurityFactory;

    public SchedulerSecurityDTO getSchedulerSecurityFactoryInstance(final ScheduleRole role)
    {
        return switch (role) {
            case SADMIN -> {
                schedulerSecurityFactory = new AdminSchedulerSecurityFactory();
                yield schedulerSecurityFactory.createSchedulerSecurity();
            }
            case SUSER -> {
                schedulerSecurityFactory = new UserSchedulerSecurityFactory();
                yield schedulerSecurityFactory.createSchedulerSecurity();
            }
            case SMANAGER -> {
                schedulerSecurityFactory = new ManagerSchedulerSecurityFactory();
                yield schedulerSecurityFactory.createSchedulerSecurity();
            }
            case STELLER -> {
                schedulerSecurityFactory = new TellerSchedulerSecurityFactory();
                yield schedulerSecurityFactory.createSchedulerSecurity();
            }
            case SAUDITOR -> {
                schedulerSecurityFactory = new AuditorSchedulerSecurityFactory();
                yield schedulerSecurityFactory.createSchedulerSecurity();
            }
            default -> null;
        };
    }
}
