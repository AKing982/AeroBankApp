package com.example.aerobankapp.workbench.utilities.conversion;

import com.example.aerobankapp.dto.TransferDTO;
import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.model.ConfirmationNumber;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.services.TransactionCriteriaService;
import com.example.aerobankapp.services.TransactionScheduleCriteriaService;
import com.example.aerobankapp.services.UserService;
import com.example.aerobankapp.workbench.generator.confirmation.ConfirmationNumberGenerator;
import com.example.aerobankapp.workbench.utilities.TransactionStatus;
import org.intellij.lang.annotations.JdkConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TransferMapper {
    private final AccountService accountService;
    private final UserService userService;
    private final ConfirmationNumberGenerator confirmationNumberGenerator;
    private final TransactionCriteriaService transactionCriteriaService;
    private final TransactionScheduleCriteriaService transactionScheduleCriteriaService;

    @Autowired
    public TransferMapper(AccountService accountService, UserService userService,
                          ConfirmationNumberGenerator confirmationNumberGenerator,
                          TransactionCriteriaService transactionCriteriaService,
                          TransactionScheduleCriteriaService transactionScheduleCriteriaService)
    {
        this.accountService = accountService;
        this.userService = userService;
        this.confirmationNumberGenerator = confirmationNumberGenerator;
        this.transactionCriteriaService = transactionCriteriaService;
        this.transactionScheduleCriteriaService = transactionScheduleCriteriaService;
    }

    public TransferEntity fromDTO(TransferDTO transferDTO) {
        TransferEntity transfer = new TransferEntity();
        configureCriteria(transfer, transferDTO);
        configureUsers(transfer, transferDTO);
        configureAccounts(transferDTO, transfer);
        transfer.setTransferType(transfer.getTransferType());
        transfer.setTransferID(transferDTO.transferID());
        return transfer;
    }

    private void configureAccounts(TransferDTO transferDTO, TransferEntity transferEntity) {
        AccountEntity toAccount = getAccountEntityFromDatabase(transferDTO.toAccountID());
        AccountEntity fromAccount = getAccountEntityFromDatabase(transferDTO.fromAccountID());
        transferEntity.setToAccount(toAccount);
        transferEntity.setFromAccount(fromAccount);
    }

    private void configureUsers(TransferEntity transferEntity, TransferDTO transferDTO) {
        UserEntity fromUser = getUserEntityFromDatabase(transferDTO.fromUserID());
        UserEntity toUser = getUserEntityFromDatabase(transferDTO.toUserID());

        transferEntity.setFromUser(fromUser);
        transferEntity.setToUser(toUser);
    }

    private void configureCriteria(TransferEntity transferEntity, TransferDTO transferDTO) {
        TransactionScheduleCriteriaEntity scheduleCriteria = buildTransactionSchedule(transferDTO);
        saveTransactionScheduleCriteria(scheduleCriteria);

        Integer confirmationNumber = confirmationNumberGenerator.generateConfirmationNumber().getConfirmationValue();
        TransactionCriteriaEntity transactionCriteriaEntity = buildTransactionCriteria(transferDTO, scheduleCriteria, confirmationNumber);
        saveTransactionCriteria(transactionCriteriaEntity);

        transferEntity.setCriteria(transactionCriteriaEntity);
    }


    private AccountEntity getAccountEntityFromDatabase(int accountID) {
        return accountService.findById(accountID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid accountID: " + accountID));
    }

    private UserEntity getUserEntityFromDatabase(int userID) {
        return userService.findById(userID);
    }

    private void saveTransactionCriteria(TransactionCriteriaEntity transactionCriteria)
    {
        transactionCriteriaService.save(transactionCriteria);
    }

    private void saveTransactionScheduleCriteria(TransactionScheduleCriteriaEntity transactionScheduleCriteria){
        transactionScheduleCriteriaService.save(transactionScheduleCriteria);
    }

    private TransactionScheduleCriteriaEntity buildTransactionSchedule(TransferDTO transferDTO) {
        TransactionScheduleCriteriaEntity transactionScheduleCriteria = new TransactionScheduleCriteriaEntity();
        transactionScheduleCriteria.setScheduledDate(transferDTO.transferDate());
        transactionScheduleCriteria.setScheduledTime(transferDTO.transferTime());
        return transactionScheduleCriteria;
    }

    private TransactionCriteriaEntity buildTransactionCriteria(TransferDTO transferDTO, TransactionScheduleCriteriaEntity criteria, int confirmationNumber) {
        TransactionCriteriaEntity transactionCriteria = new TransactionCriteriaEntity();
        transactionCriteria.setAmount(transferDTO.transferAmount());
        transactionCriteria.setDescription(transferDTO.transferDescription());
        transactionCriteria.setPosted(LocalDate.now()); // Make sure the 'now()' is correct in your context
        transactionCriteria.setTransactionScheduleCriteria(criteria);
        transactionCriteria.setConfirmationNumber(confirmationNumber);
        transactionCriteria.setReferenceNumber("e2342342342342");
        transactionCriteria.setNotificationsEnabled(transactionCriteria.isNotificationsEnabled());
        transactionCriteria.setTransactionStatus(TransactionStatus.PENDING); // Assuming pending status for all new transactions
        return transactionCriteria;
    }
}
