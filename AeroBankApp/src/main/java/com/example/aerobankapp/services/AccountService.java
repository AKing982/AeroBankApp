package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.AccountCodeDTO;
import com.example.aerobankapp.dto.AccountInfoDTO;
import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountSecurityEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.AccountIDNotFoundException;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.model.AccountCode;
import com.example.aerobankapp.model.ServiceDAOModel;
import com.example.aerobankapp.model.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AccountService extends ServiceDAOModel<AccountEntity>
{
    @Override
    List<AccountEntity> findAll();

    @Override
    void save(AccountEntity obj);

    @Override
    void delete(AccountEntity obj);

    @Override
    Optional<AccountEntity> findAllById(Long id);

    Optional<AccountEntity> findById(int id);

    @Override
    List<AccountEntity> findByUserName(String user);

    BigDecimal getBalanceByAcctID(int acctID);

    BigDecimal getBalanceByAccountCodeUserID(String acctCode, int userID);

    BigDecimal getTotalAccountBalances(String user);

    Long getNumberOfAccounts(String user);

    int getAccountIDByAccountCodeAndAccountNumber(String acctCode, String accountNumber);

    List<String> getListOfAccountCodes(String user);

    int getAccountIDByAcctCodeAndAccountNumber(String acctCode, String accountNumber);

    List<String> getAccountCodeListByAccountNumber(String accountNumber);

    int getAccountIDByAcctCodeAndUserID(int userID, String acctCode);

    Integer getAccountWithMostTransactionsByUserID(int userID);

    List<AccountEntity> getListOfAccountsByUserID(int userID);

    Map<Integer, String> getAccountTypeMapByAccountId(String userName);

    void updateAccountBalanceByAcctID(BigDecimal balance, int accountID);

    int getRandomAccountIDByUserID(int userID);

    int getAccountIDByAccountName(String accountName);

    int getLatestAccountID();

    void saveAll(List<AccountEntity> accountEntities);

    List<String> getAccountCodeShortSegmentByUser(String user);

    AccountEntity buildAccountEntity(AccountInfoDTO accountInfoDTO, AccountCodeEntity accountCode,UserEntity user);

    AccountEntity buildAccountEntityByAccountModel(Account account, AccountCodeEntity accountCode, UserEntity user);
}
