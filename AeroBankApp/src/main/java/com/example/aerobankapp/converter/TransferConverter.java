package com.example.aerobankapp.converter;

import com.example.aerobankapp.entity.TransferEntity;
import com.example.aerobankapp.workbench.transactions.Transfer;

public class TransferConverter implements EntityToModelConverter<TransferEntity, Transfer>
{

    @Override
    public Transfer convert(TransferEntity entity) {
        return null;
    }
}
