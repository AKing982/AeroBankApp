package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.TransferEntity;
import com.example.aerobankapp.repositories.TransferRepository;
import com.example.aerobankapp.workbench.utilities.TransferStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransferServiceImpl implements TransferService
{

    private final TransferRepository transferRepository;

    @Autowired
    public TransferServiceImpl(TransferRepository transferRepository)
    {
        this.transferRepository = transferRepository;
    }

    @Override
    public List<TransferEntity> findAll() {
        return null;
    }

    @Override
    public void save(TransferEntity obj) {

    }

    @Override
    public void delete(TransferEntity obj) {

    }

    @Override
    public Optional<TransferEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<TransferEntity> findByUserName(String user) {
        return null;
    }

    @Override
    public List<TransferEntity> getTransfersByUser(String user) {
        return null;
    }

    @Override
    public List<TransferEntity> getTransfersByStatus(TransferStatus status) {
        return null;
    }

    @Override
    public List<TransferEntity> getTransfersByAccount(int acctID) {
        return null;
    }

    @Override
    public boolean cancelTransfer(Long transferID) {
        return false;
    }
}
