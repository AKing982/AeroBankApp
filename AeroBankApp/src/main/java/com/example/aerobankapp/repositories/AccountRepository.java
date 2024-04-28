package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long>
{
  //  @Query("SELECT e.acctID FROM AccountEntity e JOIN e.users u WHERE u.accountNumber LIKE %:acctNum% AND e.ac LIKE %:acctCode%")
   // Integer findAccountIDByAcctCodeAndAccountNumber(@Param("acctNum") String accountNumber, @Param("acctCode") String acctCode);

    @Query("SELECT a FROM AccountEntity a WHERE a.acctID=:id")
    Optional<AccountEntity> findById(@Param("id") int acctID);

    @Query("SELECT CONCAT(ac.first_initial_segment, ac.accountType) AS shortSegment " +
            "FROM AccountEntity a " +
            "JOIN a.accountCode ac " +
            "JOIN ac.user u " +
            "WHERE u.username = :userName")
    List<String> getAccountCodeShortSegmentByUser(@Param("userName") String user);


    @Query("SELECT a FROM AccountEntity a JOIN a.users u WHERE u.username =:username")
    List<AccountEntity> findByUserName(@Param("username") String username);

    @Query("SELECT e.balance FROM AccountEntity e WHERE e.acctID =:acctID")
    BigDecimal getBalanceByAcctID(@Param("acctID") int acctID);

   // @Query("SELECT a FROM AccountEntity a JOIN a.users u WHERE a.accountCode=:acctCode AND u.accountNumber=:acctNum")
    @Query("SELECT a.acctID FROM AccountEntity a JOIN a.accountCode ac JOIN ac.user u WHERE u.accountNumber=:acctNum AND ac.acctCodeID=:code")
    int getAccountIDByAcctCodeAndAccountNumber(@Param("code") Long acctCodeID, @Param("acctNum") String acctNumber);

    @Query("SELECT e FROM AccountEntity e JOIN e.user u WHERE e.accountCode =:acctCode AND u.userID =:userID")
    List<AccountEntity> findByAccountCodeAndUserID(@Param("acctCode") String acctCode, @Param("userID") int userID);

    @Query("SELECT SUM(a.balance) FROM AccountEntity a JOIN a.users u WHERE u.username = :username")
    BigDecimal getTotalAccountBalances(@Param("username") String username);

    @Query("SELECT COUNT(a) FROM AccountEntity a JOIN a.users u WHERE u.username =:username")
    Long getNumberOfAccounts(@Param("username") String username);

    @Query("SELECT ac.acctCodeID FROM AccountCodeEntity ac JOIN ac.user u WHERE u.accountNumber=:acctNum")
    List<Integer> findAccountCodeIDListByAccountNumber(@Param("acctNum") String acctNumber);

   // @Query("SELECT a.accountCode FROM AccountEntity a JOIN a.users u WHERE u.username =:username")
    @Query("SELECT CONCAT(a.accountName, ' - XXXX', ac.accountType, ac.account_segment) AS segment FROM AccountEntity a JOIN a.accountCode ac JOIN ac.user u WHERE u.username=:username")
    List<String> findAccountNameWithAcctCodeByUserName(@Param("username") String username);

   // @Query("SELECT a.accountCode FROM AccountEntity a JOIN a.users u WHERE u.accountNumber=:acctNum")
    @Query("SELECT CONCAT(a.accountName, ' - XXXX', ac.accountType, ac.account_segment) AS segment FROM AccountEntity a JOIN a.accountCode ac JOIN ac.user u WHERE u.accountNumber=:acctNum")
    List<String> findAccountNamesWithAcctSegmentByAccountNumber(@Param("acctNum") String acctNum);

    @Query("SELECT e.balance FROM AccountEntity e JOIN e.user u WHERE e.accountCode =:acctCode AND u.userID =:userID")
    BigDecimal findBalanceByAccountCodeAndUserID(@Param("acctCode") String acctCode, @Param("userID") int userID);

    //@Query("SELECT a.acctID FROM AccountEntity a JOIN a.user u WHERE u.userID =:userID AND a.accountCode =:acctCode")
    @Query("SELECT a.acctID FROM AccountEntity a JOIN a.accountCode ac WHERE ac.user.userID=:userID AND ac.acctCodeID=:acctCode")
    Integer getAccountIDByAcctCodeAndUserID(@Param("userID") int userID, @Param("acctCode") Long acctCode);

    @Query("SELECT a FROM AccountEntity a JOIN a.user u WHERE u.userID=:userID")
    List<AccountEntity> findAccountsByUserID(@Param("userID") int userID);

    @Modifying
    @Query("UPDATE AccountEntity a SET a.balance =:balance WHERE a.acctID =:acctID")
    void updateAccountBalanceByAcctID(@Param("balance") BigDecimal balance, @Param("acctID") int acctID);

    @Query("SELECT ts.accountEntity.acctID, COUNT(ts) AS NumberOfTransactions " +
           "FROM TransactionStatementEntity ts " +
            "WHERE ts.accountEntity.user.userID=:userID " +
           "GROUP BY ts.accountEntity.acctID " +
           "ORDER BY NumberOfTransactions DESC")
    Page<Object[]> findAccountWithMostTransactionsByUserID(@Param("userID") int userID, Pageable pageable);

    @Query("SELECT COUNT(a) FROM AccountEntity a WHERE a.accountCode =:acctCode")
    int doesAccountCodeExist(@Param("acctCode") String acctCode);

    @Query("SELECT COUNT(a) FROM AccountEntity a WHERE a.acctID =:acctID")
    int doesAccountIDExist(@Param("acctID") int acctID);

    @Query("SELECT a.acctID FROM AccountEntity a WHERE a.accountName=:name")
    int findAccountIDByAccountName(@Param("name") String accountName);
    @Query("SELECT a.acctID FROM AccountEntity a JOIN a.user u WHERE u.userID=:userID")
    List<Integer> getListOfAccountIDsByUserID(@Param("userID") int userID);

    @Query("SELECT MAX(a.acctID) AS latestAccountID FROM AccountEntity a")
    Integer fetchLatestAccountID();
}
