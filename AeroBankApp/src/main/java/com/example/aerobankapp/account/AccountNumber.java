package com.example.aerobankapp.account;

import lombok.*;

import javax.annotation.Nonnull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountNumber
{
    private AccountPrefix accountPrefix;
    private String branchIdentifier;
    private String accountNum;
    private boolean isSavings;
    private boolean isChecking;
    private boolean isInvestment;
    private boolean isRent;

    public AccountNumber(AccountPrefix prefix, String branch, String accountNum)
    {
        this.accountPrefix = prefix;
        this.branchIdentifier = branch;
        this.accountNum = accountNum;
    }

    private boolean validateAccountPrefixLength(AccountPrefix prefix)
    {
        return prefix.getPrefix().length() == 2;
    }

    private boolean validateBranchIdentifierLength(String branch)
    {
        return branch.length() == 4;
    }

    private boolean validateAccountNumberLength(String accountNumber)
    {
        return accountNumber.length() == 5;
    }

    public String build()
    {
        String prefix = accountPrefix.getPrefix();
        if(prefix != null && validateAccountPrefixLength(accountPrefix))
        {
            if(validateAccountNumberLength(accountNum) && validateBranchIdentifierLength(branchIdentifier))
            {
                StringBuilder sb = new StringBuilder();
                sb.append(prefix);
                sb.append("-");
                sb.append(branchIdentifier);
                sb.append("-");
                sb.append(accountNum);
                return sb.toString();
            }
            else
            {
                throw new IllegalArgumentException("Invalid Account Number or Branch Number length!");
            }
        }
        return null;
    }

}
