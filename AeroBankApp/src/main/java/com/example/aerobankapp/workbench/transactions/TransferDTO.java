package com.example.aerobankapp.workbench.transactions;

import com.example.aerobankapp.workbench.transactions.base.TransactionBase;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferDTO extends TransactionBase implements Serializable
{
    private Long id;
    private int toUserID;
    private int fromUserID;
    private String toAcctID;
    private String fromAcctID;
    private String fromAcctName;
    private String toAcctName;
    private boolean isPending;


}
