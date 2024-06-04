package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.BillPaymentHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillPaymentHistoryRepository extends JpaRepository<BillPaymentHistoryEntity, Long>
{
    @Query("SELECT e.isProcessed FROM BillPaymentHistoryEntity e WHERE e.paymentHistoryID =:id")
    Boolean findProcessedBillPaymentHistory(@Param("id") Long id);

    @Query("SELECT e FROM BillPaymentHistoryEntity e WHERE e.paymentHistoryID=:id")
    Optional<BillPaymentHistoryEntity> findByPaymentHistoryID(@Param("id") Long id);
}
