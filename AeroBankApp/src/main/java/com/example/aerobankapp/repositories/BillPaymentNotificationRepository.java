package com.example.aerobankapp.repositories;

import com.example.aerobankapp.entity.BillPaymentNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillPaymentNotificationRepository extends JpaRepository<BillPaymentNotificationEntity, Long>
{

}
