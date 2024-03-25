package com.example.aerobankapp.engine;

import com.example.aerobankapp.converter.EntityToModelConverter;
import com.example.aerobankapp.converter.TransferConverter;
import com.example.aerobankapp.dto.TransferDTO;
import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.entity.TransferEntity;
import com.example.aerobankapp.exceptions.NonEmptyListRequiredException;
import com.example.aerobankapp.exceptions.NullTransferObjectException;
import com.example.aerobankapp.model.TransactionBalanceSummary;
import com.example.aerobankapp.model.TransferBalanceSummary;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.transactions.TransactionSummary;
import com.example.aerobankapp.workbench.transactions.Transfer;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TransferEngine extends TransactionEngine<Transfer, TransferBalanceSummary> implements Runnable
{

    private final TransferService transferService;
    private final EntityToModelConverter<TransferEntity, Transfer> transferConverter;
    private final Logger LOGGER = LoggerFactory.getLogger(TransferEngine.class);

    @Autowired
    public TransferEngine(TransferService transferService, AccountService accountService, AccountSecurityService accountSecurityService, NotificationService notificationService, CalculationEngine calculationEngine, BalanceHistoryService balanceHistoryService, EncryptionService encryptionService) {
        super(accountService, accountSecurityService, notificationService, calculationEngine, balanceHistoryService, encryptionService);
        this.transferService = transferService;
        this.transferConverter = new TransferConverter();
    }

    @Override
    protected List<Transfer> fetchAll() {
        List<TransferEntity> transferEntities = transferService.findAll();
        if(transferEntities.isEmpty()){
            throw new NonEmptyListRequiredException("Found No Transfer Entities.");
        }
        return transferEntities.stream()
                .map(transferConverter::convert)
                .toList();
    }

    @Override
    protected List<Transfer> retrieveTransactionsByUserID(int userID) {
        return null;
    }

    @Override
    protected void sendNotification(List<TransferBalanceSummary> balanceSummaries) {

    }


    @Override
    protected Map<Integer, BigDecimal> getCalculatedAccountBalanceMap(List<Transfer> transactionList) {
        Map<Integer, BigDecimal> accountBalanceMap = new HashMap<>();

        Set<Integer> originAccountIDs = retrieveTransferAccountIDSet(transactionList, Transfer::getFromAccountID);

        Set<Integer> targetAccountIDs = retrieveTransferAccountIDSet(transactionList, Transfer::getToAccountID);

        Map<Integer, BigDecimal> transactionAmountByAcctID = retrieveTransactionAmountByAcctID(transactionList);

        return null;
    }


    @Override
    protected Set<Integer> retrieveAccountIDSet(List<Transfer> transactionList) {
        // NOT IMPLEMENTED
        return null;
    }

    protected Set<Integer> retrieveTransferAccountIDSet(List<Transfer> transfers, Function<Transfer, Integer> accountIDExtractor){
        if(transfers.isEmpty()){
            throw new NonEmptyListRequiredException("Unable to retrieve AccountID's with empty transfer list.");
        }
        Set<Integer> accountIDs = transfers.stream()
                .map(accountIDExtractor)
                .collect(Collectors.toUnmodifiableSet());

        if(accountIDs == null){
            LOGGER.info("AccountID Set is Null");
        }
        return accountIDs;
    }

    @Override
    protected Map<Integer, BigDecimal> retrieveTransactionAmountByAcctID(List<Transfer> transactionList) {
        return null;
    }

    @Override
    protected Map<Integer, List<TransferBalanceSummary>> generateBalanceSummaryMap(List<Transfer> transactions, Map<Integer, BigDecimal> accountBalances) {
        return null;
    }

    @Override
    protected BalanceHistoryEntity createBalanceHistoryEntity(TransferBalanceSummary balanceSummary, BigDecimal currentBalance, BigDecimal adjustedAmount) {
        return null;
    }

    @Override
    protected List<BalanceHistoryEntity> convertBalanceSummaryToBalanceHistoryEntityList(List<TransferBalanceSummary> transactionSummaries) {
        return null;
    }

    protected List<Transfer> getFilteredUserToUserTransfers(List<Transfer> unfilteredTransfers){
        return null;
    }

    protected boolean isSameUserTransfer(final Transfer transfer){
        return transfer.isUserToUserTransfer();
    }

    protected void processTransfers(List<Transfer> unfilteredTransfers){

    }

    @Override
    public void run() {

    }
}
