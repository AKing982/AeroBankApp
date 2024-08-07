package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.TransactionStatementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionStatementRepository extends JpaRepository<TransactionStatementEntity, Long>
{
    @Query("SELECT e FROM TransactionStatementEntity e WHERE e.accountEntity.acctID=:acctID AND e.isPending=FALSE")
    List<TransactionStatementEntity> getTransactionStatementsByAcctID(@Param("acctID") int acctID);

    @Query("SELECT e FROM TransactionStatementEntity e WHERE e.accountEntity.acctID=:acctID AND e.isPending=TRUE")
    List<TransactionStatementEntity> getPendingTransactionStatementsByAcctID(@Param("acctID") int acctID);

    @Query("SELECT COUNT(*) FROM TransactionStatementEntity e WHERE e.accountEntity.acctID=:acctID AND e.isPending=TRUE")
    int getCountOfPendingTransactionsForAcctID(@Param("acctID") int acctID);
}
