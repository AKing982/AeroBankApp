package com.example.aerobankapp.scheduler.criteria;

import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.scheduler.security.ScheduleRole;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Component
public final class SchedulerCriteria {

    private Long scheduleCriteriaID;
    private int schedulerUserID;
    private LocalTime scheduledTime;
    private LocalDate scheduledDate;
    private ScheduleType scheduleType;
    private int priority;
    private LocalDate createdAt;
    private String createdByUser;

    public SchedulerCriteria(LocalTime scheduledTime, LocalDate scheduledDate, ScheduleType scheduleType, LocalDate createdAt) {
        this.scheduledTime = scheduledTime;
        this.scheduledDate = scheduledDate;
        this.scheduleType = scheduleType;
        this.createdAt = createdAt;
    }

    public SchedulerCriteria(Long scheduleCriteriaID, int schedulerUserID, LocalTime scheduledTime, LocalDate scheduledDate, ScheduleType scheduleType, int priority, LocalDate createdAt, String createdByUser) {
        this.scheduleCriteriaID = scheduleCriteriaID;
        this.schedulerUserID = schedulerUserID;
        this.scheduledTime = scheduledTime;
        this.scheduledDate = scheduledDate;
        this.scheduleType = scheduleType;
        this.priority = priority;
        this.createdAt = createdAt;
        this.createdByUser = createdByUser;
    }

    public SchedulerCriteria()
    {

    }

    public Long getScheduleCriteriaID() {
        return scheduleCriteriaID;
    }

    public void setScheduleCriteriaID(Long scheduleCriteriaID) {
        this.scheduleCriteriaID = scheduleCriteriaID;
    }

    public int getSchedulerUserID() {
        return schedulerUserID;
    }

    public void setSchedulerUserID(int schedulerUserID) {
        this.schedulerUserID = schedulerUserID;
    }

    public LocalTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public ScheduleType getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(ScheduleType scheduleType) {
        this.scheduleType = scheduleType;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }
}
