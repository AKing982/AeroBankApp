package com.example.aerobankapp.workbench.transactionHistory;

import com.example.aerobankapp.model.SQLCondition;
import com.example.aerobankapp.model.SQLOperand;
import com.example.aerobankapp.model.SQLSelect;
import com.example.aerobankapp.model.SQLTable;
import com.example.aerobankapp.workbench.transactionHistory.criteria.HistoryCriteria;
import com.example.aerobankapp.workbench.transactions.TransactionType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QueryBuilderImpl
{

    public QueryBuilderImpl(){
        // NOT THE CONSTRUCTOR YOU SEEK
    }

    public String getQueryFromCriteria(HistoryCriteria criteria){
        if(criteria == null){
            throw new IllegalArgumentException("History criteria must not be null");
        }

        String BASIC_SELECTION = "e";
        SQLSelect sqlSelect = buildSelectStatement(BASIC_SELECTION);
        SQLTable sqlTable = buildTableQueryStatement(criteria.transactionType());
        SQLOperand where = new SQLOperand("AND");
        buildConditionsStatementsFromCriteria(criteria, where);
        return buildSQLQuery(sqlSelect, sqlTable, where);
    }

    private SQLSelect getSQLSelect(){
        return new SQLSelect();
    }

    public String getSelectSQLQuery(final SQLSelect sqlSelect){
        return sqlSelect.buildSQL();
    }

    public SQLSelect buildSelectStatement(final String selection){
        SQLSelect sqlSelect = getSQLSelect();
        sqlSelect.addSelection(selection);
        return sqlSelect;
    }

    public SQLSelect buildSelectStatementFromSelections(final List<String> selections){
        SQLSelect sqlSelect = getSQLSelect();
        selections.forEach(sqlSelect::addSelection);
        return sqlSelect;
    }


    public SQLTable buildTableQueryStatement(TransactionType transactionType){
        SQLTable sqlTable;
        switch(transactionType){
            case DEPOSIT:
                sqlTable = new SQLTable("DepositsEntity", "e");
                break;
            case WITHDRAW:
                sqlTable = new SQLTable("WithdrawEntity", "e");
                break;
            case TRANSFER:
                sqlTable = new SQLTable("TransferEntity", "e");
                break;
            default:
                throw new IllegalArgumentException("Invalid TransactionType found.");
        }
        return sqlTable;
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

    public SQLOperand buildQueryWhereConditions(final HistoryCriteria criteria){
       SQLOperand whereConditions = new SQLOperand("AND");
       if(criteria.description() != null && !criteria.description().isEmpty()){
           whereConditions.addComponent(new SQLCondition("e.description", "LIKE", ":descr"));
       }
       return whereConditions;
    }

    public String buildSQLQuery(final SQLSelect select, final SQLTable table, final SQLOperand where){
        String whereClause = where.buildQueryComponent();
        if(whereClause.isEmpty()){
            return select.buildSQL() + " " + table.buildSQL();
        }
        return select.buildSQL() + " " + table.buildSQL() + " WHERE " + whereClause;
    }


    @Deprecated
    public String getDefaultUserQuery(){
        StringBuilder tableStatement = buildTableStatement(TransactionType.DEPOSIT);

        StringBuilder queryWithWhereClause = getDefaultUserWhereClause(tableStatement);
        return queryWithWhereClause.toString();
    }

    public String getDefaultQuery(){
        SQLTable depositsTable = buildTableQueryStatement(TransactionType.DEPOSIT);
        SQLOperand userClause = buildDefaultUserClause();
        SQLSelect sqlSelect = buildSelectStatement("e");

        return buildSQLQuery(sqlSelect, depositsTable, userClause);
    }

    public SQLOperand buildDefaultUserClause(){
        SQLOperand sqlOperand = new SQLOperand("");
        sqlOperand.addComponent(buildSQLCondition("e.user.userID", "=", ":userID"));
        return sqlOperand;
    }

    @Deprecated
    public StringBuilder getDefaultUserWhereClause(StringBuilder query){
        List<String> conditions = new ArrayList<>();
        conditions.add(0, "WHERE");
        conditions.add("e.user.userID=:userID");
        query.append(String.join(" ", conditions));
        return query;
    }

    public SQLCondition buildSQLCondition(String field, String operator, String value){
        return new SQLCondition(field, operator, value);
    }

    public SQLCondition buildSQLConditionWithRange(String rangeQuery){
        return new SQLCondition(rangeQuery);
    }

    public SQLOperand buildQueryWithStartingAndOperand(HistoryCriteria criteria){
        SQLOperand whereClause = new SQLOperand("AND");
        buildConditionsStatementsFromCriteria(criteria, whereClause);
        return whereClause;
    }

    public void buildConditionsStatementsFromCriteria(final HistoryCriteria criteria, final SQLOperand whereClause){

        if(criteria.description() != null && !criteria.description().isEmpty()){
            whereClause.addComponent(buildSQLCondition("e.description", "LIKE", ":descr"));
            addStartDateAndNoEndDateCondition(criteria, whereClause);
            addStartAndEndDateCondition(criteria, whereClause);
            addStatusCriteriaCondition(criteria, whereClause);
            addMinAmountAndMaxAmountCondition(criteria, whereClause);
            addMinAmountNoMaxAmountCondition(criteria, whereClause);
            addMaxAmountNoMinAmountCondition(criteria, whereClause);
            addTransferTypeCondition(criteria, whereClause);

            addUserClauseCondition(criteria, whereClause);

        }else{
            addStatusCriteriaCondition(criteria, whereClause);
            addStartAndEndDateCondition(criteria, whereClause);
            addStartDateAndNoEndDateCondition(criteria, whereClause);
            addMinAmountAndMaxAmountCondition(criteria, whereClause);
            addMinAmountNoMaxAmountCondition(criteria, whereClause);
            addMaxAmountNoMinAmountCondition(criteria, whereClause);
            addTransferTypeCondition(criteria, whereClause);
            addUserClauseCondition(criteria, whereClause);
        }
    }

    public void addUserClauseCondition(HistoryCriteria criteria, SQLOperand where){
        if(criteria.userID() > 0){
            where.addComponent(buildSQLCondition("e.user.userID", "=", ":userID"));
        }
    }

    public void addMinAmountNoMaxAmountCondition(HistoryCriteria criteria, SQLOperand where){
        if(criteria.minAmount() != null && criteria.maxAmount() == null){
            where.addComponent(buildSQLCondition("e.amount", "=", ":minAmount"));
        }
    }

    public void addTransferTypeCondition(HistoryCriteria criteria, SQLOperand where){
        if(criteria.transferType() != null){
            where.addComponent(buildSQLCondition("e.transferType", "=", ":transferType"));
        }
    }

    public void addMinAmountAndMaxAmountCondition(HistoryCriteria criteria, SQLOperand where){
        if(criteria.minAmount() != null && criteria.maxAmount() != null){
            where.addComponent(buildSQLCondition("(e.amount", ">=", ":minAmount"));
            where.addComponent(buildSQLCondition("e.amount", "<=", ":maxAmount)"));
        }
    }

    public void addMaxAmountNoMinAmountCondition(HistoryCriteria criteria, SQLOperand where){
        if(criteria.maxAmount() != null && criteria.minAmount() == null){
            where.addComponent(buildSQLCondition("e.amount", "=", ":maxAmount"));
        }
    }

    public void addStatusCriteriaCondition(HistoryCriteria criteria, SQLOperand where){
        if(criteria.status() != null){
            where.addComponent(buildSQLCondition("e.status", "=", ":status"));
        }
    }

    public void addStartAndEndDateCondition(HistoryCriteria criteria, SQLOperand where){
        if(criteria.startDate() != null && criteria.endDate() != null){
            where.addComponent(buildSQLCondition("e.scheduledDate", "BETWEEN", ":startDate AND :endDate"));
        }
    }

    public void addStartDateAndNoEndDateCondition(HistoryCriteria criteria, SQLOperand where){
        if(criteria.startDate() != null && criteria.endDate() == null){
            where.addComponent(buildSQLCondition("e.scheduledDate", "=", ":startDate"));
        }
    }

    public void addEndDateAndNoStartDateCondition(HistoryCriteria criteria, SQLOperand where){
        if(criteria.endDate() != null && criteria.startDate() == null){
            where.addComponent(buildSQLCondition("e.scheduledDate", "=", ":endDate"));
        }
    }
}
