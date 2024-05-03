package com.example.aerobankapp.workbench.transactionHistory;

import com.example.aerobankapp.workbench.transactionHistory.criteria.HistoryCriteria;

import java.util.List;

public interface QueryBuilder
{
    String getQueryFromHistoryCriteria(HistoryCriteria historyCritera);

    List<String> buildQueryConditions(HistoryCriteria criteria, StringBuilder query);
}
