package com.example.aerobankapp.workbench.utilities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public final class TransactionSecurity
{
    private boolean withdraw_enabled;
    private boolean purchase_enabled;
    private boolean deposit_enabled;
    private boolean transfer_enabled;
    private int withdrawal_limit;
    private int deposit_limit;
    private int transfer_limit;
    private int purchase_limit;
}
