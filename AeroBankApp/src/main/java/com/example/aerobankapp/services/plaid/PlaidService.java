package com.example.aerobankapp.services.plaid;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.PlaidLinkEntity;
import com.example.aerobankapp.entity.PlaidTransactionEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.model.PlaidAccountBalances;
import com.example.aerobankapp.model.PlaidTransactionCriteria;
import com.example.aerobankapp.repositories.PlaidLinkRepository;
import com.example.aerobankapp.services.PlaidTransactionService;
import com.example.aerobankapp.workbench.plaid.PlaidAccountManager;
import com.example.aerobankapp.workbench.plaid.PlaidFilterCriteriaService;
import com.example.aerobankapp.workbench.plaid.PlaidTokenProcessorImpl;
import com.example.aerobankapp.workbench.plaid.PlaidTransactionManagerImpl;
import com.plaid.client.model.*;
import com.plaid.client.request.PlaidApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PlaidService
{
    private final PlaidTokenProcessorImpl plaidTokenProcessor;
    private final PlaidTransactionManagerImpl plaidTransactionManager;
    private final PlaidAccountManager plaidAccountManager;
    private final PlaidLinkRepository plaidLinkRepository;
    private final PlaidTransactionService plaidTransactionService;
    private final PlaidFilterCriteriaService plaidFilterCriteriaService;
    private Logger LOGGER = LoggerFactory.getLogger(PlaidService.class);

    @Autowired
    public PlaidService(PlaidTokenProcessorImpl plaidTokenProcessor,
                        PlaidTransactionManagerImpl plaidTransactionManager,
                        PlaidLinkRepository plaidLinkRepository,
                        PlaidTransactionService plaidTransactionService,
                        PlaidAccountManager plaidAccountManager,
                        PlaidFilterCriteriaService plaidFilterCriteriaService)
    {
        this.plaidTokenProcessor = plaidTokenProcessor;
        this.plaidTransactionManager = plaidTransactionManager;
        this.plaidLinkRepository = plaidLinkRepository;
        this.plaidTransactionService = plaidTransactionService;
        this.plaidAccountManager = plaidAccountManager;
        this.plaidFilterCriteriaService = plaidFilterCriteriaService;
    }

    public PlaidLinkEntity buildPlaidAccountsEntity(String accessToken, String item_id, int userID, String institutionName)
    {
        return PlaidLinkEntity.builder()
                .accessToken(accessToken)
                .item_id(item_id)
                .user(UserEntity.builder().userID(userID).build())
                .institution_name(institutionName)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void createAndSavePlaidAccountEntity(String item_id, int userID, String access_token)
    {
        PlaidLinkEntity plaidAccountsEntity = buildPlaidAccountsEntity(access_token, item_id, userID, "sandBox");

        plaidLinkRepository.save(plaidAccountsEntity);
    }

    public Optional<PlaidLinkEntity> getPlaidLinkEntityByUserId(int userID)
    {
        return plaidLinkRepository.findByUserId(userID);
    }

    /**
     * Creates a link token for a given client user ID.
     *
     * @param clientUserId The ID of the client user.
     * @return A LinkTokenCreateResponse object representing the link token.
     * @throws Exception if an error occurs while creating the link token.
     */
    public LinkTokenCreateResponse createLinkToken(String clientUserId) throws Exception
    {
        if(clientUserId == null || clientUserId.isEmpty())
        {
            throw new IllegalArgumentException("Client user ID cannot be null or empty");
        }

        LOGGER.info("ClientUserId: {}", clientUserId);
        return plaidTokenProcessor.createLinkToken(clientUserId);
    }

    /**
     * Exchanges a public token for an access token and item ID.
     *
     * @param publicToken The public token to exchange.
     * @return An ItemPublicTokenExchangeResponse object containing the access token and item ID.
     * @throws Exception if an error occurs while exchanging the public token.
     */
    public ItemPublicTokenExchangeResponse exchangePublicToken(String publicToken) throws Exception
    {
        return plaidTokenProcessor.exchangePublicToken(publicToken);
    }

    /**
     * Retrieves the account balances for a given access token.
     *
     * @param userID The access token used to authenticate the request.
     * @return An AccountsGetResponse object representing the account balances.
     * @throws Exception if an error occurs while retrieving the account balances.
     */
    public List<PlaidAccountBalances> getAccountBalances(int userID) throws Exception
    {
        return plaidAccountManager.getBalancesByUserId(userID);
    }

    /**
     * Retrieves the transactions for a given access token within a specified date range.
     *
     * @param userID The UserID.
     * @param startDate The start date of the transaction range.
     * @param endDate The end date of the transaction range.
     * @return A TransactionsGetResponse object representing the transactions.
     * @throws Exception if an error occurs while retrieving the transactions.
     */
    public TransactionsGetResponse getTransactions(int userID, LocalDate startDate, LocalDate endDate) throws Exception
    {
        return plaidTransactionManager.getTransactionResponse(userID, startDate, endDate);
    }

    /**
     * Synchronizes transactions for a given access token with a specified cursor.
     *
     * @param userID The UserID.
     * @param cursor The cursor indicating the position to start syncing transactions from.
     * @return A TransactionsSyncResponse object representing the synchronized transactions.
     * @throws Exception if an error occurs while synchronizing the transactions.
     */
    public TransactionsSyncResponse syncTransactions(int userID, String cursor) throws Exception
    {
        return plaidTransactionManager.getTransactionSyncResponse(userID, cursor);
    }

    /**
     * Retrieves the account balances for a given access token.
     *
     * @param userID The access token used to authenticate the request.
     * @return An AccountsGetResponse object representing the account balances.
     * @throws Exception if an error occurs while retrieving the account balances.
     */
    public AccountsGetResponse getAccounts(int userID) throws Exception
    {
        return plaidAccountManager.getAllAccounts(userID);
    }

    public Page<PlaidTransactionCriteria> getPaginatedFilteredTransactionsFromResponse(int userID, LocalDate startDate, LocalDate endDate, Pageable pageable) throws Exception
    {
        TransactionsGetResponse transactionsGetResponse = getTransactions(userID, startDate, endDate);
        return plaidFilterCriteriaService.getPlaidTransactionCriteriaFromResponse(pageable, transactionsGetResponse);
    }

    // TODO: Test method
    public void createAndSavePlaidTransactionEntity(UserEntity user, PlaidTransactionCriteria plaidTransactionCriteria)
    {
        PlaidTransactionEntity plaidTransactionEntity =  plaidTransactionService.createPlaidTransactionEntity(user, plaidTransactionCriteria);
        plaidTransactionService.save(plaidTransactionEntity);
    }
}
