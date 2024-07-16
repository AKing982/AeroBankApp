package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="externalAccounts")
@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ExternalAccountsEntity {

    @Id
    @GeneratedValue
    private String externalAcctID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="acctID")
    private AccountEntity account;

    public ExternalAccountsEntity(String id, AccountEntity account) {
        this.externalAcctID = id;
        this.account = account;
    }
}
