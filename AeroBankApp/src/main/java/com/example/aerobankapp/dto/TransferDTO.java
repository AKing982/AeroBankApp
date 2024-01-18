package com.example.aerobankapp.dto;

import com.example.aerobankapp.account.AbstractAccountBase;

public record TransferDTO<T extends AbstractAccountBase>(Long transferID,
                                                         String toAccountID,
                                                         String fromAccountID,
                                                         T fromAccount,
                                                         T toAccount)
{

}
