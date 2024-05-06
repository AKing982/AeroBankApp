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
    private int totalTransactionAmountByWeek;
    private String lastTransactionSubmitted;
    private int totalFailedTransactions;
    private int totalTransactionCount;

    public TransactionStatsResponse(String totalTransferred,
                                    String averageValue,
                                    Long pending,
                                    String totalByMonth,
                                    int totalByWeek,
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
                                    int totalWeek,
                                    String totalByMonth,
                                    int totalCount){
        this.totalTransferredAmount = totalTransferred;
        this.averageTransactionValue = averageValue;
        this.pendingTransactionCount = pending;
        this.totalTransactionAmountByWeek = totalWeek;
        this.totalTransactionAmountByMonth = totalByMonth;
        this.totalTransactionCount = totalCount;
    }
}
