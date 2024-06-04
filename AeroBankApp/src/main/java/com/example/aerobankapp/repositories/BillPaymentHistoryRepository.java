package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.BillPaymentHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BillPaymentHistoryRepository extends JpaRepository<BillPaymentHistoryEntity, Long>
{
    @Query("SELECT e.isProcessed FROM BillPaymentHistoryEntity e WHERE e.paymentHistoryID =: id")
    boolean findProcessedBillPaymentHistory(@Param("id") Long id);
}
