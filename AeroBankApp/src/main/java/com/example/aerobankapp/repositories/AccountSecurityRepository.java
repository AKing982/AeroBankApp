package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.AccountSecurityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
