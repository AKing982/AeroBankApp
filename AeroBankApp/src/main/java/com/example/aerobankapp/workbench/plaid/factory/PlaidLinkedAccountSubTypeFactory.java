package com.example.aerobankapp.workbench.plaid.factory;

import com.example.aerobankapp.workbench.plaid.CheckingPlaidLinkSubTypeProcessor;
import com.example.aerobankapp.workbench.plaid.PlaidLinkedAccountConstants;
import com.example.aerobankapp.workbench.plaid.PlaidLinkedAccountProcessor;
import com.example.aerobankapp.workbench.plaid.SavingsPlaidLinkSubTypeProcessor;

import java.util.Locale;

public class PlaidLinkedAccountSubTypeFactory
{
    public PlaidLinkedAccountProcessor getProcessor(String subType){
        switch (subType){
            case PlaidLinkedAccountConstants.CHECKING -> {
                return new CheckingPlaidLinkSubTypeProcessor();
            }
            case PlaidLinkedAccountConstants.SAVINGS -> {
                return new SavingsPlaidLinkSubTypeProcessor();
            }
            default -> throw new IllegalArgumentException("Unsupported subtype: " + subType);
        }
    }
}
