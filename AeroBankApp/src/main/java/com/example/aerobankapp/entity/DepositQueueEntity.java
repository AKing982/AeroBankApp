package com.example.aerobankapp.entity;

import com.example.aerobankapp.workbench.utilities.QueueStatus;
import com.example.aerobankapp.workbench.utilities.Status;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="depositQueue")
public class DepositQueueEntity

{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dQueueID;

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name="depositID", nullable = false)
    private DepositsEntity deposit;

    @Column(name="queuedAt")
    private Timestamp queuedAt;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private QueueStatus status;

}
