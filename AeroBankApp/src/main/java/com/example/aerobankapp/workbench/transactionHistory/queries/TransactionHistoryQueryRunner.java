package com.example.aerobankapp.workbench.transactionHistory.queries;

import com.example.aerobankapp.model.HistoryQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class will take the parsed list of TransactionHistory criteria and execute the
 * corresponding query
 * @author alexking
 */
@Component
public class TransactionHistoryQueryRunner implements Runnable
{
    @PersistenceContext
    private EntityManager entityManager;

    public TransactionHistoryQueryRunner(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public List<HistoryQuery> getHistoryQuerySet(){
        return null;
    }

    @Override
    public void run() {

    }
}
