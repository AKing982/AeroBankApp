package com.example.aerobankapp.workbench.transactions;

import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Purchase extends TransactionBase implements Serializable
{
    private Long id;
    private String accountID;


}
