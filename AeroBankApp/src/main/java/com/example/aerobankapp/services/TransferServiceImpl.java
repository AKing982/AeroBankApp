package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.TransferEntity;
import com.example.aerobankapp.exceptions.InvalidUserStringException;
import com.example.aerobankapp.exceptions.NoTransferEntitiesFoundException;
import com.example.aerobankapp.exceptions.NullTransferEntityFoundException;
import com.example.aerobankapp.exceptions.StatusNotFoundException;
import com.example.aerobankapp.repositories.TransferRepository;
import com.example.aerobankapp.workbench.utilities.TransferStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransferServiceImpl implements TransferService
{

    private final TransferRepository transferRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(TransferServiceImpl.class);

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
        LOGGER.info("Saving Transfer Entity: {}", obj.toString());
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
    public List<TransferEntity> findByUserName(final String user) {
        // NOT IMPLEMENTED
        return null;
    }

    @Override
    public List<TransferEntity> getSameUserTransfer(final String user) {
        if(user.isEmpty()){
            throw new IllegalArgumentException("Invalid user string found.");
        }

        List<TransferEntity> transferEntities = transferRepository.findSameUserTransfers(user);
        if(transferEntities.isEmpty()){
            throw new NoTransferEntitiesFoundException("Retrieving Empty Transfer Entity List.");
        }
        return transferEntities;
    }

    @Override
    public List<TransferEntity> getTransfersFromOriginUserToTargetUser(final String originUser, final String targetUser) {
        if(originUser.isEmpty() || targetUser.isEmpty()){
            throw new InvalidUserStringException("Caught invalid Origin User String: " + originUser);
        }

        List<TransferEntity> transferEntities = transferRepository.findTransfersWithOriginUserAndTargetUser(originUser, targetUser);
        if(transferEntities.isEmpty()){
            throw new NoTransferEntitiesFoundException("Retrieving Empty Transfer Entity list.");
        }
        return transferEntities;
    }


    @Override
    public List<TransferEntity> getTransfersByStatus(TransferStatus status) {
        if(status == null){
            throw new StatusNotFoundException("Transfer Status not found.");
        }

        List<TransferEntity> transfersByStatus = transferRepository.findTransfersByStatus(status);
        if(transfersByStatus.isEmpty()){
            throw new NoTransferEntitiesFoundException("Retrieving Empty Transfer Entity list.");
        }
        return transfersByStatus;
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
