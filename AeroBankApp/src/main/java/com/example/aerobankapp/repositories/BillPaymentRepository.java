package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.BillPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillPaymentRepository extends JpaRepository<BillPaymentEntity, Long>
{

}
