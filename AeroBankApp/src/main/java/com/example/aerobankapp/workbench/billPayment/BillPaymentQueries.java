package com.example.aerobankapp.workbench.billPayment;

import com.example.aerobankapp.dto.BillPayeeInfoDTO;
import com.example.aerobankapp.dto.PaymentHistoryDTO;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.entity.BillPaymentHistoryEntity;
import com.example.aerobankapp.entity.BillPaymentScheduleEntity;
import com.example.aerobankapp.workbench.sql.QueryUtil;



import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;

import static com.example.aerobankapp.workbench.sql.QueryUtil.executeQuery;

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
        CriteriaBuilder criteriaBuilder = createCriteriaBuilder();
        CriteriaQuery<BillPayeeInfoDTO> criteriaQuery = createCriteriaQuery(criteriaBuilder);
        Root<BillPaymentEntity> billPaymentEntityRoot = createRoot(criteriaQuery);
        Join<BillPaymentEntity, BillPaymentScheduleEntity> bps = createPaymentScheduleJoin(billPaymentEntityRoot);
        Join<BillPaymentScheduleEntity, BillPaymentHistoryEntity> bph = createPaymentHistoryJoin(bps);

        criteriaQuery.select(createSelection(criteriaBuilder, criteriaQuery, billPaymentEntityRoot, bps, bph))
                .where(createEqualityPredicate(criteriaBuilder, billPaymentEntityRoot, userID));

        return executeQuery(entityManager, criteriaQuery);
    }

    private CriteriaBuilder createCriteriaBuilder(){
        return entityManager.getCriteriaBuilder();
    }

    private CriteriaQuery<BillPayeeInfoDTO> createCriteriaQuery(CriteriaBuilder criteriaBuilder){
        return QueryUtil.createCriteriaQuery(BillPayeeInfoDTO.class, entityManager);
    }

    private Root<BillPaymentEntity> createRoot(CriteriaQuery<BillPayeeInfoDTO> criteriaQuery){
        return QueryUtil.createRoot(criteriaQuery, BillPaymentEntity.class);
    }

    private Join<BillPaymentEntity, BillPaymentScheduleEntity> createPaymentScheduleJoin(Root<BillPaymentEntity> root){
        return QueryUtil.createJoin(root, "paymentSchedule");
    }

    private Join<BillPaymentScheduleEntity, BillPaymentHistoryEntity> createPaymentHistoryJoin(Join<BillPaymentEntity, BillPaymentScheduleEntity> join){
        return QueryUtil.createJoin(join, "billPaymentHistory");
    }

    private Predicate createEqualityPredicate(CriteriaBuilder criteriaBuilder, Root<BillPaymentEntity> root, int userID){
        return QueryUtil.createEqualityPredicate(criteriaBuilder, root.get("user").get("userID"), userID);
    }

    private Selection<BillPayeeInfoDTO> createSelection(CriteriaBuilder criteriaBuilder,  CriteriaQuery<BillPayeeInfoDTO> criteriaQuery,
                                                        Root<BillPaymentEntity> billPaymentEntityRoot, Join<BillPaymentEntity, BillPaymentScheduleEntity> bps,
                                                        Join<BillPaymentScheduleEntity, BillPaymentHistoryEntity> bph){
        return criteriaBuilder.construct(
                BillPayeeInfoDTO.class,
                billPaymentEntityRoot.get("payeeName"),
                bph.get("lastPayment"),
                bph.get("nextPayment"),
                bps.get("paymentDueDate"),
                billPaymentEntityRoot.get("paymentAmount")
        );
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

        return executeQuery(entityManager, cq);
    }
}
