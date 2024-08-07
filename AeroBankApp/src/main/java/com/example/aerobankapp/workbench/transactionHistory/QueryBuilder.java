package com.example.aerobankapp.workbench.transactionHistory;

import com.example.aerobankapp.workbench.transactionHistory.criteria.HistoryCriteria;
import com.example.aerobankapp.workbench.transactions.TransactionType;

import java.util.List;

public interface QueryBuilder
{
    String getQueryFromHistoryCriteria(HistoryCriteria historyCritera);

    List<String> buildQueryConditions(HistoryCriteria criteria, StringBuilder query);

    StringBuilder buildTableStatement(TransactionType transactionType);

    String getDefaultUserQuery();

    void revised(HistoryCriteria criteria, List<String> conditions, StringBuilder query);
}
