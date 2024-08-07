package com.example.aerobankapp.workbench.utilities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PendingAmountResponse
{
    private String pendingAmount;

    public PendingAmountResponse(String amount){
        this.pendingAmount = amount;
    }

}
