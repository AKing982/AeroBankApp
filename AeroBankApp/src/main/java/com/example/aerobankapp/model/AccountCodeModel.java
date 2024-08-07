package com.example.aerobankapp.model;

import com.example.aerobankapp.account.AccountType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;


@ToString
@EqualsAndHashCode
@Deprecated
public class AccountCodeModel
{
    private AccountType accountType;
    private String firstName;

    public AccountCodeModel(AccountType accountType, String firstName)
    {
        this.accountType = accountType;
        this.firstName = firstName;
    }

    public AccountCodeModel()
    {

    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
