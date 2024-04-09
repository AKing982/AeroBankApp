package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.DepositDTO;
import com.example.aerobankapp.entity.DepositsEntity;
import com.example.aerobankapp.repositories.DepositRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class DepositSubmitterImpl extends AbstractTransactionSubmitter<DepositDTO, DepositsEntity>
{
    private final DepositService depositService;

    @Autowired
    public DepositSubmitterImpl(DepositService depositService,
                                FinancialEncryptionService financialEncryptionService){
        super(financialEncryptionService);
        this.depositService = depositService;
    }

    @Override
    public void submit(DepositDTO depositDTO) {

    }

    @Override
    protected DepositsEntity buildEntity(DepositDTO dtoObject) {
        return null;
    }

    @Override
    protected void saveEntity(DepositsEntity entity) {

    }


}
