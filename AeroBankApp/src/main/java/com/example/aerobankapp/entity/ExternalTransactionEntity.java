package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="externalTransactions")
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ExternalTransactionEntity {

    @Id
    @Column(columnDefinition = "VARCHAR(50)")
    private String externalTransactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="externalAcctID")
    private ExternalAccountsEntity externalAccounts;

    public ExternalTransactionEntity(String externalTransactionId, ExternalAccountsEntity externalAccounts) {
        this.externalTransactionId = externalTransactionId;
        this.externalAccounts = externalAccounts;
    }
}
