package com.example.aerobankapp.workbench.transactionHistory;

import com.example.aerobankapp.dto.TransactionHistoryDTO;
import com.example.aerobankapp.workbench.utilities.filters.TransactionHistoryFilterType;

import java.util.ArrayList;
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
        List<String> filteredTypes = transactionHistoryDTO.filterType();
        if(!filteredTypes.isEmpty()){
            return filteredTypes;
        }
        return new ArrayList<>();
    }

    public EnumSet<TransactionHistoryFilterType> convertFilteredTypesToEnumSet(){
        EnumSet<TransactionHistoryFilterType> transactionHistoryFilterTypes1 = EnumSet.noneOf(TransactionHistoryFilterType.class);
        List<String> filteredTypes = getFilteredTypesFromRequest();
        for(String e : filteredTypes){
            TransactionHistoryFilterType transactionHistoryFilterType = TransactionHistoryFilterType.getInstance(e);
            if(transactionHistoryFilterType != null){
                transactionHistoryFilterTypes1.add(transactionHistoryFilterType);
            }
        }
        return transactionHistoryFilterTypes1;
    }
}
