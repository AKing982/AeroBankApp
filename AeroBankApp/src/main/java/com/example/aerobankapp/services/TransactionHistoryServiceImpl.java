package com.example.aerobankapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionHistoryServiceImpl implements TransactionHistoryService
{
    private final WithdrawService withdrawService;
    private final TransferService transferService;
    private final DepositService depositService;
    private final TransactionStatementService transactionStatementService;

    @Autowired
    public TransactionHistoryServiceImpl(WithdrawService withdrawService,
                                         DepositService depositService,
                                         TransferService transferService,
                                         TransactionStatementService transactionStatementService){
        this.withdrawService = withdrawService;
        this.depositService = depositService;
        this.transferService = transferService;
        this.transactionStatementService = transactionStatementService;
    }
}
