package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.PlaidTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlaidTransactionRepository extends JpaRepository<PlaidTransactionEntity, Long>
{
    @Query("SELECT e FROM PlaidTransactionEntity e WHERE e.id =:id")
    List<PlaidTransactionEntity> findAllById(@Param("id") Long plaidId);

    @Query("SELECT e FROM PlaidTransactionEntity e WHERE e.user.userID =:id")
    List<PlaidTransactionEntity> findByUserId(@Param("id") int userId);

    @Query("SELECT e FROM PlaidTransactionEntity e WHERE e.externalAcctID =:id")
    Optional<PlaidTransactionEntity> findByExternalAcctID(@Param("id") String externalAcctID);

    @Query("SELECT e FROM PlaidTransactionEntity e WHERE e.pending=true AND e.user.userID =:id")
    List<PlaidTransactionEntity> findPendingTransactionsByUserId(@Param("id") int userId);

    @Query("SELECT e FROM PlaidTransactionEntity e WHERE e.merchantName LIKE :name AND e.user.userID =:id")
    List<PlaidTransactionEntity> findByMerchantNameContainingAndUserId(@Param("name") String merchantName, @Param("id") int userID);

    @Query("SELECT e FROM PlaidTransactionEntity e WHERE e.date BETWEEN :startDate AND :endDate AND e.user.userID =:id")
    List<PlaidTransactionEntity> findByDateBetweenAndUserId(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("id") int userID);

    @Query("SELECT e FROM PlaidTransactionEntity e WHERE e.amount BETWEEN :startDate AND :endDate AND e.user.userID =:id")
    List<PlaidTransactionEntity> findByAmountBetweenAndUserId(@Param("startDate") BigDecimal start, @Param("endDate") BigDecimal end, @Param("id") int userID);
}
