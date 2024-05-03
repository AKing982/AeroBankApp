package com.example.aerobankapp.workbench.transactionHistory;

import com.example.aerobankapp.controllers.TransactionHistoryController;
import com.example.aerobankapp.dto.TransactionHistoryDTO;
import com.example.aerobankapp.workbench.utilities.filters.TransactionHistoryFilterType;

import java.util.EnumSet;
import java.util.List;

public class TransactionHistoryParser
{
    private TransactionHistoryDTO transactionHistoryDTO;
    private EnumSet<TransactionHistoryFilterType> transactionHistoryFilterTypes;

    public TransactionHistoryParser(TransactionHistoryDTO transactionHistoryDTO){
        this.transactionHistoryDTO = transactionHistoryDTO;
    }

    public List<String> getFilteredTypesFromRequest(){
        return null;
    }

    public EnumSet<TransactionHistoryFilterType> convertFilteredTypesToEnumSet(){
        return null;
    }
}
