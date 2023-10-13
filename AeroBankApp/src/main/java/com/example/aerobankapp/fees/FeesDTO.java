package com.example.aerobankapp.fees;

import com.example.aerobankapp.account.AccountType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@Component
public class FeesDTO
{
    private String acctID;
    private BigDecimal standardFees;
    private BigDecimal annualFees;
    private BigDecimal lateFees;
    private BigDecimal transactionFees;
    private BigDecimal earlyWithdrawalFee;
    private AccountType accountType;
    private boolean isEnabled;
    private boolean isAuthorized;
    private LocalDate postingDate;

    public FeesDTO()
    {

    }

}
