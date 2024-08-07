package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.PendingTransactionEntity;
import com.example.aerobankapp.workbench.utilities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;

@Repository
public interface PendingTransactionRepository extends JpaRepository<PendingTransactionEntity, Long>
{
    @Query("SELECT e FROM PendingTransactionEntity e WHERE e.account.acctID=:acctID")
    Map<Integer, BigDecimal> getPendingAmountsByAcctID(@Param("acctID") int acctID);

    @Query("SELECT e FROM PendingTransactionEntity e WHERE e.pendingID=:id")
    Map<Integer, BigDecimal> getPendingAmountByID(@Param("id") Long id);

    @Query("SELECT COUNT(e) FROM PendingTransactionEntity e WHERE e.account.acctID=:acctID")
    int totalCountOfPendingTransactionsByAcctID(@Param("acctID") int acctID);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE PendingTransactionEntity e SET e.status=:status WHERE e.pendingID=:id")
    int updatePendingTransactionStatus(@Param("status")Status status, @Param("id") Long id);
}
