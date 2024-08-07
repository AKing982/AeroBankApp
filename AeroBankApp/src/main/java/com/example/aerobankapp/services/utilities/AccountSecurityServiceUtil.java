package com.example.aerobankapp.services.utilities;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountSecurityEntity;

import java.math.BigDecimal;

public class AccountSecurityServiceUtil
{

    public static AccountSecurityEntity buildSecurityEntity(AccountEntity account) {
        AccountSecurityEntity accountSecurityEntity = new AccountSecurityEntity();
        AccountType accountType = AccountType.getInstance(account.getAccountType());
        return buildAccountSecurityEntityProperties(account);
    }

    public static AccountSecurityEntity buildAccountSecurityEntityProperties(AccountEntity account){
        AccountType accountType = AccountType.getInstance(account.getAccountType());
        AccountSecurityEntity accountSecurity = new AccountSecurityEntity();

        // Convert the Account object to an account Entity
        // AccountEntity accountEntity = accountService.buildAccountEntityByAccountModel(account);
        switch(accountType){
            case CHECKING -> {
                accountSecurity = buildEntity(true,
                        false,
                        1000,
                        1000,
                        25,
                        true,
                        true,
                        true,
                        true,
                        false,
                        10,
                        new BigDecimal("120.000"),
                        true, account);
            }
            case SAVINGS -> {
                accountSecurity = buildEntity(true,
                        false,
                        1000,
                        20,
                        15,
                        true,
                        true,
                        true,
                        true,
                        true,
                        5,
                        new BigDecimal("300.000"),
                        true, account);
            }
            case RENT -> {
                accountSecurity = buildEntity(true,
                        false,
                        1000,
                        90,
                        100,
                        true,
                        true,
                        true,
                        true,
                        true,
                        11,
                        new BigDecimal("1215.000"),
                        true,
                        account);
            }
            case INVESTMENT -> {
                accountSecurity = buildEntity(true,
                        false,
                        1000,
                        12,
                        10,
                        true,
                        true,
                        true,
                        false,
                        false,
                        2,
                        new BigDecimal("8500.000"),
                        false, account);
            }
        }
        return accountSecurity;
    }

    public static AccountSecurityEntity buildEntity(boolean isEnabled, boolean isAccountLocked, int depositLimit, int withdrawLimit, int transferLimit, boolean depositEnabled, boolean withdrawEnabled, boolean transferEnabled,
                                              boolean interestEnabled, boolean feesEnabled, int transferVelocityLimit, BigDecimal minimumBalance, boolean autoPayOn, AccountEntity account){
        AccountSecurityEntity accountSecurity = new AccountSecurityEntity();
        accountSecurity.setEnabled(isEnabled);
        accountSecurity.setAccountLocked(isAccountLocked);
        accountSecurity.setDepositLimit(depositLimit);
        accountSecurity.setWithdrawLimit(withdrawLimit);
        accountSecurity.setDepositEnabled(depositEnabled);
        accountSecurity.setFeesEnabled(feesEnabled);
        accountSecurity.setInterestEnabled(interestEnabled);
        accountSecurity.setMinimumBalance(minimumBalance);
        accountSecurity.setTransferLimit(transferLimit);
        accountSecurity.setAutoPayEnabled(autoPayOn);
        accountSecurity.setTransactionVelocityLimit(transferVelocityLimit);
        accountSecurity.setTransferEnabled(transferEnabled);
        accountSecurity.setWithdrawEnabled(withdrawEnabled);
        accountSecurity.setAccount(account);
        return accountSecurity;
    }

}
