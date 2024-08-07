package com.example.aerobankapp.workbench.transactionHistory;

import com.example.aerobankapp.model.SQLCondition;
import com.example.aerobankapp.model.SQLOperand;
import com.example.aerobankapp.model.SQLSelect;
import com.example.aerobankapp.model.SQLTable;
import com.example.aerobankapp.workbench.sql.QueryConstants;
import com.example.aerobankapp.workbench.transactionHistory.criteria.HistoryCriteria;
import com.example.aerobankapp.workbench.transactions.TransactionType;
import org.hibernate.annotations.processing.SQL;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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


    public String buildTotalAmountTransferredQuery(){
        SQLSelect selectSum = getSumAmountSelectParameter();
        SQLTable transferTable = buildTableQueryStatement(TransactionType.TRANSFER);
        SQLOperand where = new SQLOperand("");
        where.addComponent(buildSQLCondition("e.fromUser.userID", "=", ":userID"));

        return buildSQLQuery(selectSum, transferTable, where);
    }

    private SQLSelect getCountSelectParameter(){
        return buildSelectStatement(QueryConstants.COUNT);
    }

    private SQLSelect getSumAmountSelectParameter(){
        return buildSelectStatement(QueryConstants.SUM_AMOUNT);
    }

    public String buildPendingTransactionCountQuery(final TransactionType transactionType){
        SQLSelect averageSelect = getCountSelectParameter();
        SQLTable table = buildTableQueryStatement(transactionType);
        SQLOperand where = new SQLOperand("AND");
        buildWhereClauseWithStatusCondition(where);
        buildWhereClauseTransferType(transactionType, where);
        return buildSQLQuery(averageSelect, table, where);
    }

    public String buildCountQuery(TransactionType type){
        return "";
    }

    public String buildCountQueryForTransfersForMonth(){
        SQLSelect countSelect = getCountSelectParameter();
        SQLTable table = buildTableQueryStatement(TransactionType.TRANSFER);
        SQLOperand where = buildScheduledDateWhereConditions(TransactionType.TRANSFER);
        return buildSQLQuery(countSelect, table, where);
    }

    public void buildWhereClauseWithStatusCondition(SQLOperand where){
        where.addComponent(buildSQLCondition("e.status", "=", ":status"));
    }

    public String buildAverageTransactionValueQuery(final TransactionType transactionType){
        SQLSelect averageSelect = buildSelectStatement(QueryConstants.AVG_AMOUNT);
        SQLTable table = buildTableQueryStatement(transactionType);
        SQLOperand where = buildWhereClauseWithTransferCondition(transactionType);
        return buildSQLQuery(averageSelect, table, where);
    }

    public void buildWhereClauseTransferType(final TransactionType transactionType, SQLOperand where){
        if(transactionType.equals(TransactionType.TRANSFER)){
            where.addComponent(buildSQLCondition("e.fromUser.userID", "=", ":userID"));
        }else{
            where.addComponent(buildSQLCondition("e.user.userID", "=", ":userID"));
        }
    }

    public SQLOperand buildWhereClauseWithTransferCondition(final TransactionType transactionType){
        SQLOperand where = new SQLOperand("");
        if(transactionType.equals(TransactionType.TRANSFER)){
            where.addComponent(buildSQLCondition("e.fromUser.userID", "=", ":userID"));
        }else{
            where.addComponent(buildSQLCondition("e.user.userID", "=", ":userID"));
        }
        return where;
    }

    public String buildTotalWeeklyCountQuery(final TransactionType transactionType){
        SQLSelect countSelect = getCountSelectParameter();
        SQLTable table = buildTableQueryStatement(transactionType);
        SQLOperand where = buildScheduledDateWhereConditions(transactionType);
        return buildSQLQuery(countSelect, table, where);
    }

    public String buildTotalTransactionCountQuery(final TransactionType transactionType){
        SQLSelect countSelect = getCountSelectParameter();
        SQLTable table = buildTableQueryStatement(transactionType);
        SQLOperand where = new SQLOperand("");
        buildWhereClauseTransferType(transactionType, where);
        return buildSQLQuery(countSelect, table, where);
    }

    public String buildTotalSumScheduledDateStatement(final TransactionType transactionType){
        SQLSelect selectSum = getSumAmountSelectParameter();
        SQLTable table = buildTableQueryStatement(transactionType);
        SQLOperand where = buildScheduledDateWhereConditions(transactionType);

        return buildSQLQuery(selectSum, table, where);
    }

    public SQLOperand buildScheduledDateWhereConditions(final TransactionType transactionType){
        SQLOperand where = new SQLOperand(QueryConstants.AND);
        where.addComponent(buildSQLCondition("e.scheduledDate", "BETWEEN", ":startDate AND :endDate"));
        if(transactionType.equals(TransactionType.TRANSFER)){
            where.addComponent(buildSQLCondition("e.fromUser.userID", "=", ":userID"));
        }else{
            where.addComponent(buildSQLCondition("e.user.userID", "=", ":userID"));
        }
        return where;
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

    @Deprecated
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
       SQLOperand whereConditions = new SQLOperand(QueryConstants.AND);
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
        return select.buildSQL() + " " + table.buildSQL() + QueryConstants.WHERE + whereClause;
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

            addTransactionTransferSelectionCondition(criteria, whereClause);

        }else{
            addStatusCriteriaCondition(criteria, whereClause);
            addStartAndEndDateCondition(criteria, whereClause);
            addStartDateAndNoEndDateCondition(criteria, whereClause);
            addMinAmountAndMaxAmountCondition(criteria, whereClause);
            addMinAmountNoMaxAmountCondition(criteria, whereClause);
            addMaxAmountNoMinAmountCondition(criteria, whereClause);
            addTransferTypeCondition(criteria, whereClause);
            addTransactionTransferSelectionCondition(criteria, whereClause);
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

    public void addTransactionTransferSelectionCondition(HistoryCriteria criteria, SQLOperand where){
        if(criteria.transactionType().equals(TransactionType.TRANSFER)){
            where.addComponent(buildSQLCondition("e.fromUser.userID", "=", ":userID"));
        }else{
            where.addComponent(buildSQLCondition("e.user.userID", "=",":userID"));
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
