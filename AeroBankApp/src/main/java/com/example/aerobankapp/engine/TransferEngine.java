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
import com.example.aerobankapp.workbench.utilities.TransferType;
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
    public TransferEngine(TransferService transferService, AccountService accountService, UserService userService, AccountSecurityService accountSecurityService, NotificationService notificationService, CalculationEngine calculationEngine, BalanceHistoryService balanceHistoryService, EncryptionService encryptionService) {
        super(accountService, userService, accountSecurityService, notificationService, calculationEngine, balanceHistoryService, encryptionService);
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

    protected Map<Integer, BigDecimal> getNewAccountBalancesAfterTransfer(final Map<Integer, BigDecimal> transferAmount, final Map<Integer, BigDecimal> currentBalances){
        Map<Integer, BigDecimal> newBalances = new HashMap<>();
        if(!transferAmount.isEmpty() || !currentBalances.isEmpty()){
            transferAmount.forEach((accountID, amount) -> {
                BigDecimal currentBalance = newBalances.getOrDefault(accountID, BigDecimal.ZERO);

                TransferBalances balanceChange = getTransferCalculation(amount, currentBalance, currentBalance);

                newBalances.put(accountID, balanceChange.getToAccountBalance());
            });
        }else{
            throw new NonEmptyListRequiredException("Unable to process Account Balances due to empty maps.");
        }
        return newBalances;
    }

    @Override
    protected Map<Integer, BigDecimal> getCalculatedAccountBalanceMap(final List<Transfer> transactionList) {

        // NOT IMPLEMENTED
        return null;
    }


    @Override
    protected Set<Integer> retrieveAccountIDSet(List<Transfer> transactionList) {
        // NOT IMPLEMENTED
        return null;
    }

    private int getUserIDByAccountNumber(final String accountNumber){
        return getUserService().getUserIDByAccountNumber(accountNumber);
    }

    protected Set<Integer> retrieveTransferToUserIDSet(final List<Transfer> transfers){
        Set<Integer> userIDs = new HashSet<>();
        if(!transfers.isEmpty()){
            for(Transfer transfer : transfers){
                if(transfer != null){
                    // Grab the AccountNumber and Account Code from the transfer
                    String accountNumber = getAccountNumberFromTransfer(transfer);
                    // Use the AccountService to retrieve the UserID
                    int foundUserID = getUserIDByAccountNumber(accountNumber);
                    userIDs.add(foundUserID);
                }
            }
        }
        return userIDs;
    }

    private int getAccountIDByAcctCodeAndAcctNumber(final String acctCode, final String acctNum){
        LOGGER.info("AccountNumber: " + acctNum);
        LOGGER.info("AccountCode: " + acctCode);
        return getAccountService().getAccountIDByAccountCodeAndAccountNumber(acctCode, acctNum);
    }

    protected Set<Integer> retrieveToAccountIDSetByAcctCodeAndAccountNumber(final List<Transfer> transfers){
        Set<Integer> accountIDs = new HashSet<>();
        if(!transfers.isEmpty()){
            for(Transfer transfer : transfers){
                if(transfer != null){
                    String accountNumber = getAccountNumberFromTransfer(transfer);
                    String accountCode = getAccountCodeFromTransfer(transfer);
                    int accountID = getAccountIDByAcctCodeAndAcctNumber(accountCode, accountNumber);
                    accountIDs.add(accountID);
                }
            }
        }
        return accountIDs;
    }

    protected Set<Integer> retrieveTransferAccountIDSet(final List<Transfer> transfers, final Function<Transfer, Integer> accountIDExtractor){
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
                    if(amount != null || fromAccountID > 0){
                        LOGGER.info("Saving amount: $" + amount);
                        LOGGER.info("Saving fromAccountID: " + fromAccountID);
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

    protected List<Transfer> filterByType(final List<Transfer> transfers, final TransferType transferType){
        List<Transfer> filteredList = new ArrayList<>();
        for(Transfer transfer : transfers){
            if(transfer.getTransferType().equals(transferType)){
                filteredList.add(transfer);
            }
        }
        return filteredList;
    }


    protected Map<TransferType, List<Transfer>> filterTransfersByType(final List<Transfer> unfilteredTransfers, final TransferType transferType){
        Map<TransferType, List<Transfer>> filteredTransfers = new HashMap<>();
        if(!unfilteredTransfers.isEmpty()){
            List<Transfer> filteredList = filterByType(unfilteredTransfers, transferType);
            filteredTransfers.put(transferType, filteredList);
        }
        return filteredTransfers;
    }

    protected TransferType getTransferType(final Transfer transfer){
        return transfer.getTransferType();
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

    protected boolean processUserToUserTransfers(final List<Transfer> userToUserTransfer){
        if (!userToUserTransfer.isEmpty()) {
            // Step 1: Filter Transfers for USER_TO_USER type.
            Map<TransferType, List<Transfer>> userToUserTransferMap = filterTransfersByType(userToUserTransfer, TransferType.USER_TO_USER);

            // Get the List of USER TO USER transfers
            List<Transfer> userToUserList = userToUserTransferMap.get(TransferType.USER_TO_USER);

            // Step 2 & 3: Retrieve Account and User IDs.
            Set<Integer> toAccountIDs = retrieveToAccountIDSetByAcctCodeAndAccountNumber(userToUserTransferMap.get(TransferType.USER_TO_USER));
            Set<Integer> userIDs = retrieveTransferToUserIDSet(userToUserTransferMap.get(TransferType.USER_TO_USER));

            Map<Integer, BigDecimal> currentAccountBalances = retrieveCurrentAccountBalancesByAcctID(toAccountIDs);

            Map<Integer, BigDecimal> transactionAmountMap = retrieveTransactionAmountByAcctID(userToUserList);

            // Assume retrieval and calculation logic for current and new balances is correctly implemented.
            Map<Integer, BigDecimal> fromAccountBalances = getCalculatedAccountBalanceMap(userToUserTransferMap.get(TransferType.USER_TO_USER));

            // Step 4: Calculate New Balances.
            Map<Integer, BigDecimal> newBalances = getNewAccountBalancesAfterTransfer(transactionAmountMap, currentAccountBalances); // This method might need adjustments to correctly handle "from" and "to" balances.

            // Step 5: Update Balances.
            int status = bulkUpdateAccountBalances(newBalances);
            if(status == 1){
                return true;
            }
        }
        return false;
    }

    private String getAccountCodeFromTransfer(final Transfer transfer){
        return transfer.getToAccountCode();
    }

    private String getAccountNumberFromTransfer(final Transfer transfer){
        return transfer.getToAccountNumber();
    }


    protected boolean processSameUserTransfers(final List<Transfer> sameUserTransfers){
        if (!sameUserTransfers.isEmpty()) {
            // Step 1: Extract Account IDs.
            Set<Integer> fromAccountIDs = retrieveTransferAccountIDSet(sameUserTransfers, Transfer::getFromAccountID);
            Set<Integer> toAccountIDs = retrieveTransferAccountIDSet(sameUserTransfers, Transfer::getToAccountID);

            // Step 2: Retrieve Current Balances.
            Map<Integer, BigDecimal> fromAccountBalance = retrieveCurrentAccountBalancesByAcctID(fromAccountIDs);
            Map<Integer, BigDecimal> toAccountBalance = retrieveCurrentAccountBalancesByAcctID(toAccountIDs);

            // Step 3: Calculate New Balances.
            Map<Integer, BigDecimal> transactionAmountByAcctID = retrieveTransactionAmountByAcctID(sameUserTransfers);
            Map<Integer, BigDecimal> calculatedNewBalances = getNewAccountBalancesAfterTransfer(transactionAmountByAcctID, fromAccountBalance); // Adjust this logic as necessary.

            // Step 4: Update Account Balances.
            int status = bulkUpdateAccountBalances(calculatedNewBalances);
            if(status == 1){
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {

    }
}
