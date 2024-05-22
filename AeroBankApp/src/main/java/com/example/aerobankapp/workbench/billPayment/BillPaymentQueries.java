package com.example.aerobankapp.workbench.billPayment;

import com.example.aerobankapp.dto.BillPayeeInfoDTO;
import com.example.aerobankapp.dto.PaymentHistoryDTO;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.entity.BillPaymentHistoryEntity;
import com.example.aerobankapp.entity.BillPaymentScheduleEntity;
import com.example.aerobankapp.workbench.sql.QueryUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BillPayeeInfoDTO> criteriaQuery = QueryUtil.createCriteriaQuery(BillPayeeInfoDTO.class, entityManager);
        Root<BillPaymentEntity> billPaymentEntityRoot = QueryUtil.createRoot(criteriaQuery, BillPaymentEntity.class);
        Join<BillPaymentEntity, BillPaymentScheduleEntity> bps = QueryUtil.createJoin(billPaymentEntityRoot, "paymentSchedule");
        Join<BillPaymentScheduleEntity, BillPaymentHistoryEntity> bph = QueryUtil.createJoin(bps, "billPaymentHistory");

        criteriaQuery.select(criteriaBuilder.construct(
                BillPayeeInfoDTO.class,
                billPaymentEntityRoot.get("payeeName"),
                bph.get("lastPayment"),
                bph.get("nextPayment"),
                bps.get("paymentDueDate"),
                billPaymentEntityRoot.get("paymentAmount")
        )).where(QueryUtil.createEqualityPredicate(criteriaBuilder, billPaymentEntityRoot.get("user").get("userID"), userID));

        return QueryUtil.executeQuery(entityManager, criteriaQuery);
    }

    public List<PaymentHistoryDTO> getBillPaymentHistoryQuery(int userID){
        LOGGER.info("Running Bill Payment History Query for userID: {}", userID);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PaymentHistoryDTO> cq = QueryUtil.createCriteriaQuery(PaymentHistoryDTO.class, entityManager);
        Root<BillPaymentEntity> bp = QueryUtil.createRoot(cq, BillPaymentEntity.class);
        Join<BillPaymentEntity, BillPaymentScheduleEntity> bps = QueryUtil.createJoin(bp, "paymentSchedule");

        cq.select(cb.construct(
                PaymentHistoryDTO.class,
                bp.get("payeeName"),
                bp.get("paymentAmount"),
                bp.get("postedDate"),
                bps.get("scheduleStatus")
        )).where(QueryUtil.createEqualityPredicate(cb, bp.get("user").get("userID"), userID));

        return QueryUtil.executeQuery(entityManager, cq);
    }
}
