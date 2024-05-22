package com.example.aerobankapp.workbench.billPayment;

import com.example.aerobankapp.dto.BillPayeeInfoDTO;
import com.example.aerobankapp.dto.PaymentHistoryDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BillPaymentQueries
{
    @PersistenceContext
    private EntityManager entityManager;

    private Logger LOGGER = LoggerFactory.getLogger(BillPaymentQueries.class);

    @Autowired
    public BillPaymentQueries(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public List<BillPayeeInfoDTO> getBillPaymentScheduleQuery(int userID) {
        String query = "SELECT new com.example.aerobankapp.dto.BillPayeeInfoDTO( " +
                "    bp.payeeName, " +
                "    bph.lastPayment, " +
                "    bph.nextPayment," +
                "    bps.paymentDueDate, " +
                "    bp.paymentAmount) " +
                "FROM BillPaymentEntity bp " +
                "JOIN bp.paymentSchedule bps " +
                "JOIN bps.billPaymentHistory bph " +
                "WHERE bp.user.userID = :userID";

        LOGGER.info("Running Query: " + query);
        TypedQuery<BillPayeeInfoDTO> billPaymentEntityTypedQuery = entityManager.createQuery(query, BillPayeeInfoDTO.class);
        billPaymentEntityTypedQuery.setParameter("userID", userID);

        return billPaymentEntityTypedQuery.getResultList();
    }

    public List<PaymentHistoryDTO> getBillPaymentHistoryQuery(int userID){
        String paymentHistoryQuery = "SELECT new com.example.aerobankapp.dto.PaymentHistoryDTO( " +
                "bp.payeeName, " +
                "bp.paymentAmount,  " +
                "bp.postedDate, " +
                "bps.scheduleStatus) " +
                "FROM BillPaymentEntity bp " +
                "JOIN bp.paymentSchedule bps " +
                "WHERE bp.user.userID =:userID";

        TypedQuery<PaymentHistoryDTO> paymentHistoryDTOTypedQuery = entityManager.createQuery(paymentHistoryQuery, PaymentHistoryDTO.class);
        paymentHistoryDTOTypedQuery.setParameter("userID", userID);
        return paymentHistoryDTOTypedQuery.getResultList();
    }
}
