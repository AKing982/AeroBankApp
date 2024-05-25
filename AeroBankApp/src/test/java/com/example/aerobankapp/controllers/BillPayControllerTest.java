package com.example.aerobankapp.controllers;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.aerobankapp.configuration.AppConfig;
import com.example.aerobankapp.configuration.JpaConfig;
import com.example.aerobankapp.dto.BillPayeeInfoDTO;
import com.example.aerobankapp.dto.BillPaymentDTO;
import com.example.aerobankapp.dto.PaymentHistoryDTO;
import com.example.aerobankapp.dto.ScheduledPaymentDTO;
import com.example.aerobankapp.entity.BillPayeesEntity;
import com.example.aerobankapp.entity.BillPaymentEntity;
import com.example.aerobankapp.entity.BillPaymentHistoryEntity;
import com.example.aerobankapp.entity.BillPaymentScheduleEntity;
import com.example.aerobankapp.exceptions.UserNotFoundException;
import com.example.aerobankapp.services.BillPaymentHistoryService;
import com.example.aerobankapp.services.BillPaymentNotificationService;
import com.example.aerobankapp.services.BillPaymentScheduleService;
import com.example.aerobankapp.services.BillPaymentService;
import com.example.aerobankapp.services.utilities.BillPaymentHistoryServiceUtils;
import com.example.aerobankapp.services.utilities.BillPaymentScheduleServiceUtils;
import com.example.aerobankapp.services.utilities.BillPaymentServiceUtils;
import com.example.aerobankapp.workbench.billPayment.BillPaymentQueries;
import com.example.aerobankapp.workbench.formatter.FormatterUtil;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleFrequency;
import com.example.aerobankapp.workbench.utilities.schedule.ScheduleStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.With;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import javax.sql.DataSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value= BillPayController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.session.SessionAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class,
        org.springframework.session.jdbc.config.annotation.web.http.JdbcHttpSessionConfiguration.class
})
@Import({AppConfig.class, JpaConfig.class})
class BillPayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BillPaymentService billPaymentService;

    @MockBean
    private BillPaymentScheduleService billPaymentScheduleService;

    @MockBean
    private BillPaymentHistoryService billPaymentHistoryService;

    @MockBean
    private BillPaymentNotificationService billPaymentNotificationService;

    @MockBean
    private BillPaymentHistoryServiceUtils billPaymentHistoryServiceUtils;

    @MockBean
    private BillPaymentScheduleServiceUtils billPaymentScheduleServiceUtils;

    @MockBean
    private BillPaymentServiceUtils billPaymentServiceUtils;

    @MockBean
    private BillPaymentQueries billPaymentQueries;

    private final int VALID_USERID = 1;


    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser
    public void testFetchBillPayeesList_shouldReturnNotFound() throws Exception {
        int userID = 1;

        mockMvc.perform(get("/api/billpay/" + userID + "/list"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testFetchBillsPayeesList_shouldReturnBillPayees() throws Exception {
        int userID = 1;
        BillPaymentEntity billPayeesEntity = new BillPaymentEntity();
        billPayeesEntity.setPayeeName("Payee 1");
        billPayeesEntity.setPaymentID(1L);

        List<BillPaymentEntity> billPayeesEntityList = Collections.singletonList(billPayeesEntity);
        given(billPaymentService.findBillPaymentsByUserID(userID)).willReturn(billPayeesEntityList);

        mockMvc.perform(get("/api/bills/" + userID + "/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Payee 1"));
    }

    @Test
    @WithMockUser
    public void testFetchBillPayeesList_invalidUserID_shouldReturnBadRequest() throws Exception{
        int userID = -1;
        mockMvc.perform(get("/api/bills/" + userID + "/list"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFetchBillPayeesList_unauthorized_shouldReturnUnauthorized() throws Exception{
        int userID = 1;
        mockMvc.perform(get("/api/bills/" + userID + "/list"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void testFetchBillPayeesList_userNotFound_shouldReturnNotFound() throws Exception{
        int userID = 999;
        given(billPaymentService.findBillPaymentsByUserID(userID)).willThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/api/bills/" + userID + "/list"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testFetchBillPayeesList_internalServerError_shouldReturnInternalServerError() throws Exception {
        Integer userID = null;
        given(billPaymentService.findBillPaymentsByUserID(userID)).willThrow(new RuntimeException("Internal server error"));

        mockMvc.perform(get("/api/bills/" + userID + "/list"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser
    public void testFetchUserBillSchedules_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/schedules/" + VALID_USERID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testFetchUserBillSchedules_shouldReturnBillSchedules() throws Exception{
        LocalDate lastPayment = LocalDate.of(2024, 5, 19);
        LocalDate nextPayment = LocalDate.of(2024, 6, 19);
        LocalDate paymentDueDate = LocalDate.of(2024, 6, 19);
        BigDecimal paymentAmount = new BigDecimal("120.000");
        BillPayeeInfoDTO billPayeeInfoDTO = new BillPayeeInfoDTO("Payee 1", lastPayment, nextPayment, paymentDueDate, paymentAmount);
        ScheduledPaymentDTO scheduledPaymentDTO = new ScheduledPaymentDTO("Payee 1", "05/19/2024", "06/19/2024", paymentDueDate, paymentAmount.toString());

        List<BillPayeeInfoDTO> billPayeeInfoDTOS = Collections.singletonList(billPayeeInfoDTO);
        List<ScheduledPaymentDTO> billSchedules = Collections.singletonList(scheduledPaymentDTO);
        given(billPaymentQueries.getBillPaymentScheduleQuery(VALID_USERID)).willReturn(billPayeeInfoDTOS);
        try (MockedStatic<FormatterUtil> formatterUtilMock = Mockito.mockStatic(FormatterUtil.class)) {
            formatterUtilMock.when(() -> FormatterUtil.getFormattedBillPaymentSchedule(billPayeeInfoDTOS))
                    .thenReturn(billSchedules);

            mockMvc.perform(get("/api/bills/" + VALID_USERID + "/schedules"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].lastPayment").value("05/19/2024"))
                    .andExpect(jsonPath("$[0].nextPayment").value("06/19/2024"))
                    .andExpect(jsonPath("$[0].dueDate").value("2024-06-19"))
                    .andExpect(jsonPath("$[0].payeeName").value("Payee 1"));
        }
    }

    @ParameterizedTest
    @MethodSource("billScheduleProvider")
    @WithMockUser
    public void testFetchUserBillSchedules_shouldReturnBillSchedules(BillPayeeInfoDTO billPayeeInfoDTO, ScheduledPaymentDTO scheduledPaymentDTO) throws Exception {
        List<BillPayeeInfoDTO> billPayeeInfoDTOS = Collections.singletonList(billPayeeInfoDTO);
        List<ScheduledPaymentDTO> billSchedules = Collections.singletonList(scheduledPaymentDTO);
        given(billPaymentQueries.getBillPaymentScheduleQuery(VALID_USERID)).willReturn(billPayeeInfoDTOS);

        try (MockedStatic<FormatterUtil> formatterUtilMock = Mockito.mockStatic(FormatterUtil.class)) {
            formatterUtilMock.when(() -> FormatterUtil.getFormattedBillPaymentSchedule(billPayeeInfoDTOS))
                    .thenReturn(billSchedules);

            mockMvc.perform(get("/api/bills/" + VALID_USERID + "/schedules"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].lastPayment").value(scheduledPaymentDTO.lastPayment()))
                    .andExpect(jsonPath("$[0].nextPayment").value(scheduledPaymentDTO.nextPayment()))
                    .andExpect(jsonPath("$[0].dueDate").value(scheduledPaymentDTO.dueDate().toString()))
                    .andExpect(jsonPath("$[0].payeeName").value(scheduledPaymentDTO.payeeName()))
                    .andExpect(jsonPath("$[0].paymentAmount").value(scheduledPaymentDTO.paymentAmount()));
        }
    }

    static Stream<Arguments> billScheduleProvider() {
        LocalDate lastPayment1 = LocalDate.of(2024, 5, 19);
        LocalDate nextPayment1 = LocalDate.of(2024, 6, 19);
        LocalDate paymentDueDate1 = LocalDate.of(2024, 6, 19);
        BigDecimal paymentAmount1 = new BigDecimal("120.000");
        BillPayeeInfoDTO billPayeeInfoDTO1 = new BillPayeeInfoDTO("Payee 1", lastPayment1, nextPayment1, paymentDueDate1, paymentAmount1);
        ScheduledPaymentDTO scheduledPaymentDTO1 = new ScheduledPaymentDTO("Payee 1", "05/19/2024", "06/19/2024", paymentDueDate1, paymentAmount1.toString());

        LocalDate lastPayment2 = LocalDate.of(2024, 4, 10);
        LocalDate nextPayment2 = LocalDate.of(2024, 5, 10);
        LocalDate paymentDueDate2 = LocalDate.of(2024, 5, 10);
        BigDecimal paymentAmount2 = new BigDecimal("250.000");
        BillPayeeInfoDTO billPayeeInfoDTO2 = new BillPayeeInfoDTO("Payee 2", lastPayment2, nextPayment2, paymentDueDate2, paymentAmount2);
        ScheduledPaymentDTO scheduledPaymentDTO2 = new ScheduledPaymentDTO("Payee 2", "04/10/2024", "05/10/2024", paymentDueDate2, paymentAmount2.toString());

        return Stream.of(
                Arguments.of(billPayeeInfoDTO1, scheduledPaymentDTO1),
                Arguments.of(billPayeeInfoDTO2, scheduledPaymentDTO2)
        );
    }

    @Test
    @WithMockUser
    public void testFetchUserBillSchedules_invalidUserID_shouldReturnBadRequest() throws Exception{
        int userID = -1;
        mockMvc.perform(get("/api/bills/" + userID + "/schedules"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testFetchUserBillPaymentHistory_invalidUserID_shouldReturnBadRequest() throws Exception{
        int userID = -1;

        mockMvc.perform(get("/api/bills/" + userID + "/history"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void testFetchUserBillPaymentHistory_shouldReturnBillPaymentHistory() throws Exception{
        LocalDate posted = LocalDate.of(2024, 5, 24);
        BigDecimal paymentAmount = new BigDecimal(120);
        String payee = "Payee 1";
        PaymentHistoryDTO paymentHistoryDTO = new PaymentHistoryDTO(payee, paymentAmount, posted, ScheduleStatus.PENDING);
        List<PaymentHistoryDTO> paymentHistoryDTOS = Collections.singletonList(paymentHistoryDTO);

        given(billPaymentQueries.getBillPaymentHistoryQuery(VALID_USERID)).willReturn(paymentHistoryDTOS);

        mockMvc.perform(get("/api/bills/" + VALID_USERID + "/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].postedDate").value(posted.toString()))
                .andExpect(jsonPath("$[0].paymentAmount").value(paymentAmount))
                .andExpect(jsonPath("$[0].payeeName").value(payee));
    }

    @Test
    @WithMockUser
    public void testSavePaymentDetails_validPaymentDTO() throws Exception {
        BillPaymentDTO billPaymentDTO = new BillPaymentDTO("Payee 1",
                1, BigDecimal.valueOf(120), "Alex Checking - XXXX011", 1, ScheduleFrequency.ONE_TIME, true, LocalDate.of(2024, 6, 19), LocalDate.of(2024, 6, 19),true);

        doNothing().when(billPaymentService).save(any(BillPaymentEntity.class));
        doNothing().when(billPaymentScheduleService).save(any(BillPaymentScheduleEntity.class));
        doNothing().when(billPaymentHistoryService).save(any(BillPaymentHistoryEntity.class));

        mockMvc.perform(post("/api/bills/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(billPaymentDTO)))
                .andExpect(status().isOk())
                .andDo(print());

        verify(billPaymentService).save(any(BillPaymentEntity.class));
        verify(billPaymentScheduleService).save(any(BillPaymentScheduleEntity.class));
        verify(billPaymentHistoryService).save(any(BillPaymentHistoryEntity.class));
    }

    @Test
    @WithMockUser
    public void testSavePaymentDetails_invalidPaymentDTO() throws Exception{
        BillPaymentDTO paymentDTO = null;

        mockMvc.perform(post("/api/bills/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(paymentDTO)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    @AfterEach
    void tearDown() {
    }
}