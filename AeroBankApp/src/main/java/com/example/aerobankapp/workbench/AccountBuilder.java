package com.example.aerobankapp.workbench;

import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountBuilder
{
    private final AccountCodeCreator accountCodeCreator;
    private final AccountPropertiesService accountPropertiesService;
    private final AccountCodeService accountCodeService;
    private final AccountSecurityService accountSecurityService;
    private final AccountService accountService;

    @Autowired
    public AccountBuilder(AccountCodeCreator accountCodeCreator, AccountPropertiesService accountPropertiesService, AccountCodeService accountCodeService, AccountSecurityService accountSecurityService, AccountService accountService) {
        this.accountCodeCreator = accountCodeCreator;
        this.accountPropertiesService = accountPropertiesService;
        this.accountCodeService = accountCodeService;
        this.accountSecurityService = accountSecurityService;
        this.accountService = accountService;
    }

    public AccountEntity createAccountEntityFromPlaidAccount(final PlaidAccount plaidAccount, final UserEntity user)
    {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountName(plaidAccount.getName());
        accountEntity.setSubtype(plaidAccount.getSubtype());
        accountEntity.setType(plaidAccount.getType());
        accountEntity.setBalance(plaidAccount.getCurrentBalance());

        accountEntity.setUser(user);
        accountEntity.setMask(plaidAccount.getMask());

        AccountSecurityEntity accountSecurityEntity = createAccountSecurity(accountEntity, user);
        saveAccountSecurityEntity(accountSecurityEntity);

        AccountPropertiesEntity accountPropertiesEntity = createAccountProperties(accountEntity);
        saveAccountPropertiesEntity(accountPropertiesEntity);
        AccountCodeEntity accountCodeEntity = createAccountCodeEntity(accountEntity, user);
        saveAccountCodeEntity(accountCodeEntity);

        accountEntity.setAccountSecurity(accountSecurityEntity);
        accountEntity.setAccountProperties(accountPropertiesEntity);
        accountEntity.setAccountCode(accountCodeEntity);
        saveAccountEntity(accountEntity);
        return accountEntity;
    }

    private AccountCode createAccountCodeModelFromUserAndAccount(User user, Account account){
        return accountCodeCreator.createAccountCode(user, account);
    }


    public AccountCodeEntity createAccountCodeEntity(AccountEntity account, UserEntity user)
    {
        return accountCodeCreator.createAccountCodeEntityFromAccountAndUser(account, user);
    }

    private void saveAccountCodeEntity(AccountCodeEntity accountCodeEntity)
    {
        accountCodeService.save(accountCodeEntity);
    }

    private void saveAccountSecurityEntity(AccountSecurityEntity accountSecurityEntity)
    {
        accountSecurityService.save(accountSecurityEntity);
    }

    private void saveAccountEntity(AccountEntity accountEntity)
    {
        accountService.save(accountEntity);
    }

    private void saveAccountPropertiesEntity(AccountPropertiesEntity accountPropertiesEntity)
    {
        accountPropertiesService.save(accountPropertiesEntity);
    }

    public AccountSecurityEntity createAccountSecurity(AccountEntity accountEntity, UserEntity user)
    {
        AccountSecurityEntity accountSecurityEntity = new AccountSecurityEntity();
        accountSecurityEntity.setAccount(accountEntity);
        accountSecurityEntity.setAccountLocked(false);
        accountSecurityEntity.setEnabled(true);
        accountSecurityEntity.setDepositLimit(1000);
        accountSecurityEntity.setDepositEnabled(true);
        accountSecurityEntity.setAutoPayEnabled(true);
        accountSecurityEntity.setMinimumBalance(new BigDecimal("120"));
        return accountSecurityEntity;
    }

    public AccountPropertiesEntity createAccountProperties(AccountEntity accountEntity){
        AccountPropertiesEntity accountPropertiesEntity = new AccountPropertiesEntity();
        accountPropertiesEntity.setAccount(accountEntity);

        String randomUrl = accountPropertiesService.getRandomImageUrl();
        String randomAcctColor = accountPropertiesService.getRandomAcctColor();
        accountPropertiesEntity.setImage_url(randomUrl);
        accountPropertiesEntity.setAcct_color(randomAcctColor);
        return accountPropertiesEntity;
    }
}
