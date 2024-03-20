package com.example.aerobankapp.entity;

import com.example.aerobankapp.workbench.utilities.QueueStatus;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Deprecated
@Table(name="withdrawQueue")
public class WithdrawQueueEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wQueueID;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="withdrawID", nullable = false)
    private WithdrawEntity withdraw;

    @Column(name="queuedAt")
    private Timestamp queuedAt;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private QueueStatus status;

}
