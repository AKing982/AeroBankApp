package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.Account;
import com.example.aerobankapp.model.LinkedAccountInfo;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.model.PlaidImportResult;
import com.example.aerobankapp.repositories.AccountRepository;
import com.example.aerobankapp.repositories.UserRepository;
import com.example.aerobankapp.workbench.plaid.PlaidAccountImporter;
import com.example.aerobankapp.workbench.plaid.PlaidAccountManager;
import com.plaid.client.model.AccountBase;

import com.example.aerobankapp.account.AccountType;
import com.plaid.client.model.AccountsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PlaidAccountImportRunner
{
    private PlaidAccountManager plaidAccountManager;
    private PlaidAccountImporter plaidAccountImporter;
    private UserRepository userRepository;
    private AccountRepository accountRepository;

    @Autowired
    public PlaidAccountImportRunner(PlaidAccountManager plaidAccountManager, PlaidAccountImporter plaidAccountImporter,
                                    UserRepository userRepository,
                                    AccountRepository accountRepository)
    {
        this.plaidAccountManager = plaidAccountManager;
        this.plaidAccountImporter = plaidAccountImporter;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public List<PlaidAccount> getUserPlaidAccounts(int userId) throws IOException, InterruptedException {
        AccountsGetResponse accountsGetResponse = plaidAccountManager.getAllAccounts(userId);
        List<AccountBase> accountBaseList = accountsGetResponse.getAccounts();
        Set<PlaidAccount> plaidAccounts = plaidAccountManager.getPlaidAccountsSetFromResponse(accountBaseList);
        return plaidAccounts.stream().toList();
    }

    public void importPlaidAccounts(int userId) throws IOException, InterruptedException {
        List<PlaidAccount> plaidAccounts = getUserPlaidAccounts(userId);
        UserEntity user = userRepository.findById(userId).orElseThrow();
        List<LinkedAccountInfo> linkedAccounts = plaidAccountImporter.prepareLinkedAccounts(user, plaidAccounts);

        // Create and save the external accountID
        Boolean externalAcctIsCreated = plaidAccountImporter.executeCreateAndSaveExternalAccountEntity(linkedAccounts);
        if(externalAcctIsCreated)
        {
            List<AccountEntity> userAccounts = accountRepository.findAccountsByUserID(userId);
            // Convert the account entities to Account model
            List<Account> accounts = userAccounts.stream()
                    .map(accountEntity -> {
                        Account account = new Account();
                        account.setAccountID(accountEntity.getAcctID());
                        account.setAccountName(accountEntity.getAccountName());
                        account.setAccountType(AccountType.getInstance(accountEntity.getAccountType()));
                        account.setBalance(accountEntity.getBalance());
                        account.setInterest(accountEntity.getInterest());
                        account.setUserID(accountEntity.getUser().getUserID());
                        account.setMask(accountEntity.getMask());
                        account.setType(accountEntity.getType());
                        account.setSubType(accountEntity.getSubtype());
                        return account;
                    })
                    .collect(Collectors.toList());

            for(Account account : accounts)
            {
                for(PlaidAccount plaidAccount : plaidAccounts)
                {
                    PlaidImportResult plaidImportResult = plaidAccountImporter.importDataFromPlaidAccountToSystemAccount(plaidAccount, account);
                }
            }

           Map<Integer, List<PlaidAccount>> integerListMap = plaidAccountImporter.getPlaidAccountsMap();
           plaidAccountImporter.createImportedAccountsFromNonLinkAccountsList(integerListMap);
        }
    }



}
