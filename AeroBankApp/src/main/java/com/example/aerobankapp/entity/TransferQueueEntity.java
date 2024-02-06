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
@Table(name="transferQueue")
public class TransferQueueEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tQueueID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="transferID", nullable = false)
    private TransferEntity transferEntity;

    @Column(name="queuedAt")
    private Timestamp queuedAt;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private QueueStatus status;

}
