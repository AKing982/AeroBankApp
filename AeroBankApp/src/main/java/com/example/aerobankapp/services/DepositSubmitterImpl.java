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
    private final DepositRepository depositRepository;
    private final DepositService depositService;

    @Autowired
    public DepositSubmitterImpl(DepositRepository depositRepository,
                                DepositService depositService,
                                FinancialEncryptionService financialEncryptionService,
                                VaultService vaultService){
        super(vaultService, financialEncryptionService);
        this.depositRepository = depositRepository;
        this.depositService = depositService;
    }

    @Override
    public void submit(DepositDTO depositDTO) {
        DepositsEntity depositsEntity = buildEntity(depositDTO);

        // Save the Deposits entity
        saveEntity(depositsEntity);
    }

    @Override
    protected DepositsEntity buildEntity(DepositDTO dtoObject) {
        return null;
    }

    @Override
    protected void saveEntity(DepositsEntity entity) {

    }


}
