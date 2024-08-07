package com.example.aerobankapp.entity;

import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.workbench.utilities.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="schedulerCriteria")
public class SchedulerCriteriaEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int schedulerCriteriaID;

    @ManyToOne
    @JoinColumn(name="schedulerUserID")
    private UserEntity schedulerUser;

    @Column(name="scheduledTime")
    private LocalTime scheduledTime;

    @Column(name="scheduledDate")
    private LocalDate scheduledDate;

    @Column(name="scheduleType")
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name="priority")
    private int priority;

    @Column(name="lastRunTime")
    private LocalDate lastRunTime;

    @Column(name="nextRunTime")
    private LocalDate nextRunTime;

    public SchedulerCriteriaEntity(int schedulerCriteriaID, UserEntity schedulerUser, LocalTime scheduledTime, LocalDate scheduledDate, ScheduleType scheduleType, Status status, int priority, LocalDate lastRunTime, LocalDate nextRunTime) {
        this.schedulerCriteriaID = schedulerCriteriaID;
        this.schedulerUser = schedulerUser;
        this.scheduledTime = scheduledTime;
        this.scheduledDate = scheduledDate;
        this.scheduleType = scheduleType;
        this.status = status;
        this.priority = priority;
        this.lastRunTime = lastRunTime;
        this.nextRunTime = nextRunTime;
    }

    public SchedulerCriteriaEntity()
    {

    }

    public int getSchedulerCriteriaID() {
        return schedulerCriteriaID;
    }

    public void setSchedulerCriteriaID(int schedulerCriteriaID) {
        this.schedulerCriteriaID = schedulerCriteriaID;
    }

    public UserEntity getSchedulerUser() {
        return schedulerUser;
    }

    public void setSchedulerUser(UserEntity schedulerUser) {
        this.schedulerUser = schedulerUser;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public LocalDate getLastRunTime() {
        return lastRunTime;
    }

    public void setLastRunTime(LocalDate lastRunTime) {
        this.lastRunTime = lastRunTime;
    }

    public LocalDate getNextRunTime() {
        return nextRunTime;
    }

    public void setNextRunTime(LocalDate nextRunTime) {
        this.nextRunTime = nextRunTime;
    }
}
