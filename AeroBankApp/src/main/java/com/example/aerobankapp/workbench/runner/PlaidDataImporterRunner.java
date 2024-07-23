package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.workbench.plaid.PlaidAccountImporter;
import com.example.aerobankapp.workbench.plaid.PlaidDataImporter;
import com.example.aerobankapp.workbench.plaid.PlaidTransactionImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaidDataImporterRunner implements Runnable
{
    private PlaidAccountImporter plaidAccountImporter;
    private PlaidTransactionImporter plaidTransactionImporter;

    @Autowired
    public PlaidDataImporterRunner(PlaidAccountImporter plaidAccountImporter,
                                   PlaidTransactionImporter plaidTransactionImporter){
        this.plaidAccountImporter = plaidAccountImporter;
        this.plaidTransactionImporter = plaidTransactionImporter;
    }

    @Override
    public void run() {

    }
}
