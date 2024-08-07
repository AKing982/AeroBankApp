package com.example.aerobankapp.entity;

import com.example.aerobankapp.embeddables.AccountUserEmbeddable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Account_Users", schema="aerobank")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AccountUserEntity {

    @EmbeddedId
    private AccountUserEmbeddable id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="acctID", insertable = false, updatable = false)
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userID", insertable = false, updatable = false)
    private UserEntity user;

    public AccountUserEntity(AccountUserEmbeddable id){
        this.id = id;
    }
}
