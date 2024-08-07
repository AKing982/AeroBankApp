package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.AeroBankAppApplication;
import com.example.aerobankapp.model.PlaidTransaction;
import com.example.aerobankapp.workbench.plaid.PlaidTransactionImporter;
import com.example.aerobankapp.workbench.plaid.PlaidTransactionManagerImpl;
import com.plaid.client.model.Transaction;
import com.plaid.client.model.TransactionsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
public class PlaidTransactionImportRunner
{
    private PlaidTransactionImporter transactionImporter;
    private PlaidTransactionManagerImpl plaidTransactionManager;

    @Autowired
    public PlaidTransactionImportRunner(PlaidTransactionImporter transactionImporter,
                                        PlaidTransactionManagerImpl plaidTransactionManager)
    {
        this.transactionImporter = transactionImporter;
        this.plaidTransactionManager = plaidTransactionManager;
    }

    public List<PlaidTransaction> getUserPlaidTransactions(int userId, LocalDate startDate, LocalDate endDate) throws IOException {
        TransactionsGetResponse transactionsGetResponses = plaidTransactionManager.getTransactionResponse(userId, startDate, endDate);
        List<Transaction> transactions = transactionsGetResponses.getTransactions();

        return null;
    }



    public void importTransactions(int userId) throws IOException, InterruptedException {

    }


    public static void main(String[] args) throws IOException, InterruptedException
    {
        ConfigurableApplicationContext context = SpringApplication.run(AeroBankAppApplication.class, args);

        PlaidTransactionImportRunner runner = context.getBean(PlaidTransactionImportRunner.class);
        runner.importTransactions(1);
    }
}
