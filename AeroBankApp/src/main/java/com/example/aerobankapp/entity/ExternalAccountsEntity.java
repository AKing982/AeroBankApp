package com.example.aerobankapp.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Table(name="externalAccounts")
@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ExternalAccountsEntity {

    @Id
    @Column(columnDefinition = "VARCHAR(255)")
    private String externalAcctID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="acctID")
    private AccountEntity account;

    public ExternalAccountsEntity(String id, AccountEntity account) {
        this.externalAcctID = id;
        this.account = account;
    }

    @Override
    public String toString() {
        return "ExternalAccountsEntity{" +
                "externalAcctID='" + externalAcctID + '\'' +
                ", account=" + account +
                '}';
    }
}
