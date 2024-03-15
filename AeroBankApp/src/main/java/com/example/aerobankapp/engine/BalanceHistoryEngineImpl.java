package com.example.aerobankapp.engine;

import com.example.aerobankapp.dto.BalanceHistoryDTO;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.model.BalanceHistory;
import com.example.aerobankapp.model.TransactionDetail;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.services.TransactionDetailService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Getter
public class BalanceHistoryEngineImpl
{
    private final TransactionDetailService transactionDetailService;
    private final AccountService accountService;

    @Autowired
    public BalanceHistoryEngineImpl(TransactionDetailService transactionDetailService, AccountService accountService){
        this.transactionDetailService = transactionDetailService;
        this.accountService = accountService;
    }

}
