package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.model.BalanceHistory;
import com.example.aerobankapp.services.BalanceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BalanceHistoryDataManager {

    private BalanceHistoryService balanceHistoryService;

    @Autowired
    public BalanceHistoryDataManager(BalanceHistoryService balanceHistoryService){
        this.balanceHistoryService = balanceHistoryService;
    }


    public BalanceHistory createBalanceHistoryModel(BigDecimal newBalance, BigDecimal adjusted, BigDecimal prevBalance, int acctID){
        BalanceHistory balanceHistory = new BalanceHistory();
        balanceHistory.setNewBalance(newBalance);
        balanceHistory.setCurrentBalance(prevBalance);
        balanceHistory.setAdjustedAmount(adjusted);
        balanceHistory.setAccountID(acctID);
        return balanceHistory;
    }

}
