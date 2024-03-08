package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.TransactionDetailEntity;
import com.example.aerobankapp.model.ServiceDAOModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TransactionDetailEntityService extends ServiceDAOModel<TransactionDetailEntity>
{
    List<TransactionDetailEntity> findTransactionDetailsByTransactionID(Long transactionID);
    List<TransactionDetailEntity> findTransactionDetailByUserID(int userID);
    BigDecimal findNewBalanceByID(Long id);
    BigDecimal findOriginalBalanceByID(Long id);
    Map<Integer, BigDecimal> findNewBalanceMap(Long id);
    List<TransactionDetailEntity> findTransactionDetailByAcctCode(String acctCode);
    String findTransactionDescriptionById(Long id);

}
