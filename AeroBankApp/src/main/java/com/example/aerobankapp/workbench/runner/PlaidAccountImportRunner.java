package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.AeroBankAppApplication;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.exceptions.LinkedAccountInfoListNullException;
import com.example.aerobankapp.exceptions.NonEmptyListRequiredException;
import com.example.aerobankapp.exceptions.PlaidAccountsGetResponseNullPointerException;
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
import com.plaid.client.model.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PlaidAccountImportRunner
{
    private PlaidAccountManager plaidAccountManager;
    private PlaidAccountImporter plaidAccountImporter;
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private Logger LOGGER = LoggerFactory.getLogger(PlaidAccountImportRunner.class);

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

    public List<PlaidAccount> getUserPlaidAccounts(int userId) throws IOException, InterruptedException
    {

        try
        {
            AccountsGetResponse accountsGetResponse = plaidAccountManager.getAllAccounts(userId);
            List<AccountBase> accountBaseList = accountsGetResponse.getAccounts();

            Set<PlaidAccount> plaidAccounts = plaidAccountManager.getPlaidAccountsSetFromResponse(accountBaseList);
            return plaidAccounts.stream().toList();
            
        }catch(PlaidAccountsGetResponseNullPointerException paex)
        {
            throw new PlaidAccountsGetResponseNullPointerException("Plaid Accounts Response null from plaid server.");

        }catch(NonEmptyListRequiredException nel)
        {
            throw new NonEmptyListRequiredException("Plaid Account List is empty");

        }catch(InvalidUserIDException iue)
        {
            throw new InvalidUserIDException("Invalid UserID caught: " + userId);
        }
    }

    public List<LinkedAccountInfo> getPreparedLinkAccountList(UserEntity userEntity, List<PlaidAccount> plaidAccounts) throws IOException, InterruptedException {
        return plaidAccountImporter.prepareLinkedAccounts(userEntity, plaidAccounts);
    }

    public UserEntity getUserEntityFromRepository(int userId)
    {
        return userRepository.findById(userId)
                .orElseThrow();
    }

    public void importPlaidAccounts(int userId) throws IOException, InterruptedException {
        List<PlaidAccount> plaidAccounts = getUserPlaidAccounts(userId);
        UserEntity user = getUserEntityFromRepository(userId);
        List<LinkedAccountInfo> linkedAccounts = getPreparedLinkAccountList(user, plaidAccounts);
        if(linkedAccounts == null)
        {
            throw new LinkedAccountInfoListNullException("Linked Accounts list found null");
        }

        if(linkedAccounts.isEmpty())
        {
            throw new NonEmptyListRequiredException("Linked Accounts list found empty.");
        }


        if(createAndSaveExternalAccount(linkedAccounts))
        {
            List<Account> accounts = getAccountEntityListAsAccountsList(userId);
            for(Account account : accounts)
            {
                PlaidAccount plaidAccount = plaidAccountImporter.
            }
        }
           Map<Integer, List<PlaidAccount>> integerListMap = getPlaidAccountsMap();
           plaidAccountImporter.createImportedAccountsFromNonLinkAccountsList(integerListMap);
    }

    public Map<Integer, List<PlaidAccount>> getPlaidAccountsMap(){
        return plaidAccountImporter.getPlaidAccountsMap();
    }

    public Boolean createAndSaveExternalAccount(List<LinkedAccountInfo> listOfLinkedAccounts) throws IOException, InterruptedException {
        return plaidAccountImporter.executeCreateAndSaveExternalAccountEntity(listOfLinkedAccounts);
    }

    private List<AccountEntity> getUserAccountsList(int userId){
        return accountRepository.findAccountsByUserID(userId);
    }

    public List<Account> getAccountEntityListAsAccountsList(final int userID)
    {
        List<AccountEntity> userAccounts = getUserAccountsList(userID);
        // Convert the account entities to Account model
        return userAccounts.stream()
                .map(accountEntity -> {
                    Account account = new Account();
                    account.setAccountID(accountEntity.getAcctID());
                    account.setAccountName(accountEntity.getAccountName());
                    account.setAccountType(AccountType.getInstance(accountEntity.getAccountType()));
                    account.setBalance(accountEntity.getBalance());
                    account.setInterest(accountEntity.getInterest());
                    account.setUserID(accountEntity.getUser().getUserID());
                    account.setMask(accountEntity.getMask());
                    account.setPropsID(accountEntity.getAccountProperties().getAccountPropsID());
                    account.setASecID(accountEntity.getAccountSecurity().getAccountSecurityID());
                    account.setType(accountEntity.getType());
                    account.setSubType(accountEntity.getSubtype());
                    return account;
                })
                .toList();
    }

public static void main(String[] args) throws IOException, InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(AeroBankAppApplication.class, args);

        PlaidAccountImportRunner plaidAccountImportRunner = context.getBean(PlaidAccountImportRunner.class);
        plaidAccountImportRunner.importPlaidAccounts(1);
    }



}
