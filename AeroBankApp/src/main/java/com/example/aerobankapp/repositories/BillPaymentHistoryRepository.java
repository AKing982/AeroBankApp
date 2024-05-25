package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.BillPaymentHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillPaymentHistoryRepository extends JpaRepository<BillPaymentHistoryEntity, Long>
{

}
