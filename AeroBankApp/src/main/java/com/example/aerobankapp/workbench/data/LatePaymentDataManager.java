package com.example.aerobankapp.workbench.data;

import com.example.aerobankapp.entity.LatePaymentEntity;
import com.example.aerobankapp.fees.FeeManager;
import com.example.aerobankapp.fees.FeeManagerImpl;
import com.example.aerobankapp.model.BillPayment;
import com.example.aerobankapp.model.LateBillPayment;
import com.example.aerobankapp.services.LatePaymentService;
import com.example.aerobankapp.services.builder.LatePaymentEntityBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Component
public class LatePaymentDataManager
{
    private final LatePaymentService latePaymentService;

    private final FeeManagerImpl feeManager;

    private final LatePaymentEntityBuilderImpl latePaymentEntityBuilder;

    @Autowired
    public LatePaymentDataManager(LatePaymentService latePaymentService, LatePaymentEntityBuilderImpl latePaymentEntityBuilder,
                                   FeeManagerImpl feeManager){
        this.latePaymentService = latePaymentService;
        this.latePaymentEntityBuilder = latePaymentEntityBuilder;
        this.feeManager = feeManager;
    }

    public List<LatePaymentEntity> fetchAllLatePayments(){
        return latePaymentService.findAll();
    }

    public TreeMap<LocalDate, List<LatePaymentEntity>> getPaymentsTreeMap(final List<LatePaymentEntity> latePayments){
        TreeMap<LocalDate, List<LatePaymentEntity>> paymentsTreeMap = new TreeMap<>();
        for (LatePaymentEntity latePayment : latePayments) {
            LocalDate dueDate = latePayment.getOriginalDueDate();
            paymentsTreeMap.computeIfAbsent(dueDate, k -> new ArrayList<>()).add(latePayment);
        }
        return paymentsTreeMap;
    }

    public int getNumberOfDaysBetweenPaymentDateAndDueDate(LocalDate paymentDate, LocalDate dueDate){
        return (int) ChronoUnit.DAYS.between(dueDate, paymentDate);
    }

    public void saveLatePayment(final LatePaymentEntity latePaymentEntity){
        latePaymentService.save(latePaymentEntity);
    }

    public LatePaymentEntity createPaymentEntity(final LateBillPayment lateBillPayment){
        return latePaymentEntityBuilder.createEntity(lateBillPayment);
    }

    public LateBillPayment buildLatePayment(BillPayment payment){
        LateBillPayment latePayment = new LateBillPayment();
        int daysLate = getNumberOfDaysBetweenPaymentDateAndDueDate(payment.getScheduledPaymentDate(), payment.getDueDate());
        latePayment.setBillPayment(payment);
        latePayment.setDaysLate(daysLate);
        latePayment.setLateFee(feeManager.calculateFeeByDaysLate(daysLate));
        latePayment.setOriginalDueDate(payment.getDueDate());
        return latePayment;
    }
}
