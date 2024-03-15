package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.model.BalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BalanceHistoryRepository extends JpaRepository<BalanceHistoryEntity, Long>
{
    @Query("SELECT e FROM BalanceHistoryEntity e WHERE e.account.acctID=:acctID")
    List<BalanceHistoryEntity> getBalanceHistoryByAcctID(@Param("acctID") int acctID);

    @Query("SELECT e FROM BalanceHistoryEntity e WHERE e.account.acctID=:acctID AND e.posted=:posted")
    List<BalanceHistoryEntity> getBalanceHistoryBatchByDateAcctID(@Param("acctID") int acctID, @Param("posted")LocalDate posted);

    @Query("SELECT e FROM BalanceHistoryEntity e WHERE e.account.acctID=:acctID AND e.posted=:posted")
    Optional<BalanceHistoryEntity> getBalanceHistoryByDateAcctID(@Param("acctID") int acctID, @Param("posted") LocalDate posted);

    @Query("SELECT e FROM BalanceHistoryEntity e WHERE e.account.acctID=:acctID AND e.posted BETWEEN :beginDate AND :endDate")
    List<BalanceHistoryEntity> getBalancesHistoriesByDateRangeAndAcctID(@Param("acctID") int acctID, @Param("beginDate") LocalDate beginDate, @Param("endDate") LocalDate endDate);



}
