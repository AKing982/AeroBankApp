package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountSecurityEntity;
import com.example.aerobankapp.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountSecurityDataManager extends AbstractDataManager
{
    public AccountSecurityDataManager(UserService userService, AccountService accountService, AccountSecurityService accountSecurityService, AccountPropertiesService accountPropertiesService, AccountNotificationService accountNotificationService, PlaidAccountsService plaidAccountsService, AccountCodeService accountCodeService, AccountUsersEntityService accountUsersEntityService, UserLogService userLogService) {
        super(userService, accountService, accountSecurityService, accountPropertiesService, accountNotificationService, accountCodeService, plaidAccountsService,accountUsersEntityService, userLogService);
    }

    public AccountSecurityEntity buildAccountSecurityEntity(AccountEntity account){
        return null;
    }

    public AccountSecurityEntity buildAccountSecurityProperties(AccountEntity account){
        return null;
    }

    public AccountSecurityEntity buildAccountProperties(boolean isEnabled, boolean isAccountLocked, int depositLimit, int withdrawLimit, int transferLimit, boolean depositEnabled, boolean withdrawEnabled, boolean transferEnabled,
                                                      boolean interestEnabled, boolean feesEnabled, int transferVelocityLimit, BigDecimal minimumBalance, boolean autoPayOn, AccountEntity account){
        return AccountSecurityEntity.builder()
                .isEnabled(isEnabled)
                .isAccountLocked(isAccountLocked)
                .depositLimit(depositLimit)
                .withdrawLimit(withdrawLimit)
                .transferLimit(transferLimit)
                .isDepositEnabled(depositEnabled)
                .isWithdrawEnabled(withdrawEnabled)
                .isTransferEnabled(transferEnabled)
                .interestEnabled(interestEnabled)
                .feesEnabled(feesEnabled)
                .transactionVelocityLimit(transferVelocityLimit)
                .minimumBalance(minimumBalance)
                .autoPayEnabled(autoPayOn)
                .account(account)
                .build();
    }
}
