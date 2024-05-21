package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.BillPaymentScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillPaymentScheduleRepository extends JpaRepository<BillPaymentScheduleEntity, Long>
{

}
