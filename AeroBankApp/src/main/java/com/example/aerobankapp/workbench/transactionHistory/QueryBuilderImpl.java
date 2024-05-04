package com.example.aerobankapp.workbench.transactionHistory;

import com.example.aerobankapp.exceptions.NullHistoryCriteriaException;
import com.example.aerobankapp.workbench.transactionHistory.criteria.HistoryCriteria;
import com.example.aerobankapp.workbench.transactions.TransactionType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QueryBuilderImpl implements QueryBuilder
{

    @Override
    public String getQueryFromHistoryCriteria(HistoryCriteria historyCriteria) {
        if(historyCriteria == null){
            throw new IllegalArgumentException("History criteria must not be null");
        }
        StringBuilder queryWithTable = buildTableStatement(historyCriteria.transactionType());

        List<String> queryConditions = buildQueryConditions(historyCriteria, queryWithTable);
        return queryWithTable.toString();
    }


    public StringBuilder buildTableStatement(TransactionType transactionType){
        StringBuilder query = new StringBuilder("SELECT e FROM ");

        switch(transactionType){
            case DEPOSIT:
                query.append("DepositsEntity e");
                query.append(" ");
                break;
            case WITHDRAW:
                query.append("WithdrawEntity e");
                query.append(" ");
                break;
            case TRANSFER:
                query.append("TransferEntity e");
                query.append(" ");
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
        // The query will start with a description
        if(criteria.description() != null && !criteria.description().isEmpty()){
            conditions.add("e.description LIKE :descr");
            if(criteria.startDate() != null && criteria.endDate() == null){
                conditions.add("AND e.scheduledDate =:startDate");
            }
            if(criteria.endDate() != null && criteria.startDate() != null){
                conditions.add("AND e.scheduledDate BETWEEN :startDate AND :endDate");
            }
            if(criteria.status() != null){
                conditions.add("AND e.status =:status");
            }
            if(criteria.minAmount() != null && criteria.maxAmount() == null){
                conditions.add("AND e.amount =:minAmount");
            }
            if(criteria.minAmount() != null && criteria.maxAmount() != null){
                conditions.add("AND (e.amount >= :minAmount AND e.amount <= :maxAmount)");
            }
        }
        // If the description is empty, run the following
        else{
            if(criteria.startDate() != null && criteria.endDate() != null){
                conditions.add("e.scheduledDate BETWEEN :startDate AND :endDate");
            }
            if(criteria.startDate() != null && criteria.endDate() == null){
                conditions.add("e.scheduledDate =:startDate");
            }
            if(criteria.minAmount() != null && criteria.maxAmount() != null){
                conditions.add("AND (e.amount >= :minAmount AND e.amount <= :maxAmount)");
            }

            if(criteria.minAmount() == null && criteria.maxAmount() != null){
                conditions.add("AND e.amount =:maxAmount");
            }
            if(criteria.minAmount() != null && criteria.maxAmount() == null){
                conditions.add("e.amount =:minAmount");
            }
            if(criteria.status() != null){
                conditions.add("AND e.status =:status");
            }
        }
//        if(criteria.description() != null && !criteria.description().isEmpty()){
//            conditions.add("e.description LIKE :descr");
//        }
//        if(criteria.endDate() != null && criteria.startDate() != null){
//            conditions.add("AND e.scheduledDate BETWEEN :startDate AND :endDate");
//        }
//        if(criteria.startDate() != null && criteria.endDate() == null){
//            conditions.add("AND e.scheduledDate :startDate");
//        }
//        if(criteria.status() != null){
//            conditions.add("AND e.status =:status");
//        }
//        if(criteria.maxAmount() != null && criteria.minAmount() != null){
//            conditions.add("AND (e.amount >= :minAmount AND e.amount <= :maxAmount)");
//        }
//        if(criteria.minAmount() != null && criteria.maxAmount() == null){
//            conditions.add("AND e.amount =:minAmount");
//        }


        if(!conditions.isEmpty()){
            conditions.add(0, "WHERE");
            query.append(String.join(" ", conditions));
        }
        return conditions;
    }



}
