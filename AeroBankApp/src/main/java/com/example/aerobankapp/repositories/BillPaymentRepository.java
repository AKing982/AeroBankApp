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
    @Query("SELECT " +
            "    bp.payeeName, " +
            "    bph.lastPayment, " +
            "    bph.nextPayment, " +
            "    bp.paymentAmount " +
            "FROM BillPaymentEntity bp " +
            "JOIN bp.paymentSchedule bps " +
            "JOIN bps.billPaymentHistory bph " +
            "WHERE bp.user.userID = :userID")
    List<BillPayeeInfoDTO> findBillPaymentScheduleInfoByUserID(@Param("userID") int userID);

}
