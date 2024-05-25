package com.example.aerobankapp.workbench.billPayment;


import com.example.aerobankapp.configuration.AppConfig;
import com.example.aerobankapp.configuration.JpaConfig;
import com.example.aerobankapp.dto.BillPayeeInfoDTO;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.entity.BillPaymentHistoryEntity;
import com.example.aerobankapp.entity.BillPaymentScheduleEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.*;
@DataJpaTest
@ExtendWith(MockitoExtension.class)
@Import({JpaConfig.class, AppConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BillPaymentQueriesTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Logger logger;

    private BillPaymentQueries billPaymentQueries;

    @BeforeEach
    void setUp() {
        billPaymentQueries = new BillPaymentQueries(entityManager);
    }

    @Test
    public void testGetBillPaymentScheduleQuery() {
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        CriteriaQuery<BillPayeeInfoDTO> criteriaQuery = mock(CriteriaQuery.class);
        Root<BillPaymentEntity> root = mock(Root.class);
        Join<BillPaymentEntity, BillPaymentScheduleEntity> bps = mock(Join.class);
        Join<BillPaymentScheduleEntity, BillPaymentHistoryEntity> bph = mock(Join.class);
        Predicate predicate = mock(Predicate.class);
        Selection<BillPayeeInfoDTO> selection = mock(Selection.class);

        BillPayeeInfoDTO billPayeeInfoDTO = new BillPayeeInfoDTO("Payee 1", LocalDate.now(), LocalDate.now(), LocalDate.now(), BigDecimal.valueOf(100.0));

        // Mocking entity manager and query creation
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(BillPayeeInfoDTO.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(BillPaymentEntity.class)).thenReturn(root);

        // Mocking joins
        when(root.join("paymentSchedule")).thenAnswer(invocation -> bps);
        when(bps.join("billPaymentHistory")).thenAnswer(invocation -> bph);

        // Mocking selection and predicate
        when(criteriaBuilder.construct(
                eq(BillPayeeInfoDTO.class),
                any(), any(), any(), any(), any()
        )).thenAnswer(invocation -> {
            Class<BillPayeeInfoDTO> dtoClass = invocation.getArgument(0);
            Expression<?>[] arguments = new Expression<?>[5];
            for (int i = 0; i < 5; i++) {
                arguments[i] = invocation.getArgument(i + 1);
            }
            return criteriaBuilder.construct(dtoClass, arguments);
        });

        when(criteriaBuilder.equal(root.get("user").get("userID"), 1)).thenReturn(predicate);

        // Mocking query execution
        TypedQuery<BillPayeeInfoDTO> typedQuery = mock(TypedQuery.class);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(billPayeeInfoDTO));

        // Executing the query
        List<BillPayeeInfoDTO> result = billPaymentQueries.getBillPaymentScheduleQuery(1);

        // Verifying interactions
        verify(entityManager).getCriteriaBuilder();
        verify(criteriaBuilder).createQuery(BillPayeeInfoDTO.class);
        verify(criteriaQuery).from(BillPaymentEntity.class);
        verify(root).join("paymentSchedule");
        verify(bps).join("billPaymentHistory");
        verify(criteriaBuilder).equal(root.get("user").get("userID"), 1);
        verify(criteriaQuery).select(selection);
        verify(criteriaQuery).where(predicate);
        verify(entityManager).createQuery(criteriaQuery);
        verify(typedQuery).getResultList();

        // Asserting the result
        assertEquals(1, result.size());
        assertEquals("Payee 1", result.get(0).payeeName());
    }



    @AfterEach
    void tearDown() {
    }
}