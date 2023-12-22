package com.example.aerobankapp.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountPrefix
{
    private String prefix;

    public AccountPrefix(String prefix)
    {
        nullCheck(prefix);
    }

    private void nullCheck(String prefix)
    {
        if(prefix != null && prefix.length() == 2)
        {
            this.prefix = prefix;
        }
        else
        {
            throw new IllegalArgumentException("Incorrect Prefix was entered!");
        }
    }

}
