package com.example.aerobankapp.account;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * This class will manage the AccountID
 * @author alexking
 */
@Getter
@Setter
@ToString
@Deprecated
public class AccountCodeDTO
{
    private GenerateAccountCode generateAccountCode;

    public AccountCodeDTO(AccountType accountType, String firstName)
    {
        this.generateAccountCode = new GenerateAccountCode(accountType, firstName);
    }

    public String getAccountCode()
    {
        return generateAccountCode.build();
    }

}
