package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.workbench.plaid.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaidDataImporterRunner implements Runnable
{
    private PlaidAccountImporter plaidAccountImporter;
    private PlaidTransactionImporter plaidTransactionImporter;
    private PlaidAccountManager plaidAccountManager;
    private PlaidTransactionManagerImpl plaidTransactionManager;

    @Autowired
    public PlaidDataImporterRunner(PlaidAccountImporter plaidAccountImporter,
                                   PlaidTransactionImporter plaidTransactionImporter,
                                   PlaidAccountManager plaidAccountManager,
                                   PlaidTransactionManagerImpl plaidTransactionManager){
        this.plaidAccountImporter = plaidAccountImporter;
        this.plaidTransactionImporter = plaidTransactionImporter;
        this.plaidAccountManager = plaidAccountManager;
        this.plaidTransactionManager = plaidTransactionManager;
    }

    @Override
    public void run() {
        // Fetch all the accounts tied to the user using the access token
        // Fetch all the transactions tied to the user using the access token


    }
}
