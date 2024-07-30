package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.dto.AccountDTO;
import com.example.aerobankapp.dto.AccountInfoDTO;
import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.User;
import com.example.aerobankapp.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class AccountDataManager
{
    private AccountService accountService;
    private AccountNotificationService accountNotificationService;
    private Logger LOGGER = LoggerFactory.getLogger(AccountDataManager.class);

    @Autowired
    public AccountDataManager(AccountService accountService, AccountNotificationService accountNotificationService) {
        this.accountService = accountService;
        this.accountNotificationService = accountNotificationService;
    }




    public BigDecimal getCurrentAccountBalance(int acctID){
        BigDecimal balance = accountService.getBalanceByAcctID(acctID);
        if(balance == null){
            LOGGER.error("Unable to retrieve balances for accountID: {}", acctID);
        }
        return balance;
    }

    public void updateAccountBalance(BigDecimal newBalance, int acctID) {

    }
}
