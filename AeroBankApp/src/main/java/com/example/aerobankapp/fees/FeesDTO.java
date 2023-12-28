package com.example.aerobankapp.fees;

import com.example.aerobankapp.account.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Builder
public record FeesDTO(String acctID,
                      BigDecimal standardFees,
                      BigDecimal annualFees,
                      BigDecimal lateFees,
                      BigDecimal transactionFees,
                      BigDecimal earlyWithdrawalFee,
                      AccountType type,
                      boolean isEnabled,
                      boolean isAuthorized,
                      LocalDate postingDate) {


}
