package com.example.aerobankapp.services;

public abstract class AbstractTransactionSubmitter<T, S> {
    protected FinancialEncryptionService financialEncryptionService;

    public AbstractTransactionSubmitter(FinancialEncryptionService financialEncryptionService)
    {
        this.financialEncryptionService = financialEncryptionService;
    }

    protected abstract void submit(T dtoObject);

    protected abstract S buildEntity(T dtoObject);

    protected abstract void saveEntity(S entity);
}
