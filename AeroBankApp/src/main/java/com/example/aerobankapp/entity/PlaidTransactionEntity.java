package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="plaidTransactions")
@Data
public class PlaidTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userID")
    private UserEntity user;




    public PlaidTransactionEntity() {

    }
}
