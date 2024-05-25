package com.example.aerobankapp.repositories;

import com.example.aerobankapp.dto.BillPayeeInfoDTO;
import com.example.aerobankapp.entity.BillPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillPaymentRepository extends JpaRepository<BillPaymentEntity, Long>
{
    @Query("SELECT e FROM BillPaymentEntity e WHERE e.user.userID=:userID")
    List<BillPaymentEntity> findBillPaymentsByUserID(@Param("userID") int userID);
}
