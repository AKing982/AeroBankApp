package com.example.aerobankapp.workbench.utilities.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class TransactionStatsResponse implements Serializable
{
    private String totalTransferredAmount;
    private String averageTransactionValue;
    private Long pendingTransactionCount;
    private String totalTransactionAmountByMonth;
    private String totalTransactionAmountByWeek;
    private String lastTransactionSubmitted;
    private int totalFailedTransactions;
    private int totalTransactionCount;

    public TransactionStatsResponse(String totalTransferred,
                                    String averageValue,
                                    Long pending,
                                    String totalByMonth,
                                    String totalByWeek,
                                    String lastSubmitted,
                                    int totalFailed,
                                    int totalCount){
        this.totalTransferredAmount = totalTransferred;
        this.averageTransactionValue = averageValue;
        this.pendingTransactionCount = pending;
        this.totalTransactionAmountByMonth = totalByMonth;
        this.totalTransactionAmountByWeek = totalByWeek;
        this.lastTransactionSubmitted = lastSubmitted;
        this.totalFailedTransactions = totalFailed;
        this.totalTransactionCount = totalCount;
    }

    public TransactionStatsResponse(String totalTransferred,
                                    String averageValue,
                                    Long pending,
                                    String totalByMonth){
        this.totalTransferredAmount = totalTransferred;
        this.averageTransactionValue = averageValue;
        this.pendingTransactionCount = pending;
        this.totalTransactionAmountByMonth = totalByMonth;
    }
}
