package com.example.aerobankapp.account;

import com.example.aerobankapp.model.AccountCodeModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class GenerateAccountCode
{
    private AccountCodeModel accountCode;
    /**
     * This constructor is to be used when AccountType and Fullname or FirstName are provided
     * @param accountType
     * @param fullName
     */

    public GenerateAccountCode(AccountType accountType, String fullName)
    {
        Objects.requireNonNull(accountType, "AccountType cannot be null");
        Objects.requireNonNull(fullName, "FullName cannot be null");
        this.accountCode = new AccountCodeModel(accountType, fullName);
    }

    private String getFirstNamePrefix()
    {
        if(accountCode.getFirstName() == null || accountCode.getFirstName().isEmpty())
        {
            throw new IllegalArgumentException("Invalid First Name found: " + accountCode.getFirstName());
        }
        return accountCode.getFirstName().trim().substring(0, 1);
    }

    public String build() {
        AccountType accountType = getAccountCode().getAccountType();
        int numPrefix;
        switch (accountType) {
            case CHECKING -> {
                numPrefix = 1;
            }
            case SAVINGS -> {
                numPrefix = 2;
            }
            case RENT -> {
                numPrefix = 3;
            }
            case MORTGAGE -> {
                numPrefix = 4;
            }
            case INVESTMENT -> {
                numPrefix = 5;
            }
            default -> {
                throw new IllegalArgumentException("Invalid Account Type: " + accountType);
            }
        }
        return getFirstNamePrefix() + numPrefix;
    }

}
