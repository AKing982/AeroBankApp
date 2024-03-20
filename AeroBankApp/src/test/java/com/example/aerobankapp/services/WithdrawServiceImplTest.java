package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.entity.WithdrawEntity;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.repositories.WithdrawRepository;
import com.example.aerobankapp.workbench.utilities.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
class WithdrawServiceImplTest {

    @InjectMocks
    private WithdrawServiceImpl withdrawService;

    @MockBean
    private WithdrawRepository withdrawRepository;

    private WithdrawEntity mockWithdraw;

    @BeforeEach
    void setUp()
    {
        withdrawService = new WithdrawServiceImpl(withdrawRepository);
        mockWithdraw = createMockWithdraw(1L, 1, 1, "Test Withdrawal", new BigDecimal("45.00"));
    }

    @Test
    public void testFindAllReturnsNonEmptyList(){
        WithdrawEntity withdrawEntity = createMockWithdraw(1L, 1, 1, "Checking Withdraw", new BigDecimal("45.00"));
        when(withdrawRepository.findAll()).thenReturn(List.of(withdrawEntity));

        List<WithdrawEntity> results = withdrawService.findAll();

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }

    @Test
    public void testFindAllReturnsEmptyList(){

        when(withdrawRepository.findAll()).thenReturn(Collections.emptyList());

       assertThrows(NoWithdrawEntitiesFoundException.class, () -> {
           withdrawService.findAll();
       });
    }

    @Test
    public void testFindByUserName_EmptyStringUser(){
        when(withdrawRepository.findWithdrawalsByUserName("")).thenReturn(Collections.emptyList());

        assertThrows(InvalidUserCriteriaException.class, () -> {
            withdrawService.findByUserName("");
        });
    }

    @Test
    public void testFindByUserName_ValidUser(){
        WithdrawEntity withdrawEntity = createMockWithdraw(1L, 1, 1, "Checking Withdraw", new BigDecimal("45.00"));
        when(withdrawRepository.findWithdrawalsByUserName("AKing94")).thenReturn(List.of(withdrawEntity));

        List<WithdrawEntity> withdrawEntityList = withdrawService.findByUserName("AKing94");

        assertNotNull(withdrawEntityList);
        assertEquals(1, withdrawEntityList.size());
        assertFalse(withdrawEntityList.isEmpty());
    }

    @Test
    public void testFindByUserName_ValidUser_EmptyList(){
        WithdrawEntity withdrawEntity = createMockWithdraw(1L, 1, 1, "Checking Withdraw", new BigDecimal("45.00"));
        when(withdrawRepository.findWithdrawalsByUserName("AKing94")).thenReturn(Collections.emptyList());

        assertThrows(NoWithdrawEntitiesFoundException.class, () -> {
            withdrawService.findByUserName("AKing94");
        });
    }

    @Test
    public void testFindWithdrawBetweenDatesWithValidRange(){
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 1, 31);
        WithdrawEntity withdraw = createMockWithdrawWithDate(1L, 1, 1, "Withdraw from Checking", new BigDecimal("45.00"), startDate);

        when(withdrawRepository.findWithdrawsBetweenDates(startDate, endDate)).thenReturn(List.of(withdraw));

        List<WithdrawEntity> results = withdrawService.findWithdrawBetweenDates(startDate, endDate);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(withdraw.getWithdrawID(), results.get(0).getWithdrawID());
    }

    @Test
    public void testFindWithdrawBetweenDatesWithNullDates(){

        when(withdrawRepository.findWithdrawsBetweenDates(null, null)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            List<WithdrawEntity> results = withdrawService.findWithdrawBetweenDates(null, null);
        });
    }

    @Test
    public void testFindWithdrawBetweenDates_ReturnEmptyList(){
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 1, 31);
        when(withdrawRepository.findWithdrawsBetweenDates(startDate, endDate)).thenReturn(Collections.emptyList());

        assertThrows(NoWithdrawEntitiesFoundException.class, () -> {
            withdrawService.findWithdrawBetweenDates(startDate, endDate);
        });
    }

    @Test
    public void testGetListOfWithdrawalsByUserIDAsc_InvalidUser(){
        final int userID = -1;

        when(withdrawRepository.findByUserIDAscending(userID)).thenThrow(InvalidUserIDException.class);

        assertThrows(InvalidUserIDException.class, () -> {
            withdrawService.getListOfWithdrawalsByUserIDAsc(userID);
        });
    }

    @Test
    public void testGetListOfWithdrawalsByUserID_Asc_ValidUserID(){
        final int userID = 1;

        when(withdrawRepository.findByUserIDAscending(userID)).thenReturn(List.of(mockWithdraw));

        List<WithdrawEntity> results = withdrawService.getListOfWithdrawalsByUserIDAsc(userID);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }

    @Test
    public void testGetListOfWithdrawalsByUserID_Asc_ValidUserID_ReturnEmptyList(){
        final int userID = 1;

        when(withdrawRepository.findByUserIDAscending(userID)).thenReturn(Collections.emptyList());

        assertThrows(NoWithdrawEntitiesFoundException.class, () -> {
            withdrawService.getListOfWithdrawalsByUserIDAsc(userID);
        });
    }

    @Test
    public void testGetWithdrawalByDescription_EmptyDescription(){
        final String description = "";

        when(withdrawRepository.findByDescription(description)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            withdrawService.getWithdrawalByDescription(description);
        });
    }

    @Test
    public void testGetWithdrawalByDescription_ValidDescription(){
        final String description = "Test Withdrawal";
        Optional<WithdrawEntity> optionalWithdrawEntity = Optional.of(mockWithdraw);
        when(withdrawRepository.findByDescription(description)).thenReturn(optionalWithdrawEntity);

        Optional<WithdrawEntity> result = withdrawService.getWithdrawalByDescription(description);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(mockWithdraw, result.get());
    }

    @Test
    public void testGetWithdrawalByDescription_ValidDescription_ReturnEmpty(){
        final String description = "Test Withdrawal";

        when(withdrawRepository.findByDescription(description)).thenReturn(Optional.empty());

        assertThrows(NoWithdrawEntitiesFoundException.class, () -> {
            withdrawService.getWithdrawalByDescription(description);
        });
    }

    @Test
    public void testGetWithdrawalAmountById_InvalidID(){
        final Long id = -1L;

        when(withdrawRepository.findAmountById(id)).thenThrow(InvalidUserIDException.class);

        assertThrows(InvalidUserIDException.class, () -> {
            withdrawService.getWithdrawalAmountById(id);
        });
    }

    @Test
    public void testGetWithdrawalAmountByID_ValidID(){
        final Long id = 1L;

        when(withdrawRepository.findAmountById(id)).thenReturn(new BigDecimal("45.00"));

        BigDecimal actualAmount = withdrawService.getWithdrawalAmountById(id);

        assertNotNull(actualAmount);
        assertEquals(new BigDecimal("45.00"), actualAmount);
    }

    @Test
    public void testGetWithdrawalAmountByID_ValidID_NullAmount(){
        final Long id = 5L;

        when(withdrawRepository.findAmountById(id)).thenReturn(null);

        assertThrows(WithdrawalAmountNotFoundException.class, () -> {
            withdrawService.getWithdrawalAmountById(id);
        });
    }

    @Test
    public void testFindByStatus_NullStatus(){

        when(withdrawRepository.findByStatus(null)).thenThrow(StatusNotFoundException.class);

        assertThrows(StatusNotFoundException.class, () -> {
            withdrawService.findByStatus(null);
        });
    }

    @Test
    public void testFindByStatus_ActiveStatus(){
        Status active = Status.ACTIVE;

        when(withdrawRepository.findByStatus(active)).thenReturn(List.of(mockWithdraw));

        List<WithdrawEntity> results = withdrawService.findByStatus(active);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }

    @Test
    public void testFindByStatus_ActiveStatus_ReturnEmptyList(){
        Status status = Status.ACTIVE;

        when(withdrawRepository.findByStatus(status)).thenReturn(Collections.emptyList());

        assertThrows(NoWithdrawEntitiesFoundException.class, () -> {
            withdrawService.findByStatus(status);
        });
    }

    @Test
    public void testFindByStatus_ActiveStatus_ReturnNull(){
        Status status = Status.ACTIVE;

        when(withdrawRepository.findByStatus(status)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            withdrawService.findByStatus(status);
        });
    }


    private WithdrawEntity createMockWithdrawWithDate(Long id, int userID, int acctID, String description, BigDecimal amount, LocalDate posted)
    {
        WithdrawEntity withdrawEntity = new WithdrawEntity();
        withdrawEntity.setWithdrawID(id);
        withdrawEntity.setAccount(AccountEntity.builder().acctID(acctID).build());
        withdrawEntity.setUser(UserEntity.builder().userID(userID).build());
        withdrawEntity.setAmount(amount);
        withdrawEntity.setDescription(description);
        withdrawEntity.setPosted(posted);
        withdrawEntity.setStatus(Status.ACTIVE);
        return withdrawEntity;
    }

    private WithdrawEntity createMockWithdraw(Long id, int userID, int acctID, String description, BigDecimal amount)
    {
        WithdrawEntity withdrawEntity = new WithdrawEntity();
        withdrawEntity.setWithdrawID(id);
        withdrawEntity.setAccount(AccountEntity.builder().acctID(acctID).build());
        withdrawEntity.setUser(UserEntity.builder().userID(userID).build());
        withdrawEntity.setAmount(amount);
        withdrawEntity.setDescription(description);
        withdrawEntity.setPosted(LocalDate.now());
        withdrawEntity.setStatus(Status.ACTIVE);
        return withdrawEntity;
    }

    @AfterEach
    void tearDown() {
    }
}