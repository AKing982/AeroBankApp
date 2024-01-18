package com.example.aerobankapp.workbench.security.authentication;

import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.workbench.transactions.CardDesignator;

import java.util.Set;

public interface UserSecurityModel
{
    Set<AccountEntity> getAccountDetails();
    Set<CardDesignator> getUserCardDetails();
    boolean isUserEnabled();
}
