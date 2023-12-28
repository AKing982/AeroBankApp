package com.example.aerobankapp.model;

import com.example.aerobankapp.scheduler.security.ScheduleRole;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
@Getter
@Setter
@Builder
public final class SchedulerSecurityDTO {

    private Long schedulerSecurityID;
    private int userID;
    private String userName;
    private boolean isScheduleAllowed;
    private boolean isTriggerEnabled;
    private boolean isCronTriggerEnabled;
    private boolean isAutoShutdown;
    private boolean isAutoStartup;
    private boolean isPauseEnabled;
    private boolean isShutdownEnabled;
    private boolean canUpdateTriggers;
    private boolean isStartEnabled;
    private ScheduleRole scheduleRole;
    private LocalDate dateModified;

}
