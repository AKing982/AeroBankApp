package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.model.PlaidImportResult;
import com.example.aerobankapp.model.PlaidTransaction;
import com.example.aerobankapp.model.PlaidTransactionImport;

import java.util.List;

public interface PlaidTransactionImporter
{
    List<PlaidTransactionImport> importPlaidTransactions(List<PlaidTransaction> plaidTransactionList);

    PlaidImportResult processPlaidTransaction(PlaidTransaction plaidTransaction);

    Boolean saveProcessedPlaidTransactions(List<PlaidTransactionImport> plaidTransactionList);


}
