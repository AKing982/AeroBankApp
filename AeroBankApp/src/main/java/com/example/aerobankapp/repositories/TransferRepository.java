package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.TransferEntity;
import com.example.aerobankapp.workbench.utilities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity, Long>
{
    @Query("SELECT e FROM TransferEntity e WHERE e.fromUser.username=:user AND e.toUser.username=:user AND e.isUserTransfer=false")
    List<TransferEntity> findSameUserTransfers(@Param("user") String user);

    @Query("SELECT e FROM TransferEntity e WHERE e.fromUser.username=:fromUser AND e.toUser.username=:toUser AND e.isUserTransfer=true")
    List<TransferEntity> findTransfersWithOriginUserAndTargetUser(@Param("fromUser") String fromUser, @Param("toUser") String toUser);

    @Query("SELECT e FROM TransferEntity e WHERE e.status=:status")
    List<TransferEntity> findTransfersByStatus(@Param("status")Status status);

}
