package com.example.aerobankapp.entity;

import com.example.aerobankapp.scheduler.ScheduleType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.parameters.P;

@Entity
@Table(name="triggerCriteria")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class TriggerCriteriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long triggerCriteriaID;

    @Column(name="day")
    private int day;

    @Column(name="minute")
    private int minute;

    @Column(name="hour")
    private int hour;

    @Column(name="second")
    private int second;

    @Column(name="month")
    private int month;

    @Column(name="year")
    private int year;

    @Column(name="scheduleType")
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @Column(name="repeat")
    private int repeat;
}
