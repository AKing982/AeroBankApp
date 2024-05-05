package com.example.aerobankapp.workbench.transactionHistory.queries;

import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.entity.TransferEntity;
import com.example.aerobankapp.entity.WithdrawEntity;
import com.example.aerobankapp.workbench.transactionHistory.QueryBuilder;
import com.example.aerobankapp.workbench.transactionHistory.criteria.HistoryCriteria;
import com.example.aerobankapp.workbench.transactions.TransactionType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

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
    private QueryBuilder queryBuilder;
    private Logger LOGGER = LoggerFactory.getLogger(TransactionHistoryQueryRunner.class);

    public TransactionHistoryQueryRunner(EntityManager entityManager, QueryBuilder queryBuilder){
        this.entityManager = entityManager;
        this.queryBuilder = queryBuilder;
    }

    public List<?> runQuery(HistoryCriteria criteria){
        String jpqlQuery = queryBuilder.getQueryFromHistoryCriteria(criteria);
        LOGGER.info("Running Query: " + jpqlQuery);
        TypedQuery<?> query = entityManager.createQuery(jpqlQuery, determineEntityClass(criteria.transactionType()));

        setQueryParameters(query, criteria);
        query.setParameter("userID", criteria.userID());
        String sqlQuery = query.unwrap(org.hibernate.query.Query.class).getQueryString();
        LOGGER.info("Full Query: " + sqlQuery);
        return query.getResultList();
    }

    public List<?> runDefaultQueryWithUserID(int userID){
        String jpqlQuery = queryBuilder.getDefaultUserQuery();
        LOGGER.info("Running Query; " + jpqlQuery);
        TypedQuery<?> query = entityManager.createQuery(jpqlQuery, DepositsEntity.class);

        query.setParameter("userID", userID);
        return query.getResultList();
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
