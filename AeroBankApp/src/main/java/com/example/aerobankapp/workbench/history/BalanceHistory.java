package com.example.aerobankapp.workbench.history;

import com.example.aerobankapp.workbench.history.historyModel.HistoryModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Component
@AllArgsConstructor(access=AccessLevel.PUBLIC)
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
@Deprecated
public class BalanceHistory implements HistoryModel
{
    private int id;
    private String acctID;
    private String user;
    private int transactionID;
    private BigDecimal balance;
    private BigDecimal adjusted;
    private BigDecimal lastBalance;
    private LocalDate posted;

    @Override
    public BalanceHistory getBalanceHistory()
    {
        return new BalanceHistory(id, acctID, user, transactionID, balance, adjusted, lastBalance, posted);
    }
}
