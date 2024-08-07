package com.example.aerobankapp.workbench.transactionHistory.queries;

import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.entity.TransferEntity;
import com.example.aerobankapp.entity.WithdrawEntity;
import com.example.aerobankapp.workbench.transactionHistory.QueryBuilderImpl;
import com.example.aerobankapp.workbench.transactionHistory.criteria.HistoryCriteria;
import com.example.aerobankapp.workbench.transactions.TransactionType;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static com.example.aerobankapp.workbench.utilities.dateUtil.DateUtil.*;

/**
 * This class will take the parsed list of TransactionHistory criteria and execute the
 * corresponding query
 * @author alexking
 */
@Component
public class TransactionHistoryQueryRunner
{
    @PersistenceContext
    private EntityManager entityManager;
    private QueryBuilderImpl queryBuilder;
    private Logger LOGGER = LoggerFactory.getLogger(TransactionHistoryQueryRunner.class);

    public TransactionHistoryQueryRunner(EntityManager entityManager, QueryBuilderImpl queryBuilder){
        this.entityManager = entityManager;
        this.queryBuilder = queryBuilder;
    }

    public List<?> runQuery(HistoryCriteria criteria){
        String jpqlQuery = queryBuilder.getQueryFromCriteria(criteria);
        LOGGER.info("Running Query: " + jpqlQuery);
        TypedQuery<?> query = entityManager.createQuery(jpqlQuery, determineEntityClass(criteria.transactionType()));

        setQueryParameters(query, criteria);
        query.setParameter("userID", criteria.userID());
        String sqlQuery = query.unwrap(org.hibernate.query.Query.class).getQueryString();
        LOGGER.info("Full Query: " + sqlQuery);
        return query.getResultList();
    }

    public List<?> runDefaultQueryWithUserID(int userID){
        String jpqlQuery = queryBuilder.getDefaultQuery();
        LOGGER.info("Running Query; " + jpqlQuery);
        TypedQuery<?> query = entityManager.createQuery(jpqlQuery, DepositsEntity.class);

        query.setParameter("userID", userID);
        return query.getResultList();
    }

    private Object getResultSetWithUserID(String query, int userID){
        return entityManager.createQuery(query, Object.class)
                .setParameter("userID", userID)
                .getSingleResult();
    }

    private Object setTotalAmountByWeekQuery(String query, int userID){
        LocalDate weekStart = getStartOfCurrentWeek();
        LocalDate weekEnd = getEndOfCurrentWeek();
        LOGGER.info("Week Start: " + weekStart);
        LOGGER.info("Week End: " + weekEnd);
        return entityManager.createQuery(query, BigDecimal.class)
                .setParameter("startDate", getStartOfCurrentWeek())
                .setParameter("endDate", getEndOfCurrentWeek())
                .setParameter("userID", userID)
                .getSingleResult();
    }

    private BigDecimal getTotalAmountResult(String query, int userID){
        return entityManager.createQuery(query, BigDecimal.class)
                .setParameter("startDate", getCurrentMonthStartDate())
                .setParameter("endDate", getCurrentMonthEndDate())
                .setParameter("userID", userID)
                .getSingleResult();
    }

    private BigDecimal getTotalAmountTransferResultSet(final String query, final int userID){
        return entityManager.createQuery(query, BigDecimal.class)
                .setParameter("userID", userID)
                .getSingleResult();
    }

    public BigDecimal getQuerySum(final BigDecimal total1, final BigDecimal total2, final BigDecimal total3){
        if(total1 == null){
            return total2.add(total3);
        }
        if(total2 == null){
            return total1.add(total3);
        }
        if(total3 == null){
            return total1.add(total2);
        }
        return total1.add(total2).add(total3);
    }

    public String getFormattedTotalFromQuery(final BigDecimal total){
        NumberFormat totalWithCurrency = NumberFormat.getCurrencyInstance(Locale.US);
        return totalWithCurrency.format(total);
    }

    private Object buildPendingQueryParameters(String query, int userID){
        return entityManager.createQuery(query, Object.class)
                .setParameter("userID", userID)
                .setParameter("status", TransactionStatus.PENDING)
                .getSingleResult();
    }

    private Long getQuerySumAsLong(Long param1, Long param2, Long param3){
        return param1 + param2 + param3;
    }

    public Long runPendingTransactionCountQuery(int userID){
        String pendingDepositsQuery = queryBuilder.buildPendingTransactionCountQuery(TransactionType.DEPOSIT);
        Long pendingDeposits = (Long) buildPendingQueryParameters(pendingDepositsQuery, userID);

        String pendingWithdrawalsQuery = queryBuilder.buildPendingTransactionCountQuery(TransactionType.WITHDRAW);
        Long pendingWithdrawals = (Long) buildPendingQueryParameters(pendingWithdrawalsQuery, userID);

        String pendingTransfersQuery = queryBuilder.buildPendingTransactionCountQuery(TransactionType.TRANSFER);
        Long pendingTransfers = (Long) buildPendingQueryParameters(pendingTransfersQuery, userID);

        return getQuerySumAsLong(pendingDeposits, pendingWithdrawals, pendingTransfers);
    }

    public double getSumAsDouble(double param1, double param2, double param3){
        return param1 + param2 + param3;
    }

    public String getAverageFormattedValueFromDouble(double total){
        NumberFormat totalWithCurrency = NumberFormat.getCurrencyInstance(Locale.US);
        return totalWithCurrency.format(total);
    }

    public String runAverageTransactionValueQuery(int userID){
        String averageDepositsQuery = queryBuilder.buildAverageTransactionValueQuery(TransactionType.DEPOSIT);
        double averageDepositValue = (double) getResultSetWithUserID(averageDepositsQuery, userID);
        LOGGER.info("Average Deposit Query: " + averageDepositsQuery);

        String averageWithdrawQuery = queryBuilder.buildAverageTransactionValueQuery(TransactionType.WITHDRAW);
        LOGGER.info("Average Withdraw Query: " + averageWithdrawQuery);
        double averageWithdrawValue = (double) getResultSetWithUserID(averageWithdrawQuery, userID);

        String averageTransferQuery = queryBuilder.buildAverageTransactionValueQuery(TransactionType.TRANSFER);
        LOGGER.info("Average Transfer Query: " + averageTransferQuery);
        double averageTransferValue = (double) getResultSetWithUserID(averageTransferQuery, userID);

        double totalAverageValue = getSumAsDouble(averageDepositValue, averageWithdrawValue, averageTransferValue);

        return getAverageFormattedValueFromDouble(totalAverageValue);
    }

    public int runTotalTransactionCountQuery(int userID){
        String totalDeposits = queryBuilder.buildTotalTransactionCountQuery(TransactionType.DEPOSIT);
        LOGGER.info("Total Deposit Count Query: " + totalDeposits);
        Long depositTotal = (Long) getResultSetWithUserID(totalDeposits, userID);
        LOGGER.info("Deposit Count: " + depositTotal);

        String totalWithdraws = queryBuilder.buildTotalTransactionCountQuery(TransactionType.WITHDRAW);
        LOGGER.info("Total Withdraw Count Query: " + totalWithdraws);
        Long withdrawTotal = (Long) getResultSetWithUserID(totalWithdraws, userID);
        LOGGER.info("Withdraw Count: " + withdrawTotal);

        String totalTransfers = queryBuilder.buildTotalTransactionCountQuery(TransactionType.TRANSFER);
        LOGGER.info("Total Transfer Count Query: " + totalTransfers);
        Long transferCount = (Long) getResultSetWithUserID(totalTransfers, userID);
        LOGGER.info("Transfer Count: " + transferCount);

        Long totalCount = getQuerySumAsLong(depositTotal, withdrawTotal, transferCount);
        return Integer.parseInt(totalCount.toString());
    }

    public int runTotalTransactionsThisWeekQuery(int userID){
        String totalDepositTransactions = queryBuilder.buildTotalWeeklyCountQuery(TransactionType.DEPOSIT);
        LOGGER.info("Total Weekly Deposits Query: " + totalDepositTransactions);
        Long depositTotal = (Long) setTotalAmountByWeekQuery(totalDepositTransactions, userID);
        LOGGER.info("Deposit Total: " + depositTotal);

        String totalWithdrawTransactions = queryBuilder.buildTotalWeeklyCountQuery(TransactionType.WITHDRAW);
        LOGGER.info("Total Weekly Withdraws Query: " + totalWithdrawTransactions);
        Long withdrawTotal = (Long) setTotalAmountByWeekQuery(totalWithdrawTransactions, userID);
        LOGGER.info("Withdraw Total: " + withdrawTotal);

        String totalTransferTransactions = queryBuilder.buildTotalWeeklyCountQuery(TransactionType.TRANSFER);
        LOGGER.info("Total Weekly Transfer Query: " + totalTransferTransactions);
        Long transferTotal = (Long) setTotalAmountByWeekQuery(totalTransferTransactions, userID);
        LOGGER.info("Transfer Total: " + transferTotal);

        Long total = getQuerySumAsLong(depositTotal, withdrawTotal, transferTotal);
        LOGGER.info("Total Transaction Count This week: " + total);

        return Integer.parseInt(total.toString());
    }

    public String runTotalAmountTransferredQuery(int userID){
        String totalTransferredQuery = queryBuilder.buildTotalAmountTransferredQuery();
        BigDecimal totalAmount = getTotalAmountTransferResultSet(totalTransferredQuery, userID);
        return getFormattedTotalFromQuery(totalAmount);
    }

    public BigDecimal getTotalSumResultByQuery(TransactionType transactionType, int userID){
        String totalSumQuery = queryBuilder.buildTotalSumScheduledDateStatement(transactionType);
        return getTotalAmountResult(totalSumQuery, userID);
    }

    public String runTotalAmountForMonthQuery(int userID){

        BigDecimal totalAmountForDeposits = getTotalSumResultByQuery(TransactionType.DEPOSIT, userID);

        BigDecimal totalAmountForWithdrawals = getTotalSumResultByQuery(TransactionType.WITHDRAW, userID);

        BigDecimal totalAmountForTransfers = getTotalSumResultByQuery(TransactionType.TRANSFER, userID);

        BigDecimal total = getQuerySum(totalAmountForDeposits, totalAmountForWithdrawals, totalAmountForTransfers);
        return getFormattedTotalFromQuery(total);
    }

    private Class<?> determineEntityClass(TransactionType type) {
    return switch (type) {
        case DEPOSIT -> DepositsEntity.class;
        case WITHDRAW -> WithdrawEntity.class;
        case TRANSFER -> TransferEntity.class;
        default -> throw new IllegalArgumentException("Unknown transaction type: " + type);
    };
}

    private void setQueryParameters(Query query, HistoryCriteria criteria){
        if (criteria.description() != null && !criteria.description().isEmpty()) {
            query.setParameter("descr", "%" + criteria.description() + "%");
        }
        if (criteria.startDate() != null) {
            query.setParameter("startDate", criteria.startDate());
        }
        if (criteria.endDate() != null) {
            query.setParameter("endDate", criteria.endDate());
        }
        if (criteria.minAmount() != null) {
            query.setParameter("minAmount", criteria.minAmount());
        }
        if (criteria.maxAmount() != null) {
            query.setParameter("maxAmount", criteria.maxAmount());
        }
        if (criteria.status() != null) {
            query.setParameter("status", criteria.status());
        }

    }
}
