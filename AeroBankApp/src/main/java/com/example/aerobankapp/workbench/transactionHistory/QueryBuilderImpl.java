package com.example.aerobankapp.workbench.transactionHistory;

import com.example.aerobankapp.exceptions.NullHistoryCriteriaException;
import com.example.aerobankapp.workbench.transactionHistory.criteria.HistoryCriteria;
import com.example.aerobankapp.workbench.transactionHistory.queries.QueryConstants;
import com.example.aerobankapp.workbench.transactions.TransactionType;
import org.hibernate.query.internal.ScrollableResultsIterator;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilderImpl implements QueryBuilder
{

    @Override
    public String getQueryFromHistoryCriteria(HistoryCriteria historyCriteria) {
        if(historyCriteria == null){
            throw new IllegalArgumentException("History criteria must not be null");
        }
        StringBuilder queryWithTable = buildTableStatement(historyCriteria.transactionType());

        List<String> queryConditions = buildQueryConditions(historyCriteria, queryWithTable);
        return "";
    }


    public StringBuilder buildTableStatement(TransactionType transactionType){
        StringBuilder query = new StringBuilder("SELECT e FROM");
        switch(transactionType){
            case DEPOSIT:
                query.append("DepositsEntity e");
                break;
            case WITHDRAW:
                query.append("WithdrawEntity e");
                break;
            case TRANSFER:
                query.append("TransferEntity e");
                break;
            case PURCHASE:
                query.append("PurchaseEntity e");
                break;
            default:
                throw new IllegalArgumentException("Invalid TransactionType found.");
        }
        return query;
    }

    @Override
    public List<String> buildQueryConditions(HistoryCriteria criteria, StringBuilder query){
        if(criteria == null){
            throw new NullHistoryCriteriaException("Null History Criteria caught.");
        }

        List<String> conditions = new ArrayList<>();
        if(criteria.description() != null && !criteria.description().isEmpty()){
          //  conditions.add("WHERE");
            conditions.add("e.description LIKE :descr");
        }
        if(criteria.description() != null && !criteria.description().isEmpty() && criteria.startDate() != null){
            conditions.add("e.description LIKE :descr AND e.scheduledDate :startDate");
        }
        if(!conditions.isEmpty()){
            conditions.add(0, "WHERE");
            query.append(String.join(" ", conditions));
        }
        return conditions;
    }



}
