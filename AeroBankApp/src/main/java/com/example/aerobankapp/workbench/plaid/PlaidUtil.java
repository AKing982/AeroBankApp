package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.model.PlaidTransaction;
import com.example.aerobankapp.model.PlaidTransactionImport;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class PlaidUtil
{
    public static Set<String> convertPlaidSubTypeEnumListToStrings()
    {
        List<PlaidSubType> plaidSubTypes = Arrays.stream(PlaidSubType.values()).toList();
        Set<String> plaidSubTypesStrings = new HashSet<>();
        for(PlaidSubType plaidSubType : plaidSubTypes){
            String subtypeAsString = plaidSubType.toString();
            plaidSubTypesStrings.add(subtypeAsString);
        }
        return plaidSubTypesStrings;
    }

    public static Set<String> convertPlaidAccountTypeEnumListToStrings()
    {
        List<PlaidAccountType> plaidAccountTypes = Arrays.stream(PlaidAccountType.values()).toList();
        Set<String> plaidAccountTypesStrings = new HashSet<>();
        for(PlaidAccountType plaidAccountType : plaidAccountTypes){
            plaidAccountTypesStrings.add(plaidAccountType.toString());
        }
        return plaidAccountTypesStrings;
    }

    public static PlaidTransactionImport createPlaidTransactionImport(PlaidTransaction plaidTransaction, String referenceNumber) {
        PlaidTransactionImport plaidTransactionImport = new PlaidTransactionImport();
        plaidTransactionImport.setTransactionId(plaidTransaction.getTransactionId());
        plaidTransactionImport.setTransactionDate(plaidTransaction.getDate());
        plaidTransactionImport.setAmount(plaidTransaction.getAmount());
        plaidTransactionImport.setTransactionName(plaidTransaction.getTransactionName());
        plaidTransactionImport.setAcctID(plaidTransaction.getAccountId());
        plaidTransactionImport.setPosted(plaidTransaction.getDate());
        plaidTransactionImport.setPending(plaidTransaction.isPending());
        plaidTransactionImport.setReferenceNumber(referenceNumber);
        return plaidTransactionImport;
    }
}
