package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.TransferEntity;
import com.example.aerobankapp.model.ServiceDAOModel;
import com.example.aerobankapp.workbench.utilities.TransferStatus;

import java.util.List;
import java.util.Optional;

public interface TransferService extends ServiceDAOModel<TransferEntity>
{
    @Override
    List<TransferEntity> findAll();

    @Override
    void save(TransferEntity obj);

    @Override
    void delete(TransferEntity obj);

    @Override
    Optional<TransferEntity> findAllById(Long id);

    @Override
    List<TransferEntity> findByUserName(String user);

    List<TransferEntity> getSameUserTransfer(String user);

    List<TransferEntity> getTransfersFromOriginUserToTargetUser(String originUser, String targetUser);

    List<TransferEntity> getTransfersByStatus(TransferStatus status);

    List<TransferEntity> getSameUserTransferByAccount(int acctID);

    boolean updateTransfer(TransferEntity obj);

    Optional<TransferEntity> getTransferById(Long id);

    List<TransferEntity> getTransfersByFromAccount(int acctID);

    List<TransferEntity> getTransfersByToAccount(int acctID);

    List<TransferEntity> getTransfersByUserId(int userId);

    boolean deleteTransferById(Long id);
}
