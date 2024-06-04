package com.example.aerobankapp.workbench.security.authentication;

import com.example.aerobankapp.entity.*;

import java.util.Set;

public interface UserSecurityModel
{
    Set<AccountEntity> getAccountDetails();
    boolean isUserEnabled();
}
