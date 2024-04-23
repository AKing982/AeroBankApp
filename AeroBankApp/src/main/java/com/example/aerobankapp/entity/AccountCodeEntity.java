package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="accountCode")
@Getter
@Setter
public class AccountCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long acctCodeID;

    @Column(name="first_initial_segment")
    private String first_initial_segment;

    @Column(name="last_initial_segment")
    private String last_initial_segment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userID", referencedColumnName = "userID")
    private UserEntity user;

    @Column(name="accountType")
    private String accountType;

    @Column(name="year_segment")
    private int year_segment;

    @Column(name="sequence_segment")
    private int sequence_segment;


}
