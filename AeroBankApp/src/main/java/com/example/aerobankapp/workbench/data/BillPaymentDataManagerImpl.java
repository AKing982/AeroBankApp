package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.entity.BillPaymentHistoryEntity;
import com.example.aerobankapp.entity.BillPaymentScheduleEntity;
import com.example.aerobankapp.services.BillPaymentHistoryService;
import com.example.aerobankapp.services.BillPaymentNotificationService;
import com.example.aerobankapp.services.BillPaymentScheduleService;
import com.example.aerobankapp.services.BillPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

@Component
public class BillPaymentDataManagerImpl implements BillPaymentDataManager
{
    private final BillPaymentService billPaymentService;
    private final BillPaymentScheduleService billPaymentScheduleService;
    private final BillPaymentHistoryService billPaymentHistoryService;
    private final BillPaymentNotificationService billPaymentNotificationService;

    @Autowired
    public BillPaymentDataManagerImpl(BillPaymentService billPaymentService,
                                      BillPaymentScheduleService billPaymentScheduleService,
                                      BillPaymentHistoryService billPaymentHistoryService,
                                      BillPaymentNotificationService billPaymentNotificationService){
        this.billPaymentService = billPaymentService;
        this.billPaymentScheduleService = billPaymentScheduleService;
        this.billPaymentHistoryService = billPaymentHistoryService;
        this.billPaymentNotificationService = billPaymentNotificationService;
    }

    @Override
    public List<BillPaymentEntity> fetchPendingBillPaymentsByUserID(int userID) {
        return null;
    }

    @Override
    public List<BillPaymentEntity> fetchNonProcessedBillPaymentsByUserID(int userID) {
        return null;
    }

    @Override
    public TreeMap<BillPaymentEntity, BillPaymentScheduleEntity> fetchBillPaymentTreeMap(List<BillPaymentEntity> billPaymentEntities, List<BillPaymentScheduleEntity> billPaymentScheduleEntities) {
        return null;
    }

    @Override
    public List<BillPaymentScheduleEntity> fetchBillPaymentSchedulesByPaymentDate(LocalDate paymentDate) {
        return null;
    }

    @Override
    public List<BillPaymentScheduleEntity> fetchBillPaymentSchedulesByDueDate(LocalDate dueDate) {
        return null;
    }

    @Override
    public List<BillPaymentHistoryEntity> fetchBillPaymentHistoryByID(Long id) {
        return null;
    }

    @Override
    public void updateBillPaymentRecord(BillPaymentEntity billPaymentEntity) {

    }

    @Override
    public void updateBillPaymentHistoryRecord(BillPaymentHistoryEntity billPaymentHistory) {

    }

    @Override
    public void updateBillPaymentScheduleRecord(BillPaymentScheduleEntity billPaymentScheduleEntity) {

    }
}
