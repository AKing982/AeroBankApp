package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.WithdrawDTO;
import com.example.aerobankapp.entity.WithdrawEntity;
import com.example.aerobankapp.repositories.WithdrawRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class WithdrawSubmitterImpl extends AbstractTransactionSubmitter<WithdrawDTO, WithdrawEntity> {

    private final WithdrawService withdrawService;

    @Autowired
    public WithdrawSubmitterImpl(WithdrawService withdrawService, FinancialEncryptionService financialEncryptionService) {
        super(financialEncryptionService);
        this.withdrawService = withdrawService;
    }

    @Override
    protected void submit(WithdrawDTO dtoObject) {

    }

    @Override
    protected WithdrawEntity buildEntity(WithdrawDTO dtoObject) {
        return null;
    }

    @Override
    protected void saveEntity(WithdrawEntity entity) {

    }
}