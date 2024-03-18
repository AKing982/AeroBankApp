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
    @Query("SELECT e FROM TransactionStatementEntity e WHERE e.accountEntity.acctID=:acctID")
    List<TransactionStatementEntity> getTransactionStatementsByAcctID(@Param("acctID") int acctID);
}
