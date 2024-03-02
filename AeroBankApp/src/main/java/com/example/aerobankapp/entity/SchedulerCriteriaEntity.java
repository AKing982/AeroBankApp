package com.example.aerobankapp.entity;

import com.example.aerobankapp.scheduler.ScheduleType;
import com.example.aerobankapp.workbench.utilities.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC, force=true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name="schedulerCriteria")
public class SchedulerCriteriaEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int schedulerCriteriaID;

    @ManyToOne
    private UserEntity schedulerUserID;

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
}
