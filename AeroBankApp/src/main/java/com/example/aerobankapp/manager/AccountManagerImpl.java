package com.example.aerobankapp.manager;

import com.example.aerobankapp.account.AbstractAccountBase;
import com.example.aerobankapp.account.AccountCodeDTO;
import com.example.aerobankapp.dto.AccountDTO;
import com.example.aerobankapp.dto.BalanceHistoryDTO;
import com.example.aerobankapp.dto.UserDTO;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.services.AccountServiceImpl;
import com.example.aerobankapp.workbench.utilities.conversion.AccountMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountManagerImpl implements AccountManager
{
    private AccountServiceImpl accountDAO;

    @Autowired
    public AccountManagerImpl(AccountServiceImpl accountDAO)
    {
        Objects.requireNonNull(accountDAO, "AccountDAO cannot be null");
        this.accountDAO = accountDAO;
    }


    @Override
    public Set<AccountDTO> getAccounts(int userID) {
        Optional<AccountEntity> accountEntities = accountDAO.findAllById((long)userID);

        return accountEntities.stream()
                .map(AccountMapperUtil::convertToDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<UserDTO> getUsersLinkedToAccounts(List<Integer> userIDs)
    {
        return null;
    }

    @Override
    public void deleteAccount(AccountEntity account)
    {
        accountDAO.delete(account);
    }

    @Override
    public void updateAccount(AccountEntity account)
    {
        accountDAO.save(account);
    }

    @Override
    public BigDecimal getBalanceFromAccount(String acctID)
    {
        return accountDAO.getBalanceByAcctID(acctID);
    }

    @Override
    public BigDecimal getBalanceByAccountCode(AccountCodeDTO code) {
        String accountCode = code.getAccountCode();
        return accountDAO.getBalanceByAccountCode(accountCode);
    }

}
