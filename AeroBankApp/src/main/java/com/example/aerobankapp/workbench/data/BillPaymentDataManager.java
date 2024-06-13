package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.entity.BillPaymentHistoryEntity;
import com.example.aerobankapp.entity.BillPaymentScheduleEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

public interface BillPaymentDataManager
{
    List<BillPaymentEntity> fetchPendingBillPaymentsByUserID(int userID);
    List<BillPaymentEntity> fetchNonProcessedBillPaymentsByUserID(int userID);
    TreeMap<BillPaymentEntity, BillPaymentScheduleEntity> fetchBillPaymentTreeMap(List<BillPaymentEntity> billPaymentEntities, List<BillPaymentScheduleEntity> billPaymentScheduleEntities);
    List<BillPaymentScheduleEntity> fetchBillPaymentSchedulesByPaymentDate(LocalDate paymentDate);
    List<BillPaymentScheduleEntity> fetchBillPaymentSchedulesByDueDate(LocalDate dueDate);
    List<BillPaymentHistoryEntity> fetchBillPaymentHistoryByID(Long id);
    LocalDate findLastDueDateByPaymentID(Long id);

    void updateBillPaymentRecord(BillPaymentEntity billPaymentEntity);
    void updateBillPaymentHistoryRecord(BillPaymentHistoryEntity billPaymentHistory);
    void updateBillPaymentScheduleRecord(BillPaymentScheduleEntity billPaymentScheduleEntity);

    Optional<LocalDate> findLastScheduledPaymentDateByPaymentID(Long paymentID);
    Optional<LocalDate> findLastScheduledPaymentDateInScheduleTableByPaymentID(Long paymentID);
}
