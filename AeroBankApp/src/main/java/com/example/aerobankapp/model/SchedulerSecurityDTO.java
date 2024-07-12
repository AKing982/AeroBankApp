package com.example.aerobankapp.model;

import com.example.aerobankapp.scheduler.security.ScheduleRole;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;



@Builder
@Deprecated
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

    public SchedulerSecurityDTO(Long schedulerSecurityID, int userID, String userName, boolean isScheduleAllowed, boolean isTriggerEnabled, boolean isCronTriggerEnabled, boolean isAutoShutdown, boolean isAutoStartup, boolean isPauseEnabled, boolean isShutdownEnabled, boolean canUpdateTriggers, boolean isStartEnabled, ScheduleRole scheduleRole, LocalDate dateModified) {
        this.schedulerSecurityID = schedulerSecurityID;
        this.userID = userID;
        this.userName = userName;
        this.isScheduleAllowed = isScheduleAllowed;
        this.isTriggerEnabled = isTriggerEnabled;
        this.isCronTriggerEnabled = isCronTriggerEnabled;
        this.isAutoShutdown = isAutoShutdown;
        this.isAutoStartup = isAutoStartup;
        this.isPauseEnabled = isPauseEnabled;
        this.isShutdownEnabled = isShutdownEnabled;
        this.canUpdateTriggers = canUpdateTriggers;
        this.isStartEnabled = isStartEnabled;
        this.scheduleRole = scheduleRole;
        this.dateModified = dateModified;
    }

    public Long getSchedulerSecurityID() {
        return schedulerSecurityID;
    }

    public void setSchedulerSecurityID(Long schedulerSecurityID) {
        this.schedulerSecurityID = schedulerSecurityID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isScheduleAllowed() {
        return isScheduleAllowed;
    }

    public void setScheduleAllowed(boolean scheduleAllowed) {
        isScheduleAllowed = scheduleAllowed;
    }

    public boolean isTriggerEnabled() {
        return isTriggerEnabled;
    }

    public void setTriggerEnabled(boolean triggerEnabled) {
        isTriggerEnabled = triggerEnabled;
    }

    public boolean isCronTriggerEnabled() {
        return isCronTriggerEnabled;
    }

    public void setCronTriggerEnabled(boolean cronTriggerEnabled) {
        isCronTriggerEnabled = cronTriggerEnabled;
    }

    public boolean isAutoShutdown() {
        return isAutoShutdown;
    }

    public void setAutoShutdown(boolean autoShutdown) {
        isAutoShutdown = autoShutdown;
    }

    public boolean isAutoStartup() {
        return isAutoStartup;
    }

    public void setAutoStartup(boolean autoStartup) {
        isAutoStartup = autoStartup;
    }

    public boolean isPauseEnabled() {
        return isPauseEnabled;
    }

    public void setPauseEnabled(boolean pauseEnabled) {
        isPauseEnabled = pauseEnabled;
    }

    public boolean isShutdownEnabled() {
        return isShutdownEnabled;
    }

    public void setShutdownEnabled(boolean shutdownEnabled) {
        isShutdownEnabled = shutdownEnabled;
    }

    public boolean isCanUpdateTriggers() {
        return canUpdateTriggers;
    }

    public void setCanUpdateTriggers(boolean canUpdateTriggers) {
        this.canUpdateTriggers = canUpdateTriggers;
    }

    public boolean isStartEnabled() {
        return isStartEnabled;
    }

    public void setStartEnabled(boolean startEnabled) {
        isStartEnabled = startEnabled;
    }

    public ScheduleRole getScheduleRole() {
        return scheduleRole;
    }

    public void setScheduleRole(ScheduleRole scheduleRole) {
        this.scheduleRole = scheduleRole;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }
}
