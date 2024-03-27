package com.example.aerobankapp.engine;

import com.example.aerobankapp.converter.EntityToModelConverter;
import com.example.aerobankapp.converter.TransferConverter;
import com.example.aerobankapp.dto.TransferDTO;
import com.example.aerobankapp.entity.BalanceHistoryEntity;
import com.example.aerobankapp.entity.TransferEntity;
import com.example.aerobankapp.exceptions.NonEmptyListRequiredException;
import com.example.aerobankapp.exceptions.NullTransferObjectException;
import com.example.aerobankapp.model.BalanceHistory;
import com.example.aerobankapp.model.TransactionBalanceSummary;
import com.example.aerobankapp.model.TransferBalanceSummary;
import com.example.aerobankapp.model.TransferBalances;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.transactions.TransactionSummary;
import com.example.aerobankapp.workbench.transactions.Transfer;
import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import com.example.aerobankapp.workbench.utilities.BalanceHistoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.aerobankapp.engine.TransactionEngineUtil.buildTransferBalanceSummary;

@Service
public class TransferEngine extends TransactionEngine<Transfer, TransferBalanceSummary> implements Runnable
{
    private final TransferService transferService;
    protected List<Transfer> filteredTransfers = new ArrayList<>();
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
        // NOT IMPLEMENTED
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

    protected Set<Integer> retrieveUserAccountIDSet(List<Transfer> transfers, Function<Transfer, Integer> userIDExtractor){
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
    protected Map<Integer, BigDecimal> retrieveTransactionAmountByAcctID(final List<Transfer> transactionList) {
        if(!transactionList.isEmpty()){
            for(Transfer transfer : transactionList){
                if(transfer != null){
                    final BigDecimal amount = transfer.getAmount();
                    final int fromAccountID = transfer.getFromAccountID();
                    if(amount.compareTo(BigDecimal.ZERO) > 0 && fromAccountID > 0){
                        transactionAmountByAcctIDHashMap.put(fromAccountID, amount);
                    }
                }
            }
        }
        return transactionAmountByAcctIDHashMap;
    }

    @Override
    protected Map<Integer, List<TransferBalanceSummary>> generateBalanceSummaryMap(final List<Transfer> transactions, final Map<Integer, BigDecimal> accountBalances) {
        if(!transactions.isEmpty()){
            for(Transfer transfer : transactions){
                if(transfer != null){
                    int toAccountID = transfer.getToAccountID();
                    int fromAccountID = transfer.getFromAccountID();
                    if(toAccountID > 0 && fromAccountID > 0){
                        BigDecimal fromAccountBalance = accountBalances.get(fromAccountID);
                        BigDecimal toAccountBalance = accountBalances.get(toAccountID);

                        if(fromAccountBalance != null || toAccountBalance != null){
                            TransferBalanceSummary fromAccountBalanceSummary = buildTransferBalanceSummary(transfer, fromAccountBalance, toAccountBalance);
                            balanceSummariesHashMap.computeIfAbsent(fromAccountID, k -> new ArrayList<>()).add(fromAccountBalanceSummary);
                        }
                    }
                }
            }
        }
        return balanceSummariesHashMap;
    }

    @Override
    protected BalanceHistoryEntity createBalanceHistoryEntity(TransferBalanceSummary balanceSummary, BigDecimal currentBalance, BigDecimal adjustedAmount) {
        if(balanceSummary != null){
            if(currentBalance != null || adjustedAmount != null){
                BalanceHistory balanceHistory = BalanceHistoryUtil.convertTransferBalanceSummaryToBalanceHistoryModel(balanceSummary, currentBalance, adjustedAmount);
                return BalanceHistoryUtil.convertBalanceHistoryToEntity(balanceHistory);
            }
        }
        return null;
    }

    @Override
    protected List<BalanceHistoryEntity> convertBalanceSummaryToBalanceHistoryEntityList(List<TransferBalanceSummary> transactionSummaries) {
        if(!transactionSummaries.isEmpty()){
            for(TransferBalanceSummary transferBalanceSummary : transactionSummaries){
                Transfer transfer = transferBalanceSummary.getTransaction();
                if(transfer != null){
                    int toAccountID = transfer.getToAccountID();
                    int fromAccountID = transfer.getFromAccountID();
                    if(toAccountID > 0 && fromAccountID > 0){
                        // Get the balance for the toAccount
                        BigDecimal toAccountBalance = getCurrentBalanceByAcctID(toAccountID);

                        // Get the balance for the from account
                        BigDecimal fromAccountBalance = getCurrentBalanceByAcctID(fromAccountID);

                        // Get the post balance for the from account
                        BigDecimal fromAccountBalanceAfterTransfer = transferBalanceSummary.getPostBalance();

                        BigDecimal adjustedBalanceFromAccount = getAdjustedAmount(fromAccountBalance, fromAccountBalanceAfterTransfer);

                        if(fromAccountBalance != null){
                            BalanceHistoryEntity balanceHistoryEntity = createBalanceHistoryEntity(transferBalanceSummary, fromAccountBalance, adjustedBalanceFromAccount);
                            balanceHistoryList.add(balanceHistoryEntity);
                        }
                    }
                }
            }
        }
        return balanceHistoryList;
    }

    protected List<Transfer> getFilteredUserToUserTransfers(final List<Transfer> unfilteredTransfers){
        if(!unfilteredTransfers.isEmpty()){
            for(Transfer transfer : unfilteredTransfers){
                if(transfer != null){
                    if(isSameUserTransfer(transfer)){
                        filteredTransfers.add(transfer);
                    }
                }
            }
        }
        return filteredTransfers;
    }

    protected boolean isSameUserTransfer(final Transfer transfer){
        return transfer.isUserToUserTransfer();
    }

    protected TransferBalances getTransferCalculation(final BigDecimal amount, final BigDecimal toAccountBalance, final BigDecimal fromAccountBalance){
        if(amount != null || toAccountBalance != null || fromAccountBalance != null){
            // Withdraw from the FromAccount
            BigDecimal newFromBalance = getCalculationEngine().calculateWithdrawal(amount, fromAccountBalance);

            // Deposit to the ToAccount
            BigDecimal newToAccountBalance = getCalculationEngine().calculateDeposit(amount, toAccountBalance);

            return new TransferBalances(newToAccountBalance, newFromBalance);
        }
        // Return an empty constructor
        return new TransferBalances();
    }

    @Override
    public void run() {

    }
}
