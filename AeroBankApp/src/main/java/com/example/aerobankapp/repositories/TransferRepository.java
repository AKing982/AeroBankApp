package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.TransferEntity;
import com.example.aerobankapp.workbench.utilities.Status;
import com.example.aerobankapp.workbench.utilities.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity, Long>
{
    @Query("SELECT e FROM TransferEntity e WHERE e.fromUser.userCredentials.username=:user AND e.toUser.userCredentials.username=:user AND e.transferType='SAME_USER'")
    List<TransferEntity> findSameUserTransfers(@Param("user") String user);

    @Query("SELECT e FROM TransferEntity e WHERE e.fromUser.userCredentials.username=:fromUser AND e.toUser.userCredentials.username=:toUser AND e.transferType='USER_USER'")
    List<TransferEntity> findTransfersWithOriginUserAndTargetUser(@Param("fromUser") String fromUser, @Param("toUser") String toUser);

    @Query("SELECT e FROM TransferEntity e JOIN e.criteria c WHERE c.transactionStatus=:status")
    List<TransferEntity> findTransfersByStatus(@Param("status") TransferStatus status);

    @Query("SELECT e FROM TransferEntity e WHERE e.transferID =:id")
    Optional<TransferEntity> findById(@Param("id") Long id);

    @Query("SELECT e FROM TransferEntity e WHERE e.fromAccount.acctID=:id")
    List<TransferEntity> findByFromAccount(@Param("id") Long id);

    @Query("SELECT e FROM TransferEntity e WHERE e.toAccount.acctID =:id")
    List<TransferEntity> findByToAccount(@Param("id") Long id);

    @Query("DELETE FROM TransferEntity e WHERE e.transferID =:id")
    @Modifying
    Boolean deleteById(@Param("id") long id);

}
