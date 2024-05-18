package com.example.aerobankapp.entity;

import com.example.aerobankapp.workbench.utilities.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Deprecated
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name="bills")
public class BillsEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="accountNumber")
    private String accountNumber;

    @Column(name="accountCode")
    private String accountCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userID")
    private UserEntity user;

    @Column(name="payeeName", nullable = false)
    @NotNull
    @Size(min=10, max=255)
    private String payeeName;

    @Column(name="payeeAccountNumber", nullable = false)
    @NotNull
    @Size(max=8)
    private String payeeAccountNumber;

    @Column(name="billAmount")
    private BigDecimal billAmount;

    @Column(name="dueDate")
    private LocalDate dueDate;

    @Column(name="paymentDate")
    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private Status status;

    @Column(name="referenceNumber")
    private String referenceNumber;

    @Column(name="description")
    private String description;

    @Column(name="createdAt")
    private String createdAt;

    @Column(name="createdBy")
    private String createdBy;

}
