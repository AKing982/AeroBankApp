package com.example.aerobankapp.services;

public abstract class AbstractTransactionSubmitter<T, S> {
    protected VaultService vaultService;
    protected FinancialEncryptionService financialEncryptionService;

    public AbstractTransactionSubmitter(VaultService vaultService,
                                        FinancialEncryptionService financialEncryptionService)
    {
        this.vaultService = vaultService;
        this.financialEncryptionService = financialEncryptionService;
    }

    protected abstract void submit(T dtoObject);

    protected abstract S buildEntity(T dtoObject);

    protected abstract void saveEntity(S entity);
}
