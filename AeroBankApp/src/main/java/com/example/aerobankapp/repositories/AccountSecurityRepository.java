package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.AccountSecurityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AccountSecurityRepository extends JpaRepository<AccountSecurityEntity, Long>
{
    @Query("SELECT e.minimumBalance FROM AccountSecurityEntity e WHERE e.account.acctID=:acctID")
    BigDecimal getMinimumBalanceRequirementsByAcctID(@Param("acctID") int acctID);

    @Query("SELECT e.transactionVelocityLimit FROM AccountSecurityEntity e WHERE e.account.acctID=:acctID")
    int getTransactionVelocityLimitByAcctID(@Param("acctID") int acctID);

    @Query("SELECT e.withdrawLimit FROM AccountSecurityEntity e WHERE e.account.acctID=:acctID")
    int getWithdrawalLimitByAcctID(@Param("acctID") int acctID);

    @Query("SELECT e.depositLimit FROM AccountSecurityEntity e WHERE e.account.acctID=:acctID")
    int getDepositLimitByAcctID(@Param("acctID") int acctID);

    @Modifying
    @Query("UPDATE AccountSecurityEntity e SET e.isEnabled=:enabled, e.depositLimit=:deposit, e.withdrawLimit=:withdraw, e.transactionVelocityLimit=:velocity, e.isWithdrawEnabled=:withdrawEnabled, e.isDepositEnabled=:depositEnabled, e.isTransferEnabled=:isTransferEnabled, e.isPurchaseEnabled=:purchaseEnabled, e.isAccountLocked=:accountLocked WHERE e.accountSecurityID=:id")
    int updateAccountSecurityById(@Param("enabled") boolean isEnabled, @Param("deposit") int depositLimit, @Param("withdraw") int withdrawLimit, @Param("velocity") int velocityLimit, @Param("withdrawEnabled") boolean withdrawEnabled, @Param("depositEnabled") boolean depositEnabled, @Param("isTransferEnabled") boolean isTransferEnabled, @Param("accountLocked") boolean accountLocked, @Param("id") Long id);

    @Query("SELECT e.transactionVelocityLimit FROM AccountSecurityEntity e WHERE e.account.acctID=:acctID")
    int getAccountTransferVelocityLimit(@Param("acctID") int acctID);

    @Query("SELECT e.depositLimit FROM AccountSecurityEntity e WHERE e.account.acctID=:acctID")
    int getAccountDepositLimit(@Param("acctID") int acctID);

    @Query("SELECT e.withdrawLimit FROM AccountSecurityEntity e WHERE e.account.acctID=:acctID")
    int getAccountWithdrawLimit(@Param("acctID") int acctID);

    @Query("SELECT e.transferLimit FROM AccountSecurityEntity e WHERE e.account.acctID=:acctID")
    int getAccountTransferLimit(@Param("acctID") int acctID);

}

