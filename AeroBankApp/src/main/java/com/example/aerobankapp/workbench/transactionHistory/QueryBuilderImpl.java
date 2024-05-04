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
       // originalCondition(criteria, conditions, query);
        boolean firstCondition = true;
        revised(criteria, conditions, query);
//        addDescriptionCondition(criteria, conditions, firstCondition);
//        addAmountCondition(criteria, conditions, firstCondition);
//        addStatusCondition(criteria, conditions, firstCondition);
//        addTransferTypeCondition(criteria, conditions, firstCondition);
//        addScheduledTimeCondition(criteria, conditions, firstCondition);

        if(!conditions.isEmpty()){
            conditions.add(0, "WHERE");
            conditions.add("AND e.user.userID =:userID");
            query.append(String.join(" ", conditions));
        }

        return conditions;
    }

    public void revised(HistoryCriteria criteria, List<String> conditions, StringBuilder query){
        // Is description the first statement?
        if(!criteria.description().isEmpty()){
            addANDStatement(conditions, "e.description LIKE :descr", true);
            if(criteria.status() != null){
                addANDStatement(conditions, "e.status =:status", false);
                if(criteria.minAmount() != null){
                    addANDStatement(conditions, "e.amount =:minAmount", false);
                    if(criteria.maxAmount() != null){
                        addANDStatement(conditions, "e.amount =:maxAmount", false);
                        if(criteria.startDate() != null){
                            addANDStatement(conditions, "e.scheduledDate =:startDate", false);
                            if(criteria.endDate() != null){
                                addANDStatement(conditions, "e.scheduledDate =:endDate", false);
                                if(criteria.scheduledTime() != null){
                                    addANDStatement(conditions, "e.scheduledTime =:scheduledTime", false);
                                    if(criteria.transferType() != null){
                                        addANDStatement(conditions, "e.transferType =:transferType", false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(!conditions.isEmpty()){
            conditions.add(0, "WHERE ");
            conditions.add("AND e.user.userID =:userID");
            query.append(String.join(" ", conditions));
        }

    }

    private void original(HistoryCriteria criteria, List<String> conditions, StringBuilder query){
        boolean firstCondition = true; // Flag to check if it's the first condition

        if (criteria.description() != null && !criteria.description().isEmpty()) {
            addANDStatement(conditions, "e.description LIKE :descr", firstCondition);
            firstCondition = false; // Any subsequent condition is not the first

            if (criteria.startDate() != null && criteria.endDate() == null) {
                addANDStatement(conditions, "e.scheduledDate =:startDate", firstCondition);
            }
            if (criteria.endDate() != null && criteria.startDate() != null) {
                addANDStatement(conditions, "e.scheduledDate BETWEEN :startDate AND :endDate", firstCondition);
            }
            if (criteria.status() != null) {
                addANDStatement(conditions, "e.status =:status", firstCondition);
            }
            if (criteria.minAmount() != null && criteria.maxAmount() == null) {
                addANDStatement(conditions, "e.amount =:minAmount", firstCondition);
            }
            if (criteria.minAmount() != null && criteria.maxAmount() != null) {
                addANDStatement(conditions, "(e.amount >= :minAmount AND e.amount <= :maxAmount)", firstCondition);
            }
        } else {
            // When description is null or empty, handle other conditions
            if (criteria.startDate() != null && criteria.endDate() != null) {
                addANDStatement(conditions, "e.scheduledDate BETWEEN :startDate AND :endDate", firstCondition);
                firstCondition = false;
            }
            if (criteria.startDate() != null && criteria.endDate() == null) {
                addANDStatement(conditions, "e.scheduledDate =:startDate", firstCondition);
            }
            if (criteria.minAmount() != null && criteria.maxAmount() != null) {
               addANDStatement(conditions, "(e.amount >= :minAmount AND e.amount <= :maxAmount)", firstCondition);
            }
            if (criteria.minAmount() == null && criteria.maxAmount() != null) {
                addANDStatement(conditions, "e.amount =:maxAmount", firstCondition);
            }
            if (criteria.minAmount() != null && criteria.maxAmount() == null) {
                addANDStatement(conditions, "e.amount =:minAmount", firstCondition);
            }
            if (criteria.status() != null) {
                addANDStatement(conditions, "e.status =:status", firstCondition);
            }
        }
    }

    private void originalCondition(HistoryCriteria criteria, List<String> conditions, StringBuilder query){
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
    }

    private void buildFirstConditionStatement(HistoryCriteria criteria, List<String> conditions){
        // Check if description is empty as its the first condition in the where clause
        if(!criteria.description().isEmpty()){
            boolean isFirst = true;
            addANDStatement(conditions, "e.description LIKE :descr", isFirst);
        }
    }

    private void addDescriptionCondition(HistoryCriteria criteria, List<String> conditions, boolean isFirstCondition){
        if(criteria.description() != null && !criteria.description().isEmpty()){
            addANDStatement(conditions, "e.description LIKE :descr", true);
           // conditions.add("e.description LIKE :descr");
            addStartDateAndEndDateCondition(criteria, conditions, isFirstCondition);
        }
    }

    private void addStartDateAndEndDateCondition(HistoryCriteria criteria, List<String> conditions, boolean isFirstCondition){

        if(criteria.startDate() != null){
            if(criteria.endDate() != null){
                conditions.add("e.scheduledDate BETWEEN :startDate AND :endDate");
            }else{
                conditions.add("AND e.scheduledDate =:startDate");
            }
        }
    }

    private void addStatusCondition(HistoryCriteria criteria, List<String> conditions, boolean isFirstCondition){
        if(criteria.status() != null){
            conditions.add("e.status =:status");
        }
    }

    private void addAmountCondition(HistoryCriteria criteria, List<String> conditions, boolean isFirstCondition){
        if(criteria.minAmount() != null){
            if(criteria.maxAmount() != null){
                conditions.add("e.amount BETWEEN :minAmount AND :maxAmount");
            }else{
                conditions.add("e.amount =:minAmount");
            }
        }
    }

    private void addScheduledTimeCondition(HistoryCriteria criteria, List<String> conditions, boolean isFirstCondition){
        if(criteria.scheduledTime() != null){
            conditions.add("e.scheduledTime =:scheduledTime");
        }
    }

    private void addTransferTypeCondition(HistoryCriteria criteria, List<String> conditions, boolean isFirstCondition){
        if(criteria.transferType() != null){
            conditions.add("e.transferType =:transferType");
        }
    }

    private void addANDStatement(List<String> conditions, String condition, boolean isFirstCondition){
        String newCondition = "";
        if(!isFirstCondition){
            newCondition  = "AND " + condition;
            conditions.add(newCondition);
        }
        conditions.add(condition);
    }
}
