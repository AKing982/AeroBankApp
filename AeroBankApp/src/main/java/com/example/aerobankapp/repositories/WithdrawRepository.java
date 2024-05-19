package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.WithdrawEntity;
import com.example.aerobankapp.workbench.utilities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WithdrawRepository extends JpaRepository<WithdrawEntity, Long>
{
    // Find withdraws by user ID
    @Query("SELECT w FROM WithdrawEntity w WHERE w.user.userID=:userID")
    List<WithdrawEntity> findByUserId(@Param("userID") int userID);

    // Find withdraws for a specific account
    @Query("SELECT w FROM WithdrawEntity w WHERE w.account.acctID=:acctID")
    List<WithdrawEntity> findByAccountId(@Param("acctID") int acctID);

    // Find withdraws by status
    @Query("SELECT w FROM WithdrawEntity w JOIN w.transactionCriteria c WHERE c.transactionStatus=:status")
    List<WithdrawEntity> findByStatus(@Param("status") Status status);

    @Query("SELECT w FROM WithdrawEntity w JOIN w.user u WHERE u.username=:user")
    List<WithdrawEntity> findWithdrawalsByUserName(@Param("user") String user);

    @Query("SELECT c.amount FROM WithdrawEntity w JOIN w.transactionCriteria c WHERE w.withdrawID=:id")
    BigDecimal findAmountById(@Param("id") Long id);

    // Find withdraws within a date range
    @Query("SELECT w FROM WithdrawEntity w JOIN w.transactionCriteria c WHERE c.posted BETWEEN :startDate AND :endDate")
    List<WithdrawEntity> findWithdrawsBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Find withdraws for a specific account and a specific status
    @Query("SELECT w FROM WithdrawEntity w JOIN w.transactionCriteria c WHERE w.account.acctID = :acctID AND c.transactionStatus = :status")
    List<WithdrawEntity> findByAccountIdAndStatus(@Param("acctID") int acctID, @Param("status") Status status);

    @Query("SELECT w FROM WithdrawEntity w WHERE w.user.userID=:userID ORDER BY w.withdrawID DESC")
    List<WithdrawEntity> findByUserIDDescending(@Param("userID") int userID);

    @Query("SELECT w FROM WithdrawEntity w WHERE w.user.userID=:userID ORDER BY w.withdrawID ASC")
    List<WithdrawEntity> findByUserIDAscending(@Param("userID") int userID);

    @Query("SELECT w FROM WithdrawEntity w JOIN w.transactionCriteria c WHERE c.description=:description")
    Optional<WithdrawEntity> findByDescription(@Param("description") String description);
    // Update the processed status of withdraws by user ID

}
