package com.example.aerobankapp.converter;

import com.example.aerobankapp.entity.TransferEntity;
import com.example.aerobankapp.workbench.transactions.Transfer;

import java.time.LocalDate;

public class TransferConverter implements EntityToModelConverter<TransferEntity, Transfer>
{

    @Override
    public Transfer convert(TransferEntity entity) {
        Transfer transfer = new Transfer();
        transfer.setTransferID(entity.getTransferID());
        transfer.setUserToUserTransfer(entity.isUserTransfer());
        transfer.setFromAccountID(entity.getFromAccount().getAcctID());
        transfer.setOriginUserID(entity.getFromUser().getUserID());
        transfer.setTargetUserID(entity.getToUser().getUserID());
        transfer.setAmount(entity.getTransferAmount());
        transfer.setDescription(entity.getDescription());
        transfer.setPosted(LocalDate.now());
        transfer.setToAccountID(entity.getToAccount().getAcctID());
        transfer.setToAccountCode(entity.getToAccount().getAccountCode());
        transfer.setToAccountNumber(entity.getToUser().getAccountNumber());
        return transfer;
    }
}
