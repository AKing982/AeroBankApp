package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.TransactionStatementEntity;
import com.example.aerobankapp.exceptions.NonEmptyListRequiredException;
import com.example.aerobankapp.repositories.TransactionStatementRepository;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
public class TransactionStatementServiceImpl implements TransactionStatementService
{
    private TransactionStatementRepository transactionStatementRepository;
    private Logger LOGGER = LoggerFactory.getLogger(TransactionStatementServiceImpl.class);

    @Autowired
    public TransactionStatementServiceImpl(TransactionStatementRepository transactionStatementRepository){
        this.transactionStatementRepository = transactionStatementRepository;
    }


    @Override
    public List<TransactionStatementEntity> findAll() {
        try
        {
            List<TransactionStatementEntity> transactionStatementEntities = transactionStatementRepository.findAll();
            if(transactionStatementEntities.isEmpty()){
                throw new NonEmptyListRequiredException("Unable to retrieve Transaction Statements");
            }
            return transactionStatementRepository.findAll();

        }catch(Exception e)
        {
            LOGGER.error("There was an error retrieving Transaction Statements: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void save(TransactionStatementEntity obj) {
        if(obj == null){
            throw new NullPointerException("Saving Null Transaction Statement: " + obj);
        }
        getTransactionStatementRepository().save(obj);
    }

    @Override
    public void delete(TransactionStatementEntity obj) {
        if(obj == null){
            throw new NullPointerException("Deleting a Null Transaction");
        }
        getTransactionStatementRepository().delete(obj);
    }


    @Override
    public Optional<TransactionStatementEntity> findAllById(Long id) {
        return Optional.empty();
    }

    // METHOD NOT REQUIRED FOR IMPLEMENTATION
    @Override
    public List<TransactionStatementEntity> findByUserName(String user) {
        return null;
    }

    @Override
    public List<TransactionStatementEntity> getTransactionStatementsByAcctID(int acctID) {
        List<TransactionStatementEntity> transactionStatementEntityList = transactionStatementRepository.getTransactionStatementsByAcctID(acctID);
        return transactionStatementEntityList;
    }

    @Override
    public List<TransactionStatementEntity> getPendingTransactionsByAcctID(int acctID) {
        List<TransactionStatementEntity> pendingTransactions = transactionStatementRepository.getPendingTransactionStatementsByAcctID(acctID);
        return pendingTransactions;
    }

    @Override
    public int getCountOfPendingTransactionsByAcctID(int acctID) {
        int count = transactionStatementRepository.getCountOfPendingTransactionsForAcctID(acctID);
        if(count > 0){
            return count;
        }
        return 0;
    }
}
