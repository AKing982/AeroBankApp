package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long>
{
    @Query("SELECT a FROM AccountEntity a JOIN a.users u WHERE u.username =:username")
    List<AccountEntity> findByUserName(@Param("username") String username);

    @Query("SELECT e.balance FROM AccountEntity e WHERE e.acctID =:acctID")
    BigDecimal getBalanceByAcctID(@Param("acctID") int acctID);

    @Query("SELECT e FROM AccountEntity e WHERE e.accountCode =:acctCode AND e.userID =:userID")
    List<AccountEntity> findByAccountCodeAndUserID(@Param("acctCode") String acctCode, @Param("userID") int userID);

    @Query("SELECT SUM(a.balance) FROM AccountEntity a JOIN a.users u WHERE u.username = :username")
    BigDecimal getTotalAccountBalances(@Param("username") String username);

    @Query("SELECT COUNT(a) FROM AccountEntity a JOIN a.users u WHERE u.username =:username")
    Long getNumberOfAccounts(@Param("username") String username);

    @Query("SELECT a.accountCode FROM AccountEntity a JOIN a.users u WHERE u.username =:username")
    List<String> findAccountCodesByUserName(@Param("username") String username);

    @Query("SELECT e.balance FROM AccountEntity e WHERE e.accountCode =:acctCode AND e.userID =:userID")
    BigDecimal findBalanceByAccountCodeAndUserID(@Param("acctCode") String acctCode, @Param("userID") int userID);

    @Query("SELECT a.acctID FROM AccountEntity a WHERE a.userID =:userID AND a.accountCode =:acctCode")
    Integer getAccountIDByAcctCodeAndUserID(@Param("userID") int userID, @Param("acctCode") String acctCode);

    @Query("UPDATE AccountEntity a SET a.balance =:balance WHERE a.acctID =:acctID")
    void updateAccountBalanceByAcctID(@Param("balance") BigDecimal balance, @Param("acctID") int acctID);

    @Query("SELECT COUNT(a) FROM AccountEntity a WHERE a.accountCode =:acctCode")
    boolean doesAccountCodeExist(@Param("acctCode") String acctCode);

    @Query("SELECT COUNT(a) FROM AccountEntity a WHERE a.acctID =:acctID")
    boolean doesAccountIDExist(@Param("acctID") int acctID);
}
