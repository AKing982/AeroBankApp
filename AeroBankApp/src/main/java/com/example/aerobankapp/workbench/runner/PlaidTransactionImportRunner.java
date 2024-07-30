package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.workbench.plaid.PlaidTransactionImporter;
import com.example.aerobankapp.workbench.plaid.PlaidTransactionManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlaidTransactionImportRunner
{
    private PlaidTransactionImporter transactionImporter;
    private PlaidTransactionManagerImpl plaidTransactionManager;

    @Autowired
    public PlaidTransactionImportRunner(PlaidTransactionImporter transactionImporter, PlaidTransactionManagerImpl plaidTransactionManager)
    {
        this.transactionImporter = transactionImporter;
        this.plaidTransactionManager = plaidTransactionManager;
    }
}
