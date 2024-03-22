package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.TransferEntity;
import com.example.aerobankapp.exceptions.NoTransferEntitiesFoundException;
import com.example.aerobankapp.exceptions.NullTransferEntityFoundException;
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
    public List<TransferEntity> findAll()
    {
        List<TransferEntity> transferEntities = transferRepository.findAll();
        if(transferEntities.isEmpty()){
            throw new NoTransferEntitiesFoundException("No Transfer Entities were found.");
        }
        return transferEntities;
    }

    @Override
    public void save(TransferEntity obj) {
        if(obj == null){
            throw new NullTransferEntityFoundException("Caught Null Transfer Entity.");
        }
        transferRepository.save(obj);
    }

    @Override
    public void delete(TransferEntity obj) {
        if(obj == null){
            throw new NullTransferEntityFoundException("Caught Null Transfer Entity.");
        }
        transferRepository.delete(obj);
    }

    @Override
    public Optional<TransferEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<TransferEntity> findByUserName(String user) {
        // NOT IMPLEMENTED
        return null;
    }

    @Override
    public List<TransferEntity> getSameUserTransfer(String user) {
        return null;
    }

    @Override
    public List<TransferEntity> getTransfersFromOriginUserToTargetUser(String originUser, String targetUser) {
        return null;
    }


    @Override
    public List<TransferEntity> getTransfersByStatus(TransferStatus status) {
        return null;
    }

    @Override
    public List<TransferEntity> getSameUserTransferByAccount(int acctID) {
        return null;
    }


    @Override
    public boolean cancelTransfer(Long transferID) {
        return false;
    }
}
