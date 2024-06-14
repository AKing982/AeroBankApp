package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.entity.BillPaymentHistoryEntity;
import com.example.aerobankapp.entity.BillPaymentScheduleEntity;
import com.example.aerobankapp.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

@Component
public class BillPaymentDataManagerImpl implements BillPaymentDataManager
{
    private final BillPaymentService billPaymentService;
    private final BillPaymentScheduleService billPaymentScheduleService;
    private final BillPaymentHistoryService billPaymentHistoryService;
    private final BillPaymentNotificationService billPaymentNotificationService;
    private BillPaymentDateQueryService billPaymentDateQueryService;
    private final Logger LOGGER = LoggerFactory.getLogger(BillPaymentDataManagerImpl.class);

    @Autowired
    public BillPaymentDataManagerImpl(BillPaymentService billPaymentService,
                                      BillPaymentScheduleService billPaymentScheduleService,
                                      BillPaymentHistoryService billPaymentHistoryService,
                                      BillPaymentNotificationService billPaymentNotificationService,
                                      BillPaymentDateQueryService billPaymentDateQueryService){
        this.billPaymentService = billPaymentService;
        this.billPaymentScheduleService = billPaymentScheduleService;
        this.billPaymentHistoryService = billPaymentHistoryService;
        this.billPaymentNotificationService = billPaymentNotificationService;
        this.billPaymentDateQueryService = billPaymentDateQueryService;
    }

    @Override
    public Collection<BillPaymentEntity> findAllBillPayments() {
        return billPaymentService.findAll();
    }

    @Override
    public Collection<BillPaymentScheduleEntity> findAllBillPaymentSchedules() {
        return billPaymentScheduleService.findAll();
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
    public LocalDate findLastDueDateByPaymentID(Long id) {
        Optional<LocalDate> lastDueDate = billPaymentDateQueryService.findPaymentDueDateById(id);
        LOGGER.info("Last due date found: {}", lastDueDate);
        return lastDueDate.get();
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

    @Override
    public Optional<LocalDate> findLastScheduledPaymentDateByPaymentID(Long paymentID) {
        return billPaymentDateQueryService.findScheduledPaymentDateById(paymentID);
    }

    @Override
    public Optional<LocalDate> findLastScheduledPaymentDateInScheduleTableByPaymentID(Long paymentID) {
        return billPaymentDateQueryService.findScheduledPaymentByScheduleTableById(paymentID);
    }
}
