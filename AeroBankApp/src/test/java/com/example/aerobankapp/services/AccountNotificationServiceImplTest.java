package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountNotificationEntity;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.repositories.AccountNotificationRepository;
import com.example.aerobankapp.workbench.AccountNotificationCategory;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
class AccountNotificationServiceImplTest {

    @InjectMocks
    private AccountNotificationServiceImpl accountNotificationService;

    @Mock
    private AccountNotificationRepository accountNotificationRepository;

    private AccountNotificationEntity accountNotificationEntity;

    @BeforeEach
    void setUp() {

        accountNotificationService = new AccountNotificationServiceImpl(accountNotificationRepository);
        accountNotificationEntity = buildAccountNotification(1, "Test", "This is a test", 1, true, false, AccountNotificationCategory.ACCOUNT_UPDATE);
    }

    @Test
    public void testFindAll_ReturnEmptyCollection(){

        when(accountNotificationRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(NonEmptyListRequiredException.class, () -> {
            accountNotificationService.findAll();
        });
    }

    @Test
    public void testFindAll_ReturnList(){

        List<AccountNotificationEntity> accountNotificationEntityList = List.of(accountNotificationEntity);

        when(accountNotificationRepository.findAll()).thenReturn(accountNotificationEntityList);

        List<AccountNotificationEntity> actualNotifications = accountNotificationService.findAll();

        assertNotNull(actualNotifications);
        assertFalse(actualNotifications.isEmpty());
        assertEquals(1, actualNotifications.size());
        assertEquals(1, actualNotifications.get(0).getAccount().getAcctID());
    }

    @Test
    public void testSave_NullNotification(){
        assertThrows(NullPointerException.class, () -> {
            accountNotificationService.save(null);
        });
    }

    @Test
    public void testSave_ValidNotification(){

        accountNotificationRepository.save(accountNotificationEntity);

        verify(accountNotificationRepository).save(accountNotificationEntity);
    }

    @Test
    public void testDelete_NullNotification(){
        assertThrows(NullAccountNotificationException.class, () -> {
            accountNotificationService.delete(null);
        });
    }

    @Test
    public void testDelete_ValidNotification(){
        accountNotificationRepository.delete(accountNotificationEntity);

        verify(accountNotificationRepository).delete(accountNotificationEntity);
    }

    @Test
    public void testGetAccountNotificationsByMessage_InvalidAcctID(){
        assertThrows(InvalidAccountNotificationException.class, () -> {
            accountNotificationService.getAccountNotificationsByMessage(-1, "This is a Test");
        });
    }

    @Test
    public void testGetAccountNotificationByMessage_InvalidAcctID_EmptyMessage(){
        assertThrows(InvalidAccountNotificationException.class, () -> {
            accountNotificationService.getAccountNotificationsByMessage(-1, "");
        });
    }

    @Test
    public void testGetAccountNotificationByMessage_ValidAcctID_ValidMessage(){

        AccountNotificationEntity accountNotification = buildAccountNotification(1, "Payment Received", "You have recieved a payment of $150 from John Doe.", 1, false, false, AccountNotificationCategory.PAYMENT_RECEIVED);
        when(accountNotificationRepository.findAccountNotificationsByMessage(1, "You have recieved a payment of $150 from John Doe.")).thenReturn(List.of(accountNotification));

        List<AccountNotificationEntity> actualNotifications = accountNotificationService.getAccountNotificationsByMessage(1, "You have recieved a payment of $150 from John Doe.");

        assertNotNull(actualNotifications);
        assertFalse(actualNotifications.isEmpty());
        assertEquals(1, actualNotifications.get(0).getAccount().getAcctID());
        assertEquals("You have recieved a payment of $150 from John Doe.", actualNotifications.get(0).getMessage());
        assertEquals("Payment Received", actualNotifications.get(0).getTitle());
    }

    @Test
    public void testGetAccountNotificationsByMessage_ValidAcctID_ValidMessage_ReturnEmptyList(){

        when(accountNotificationRepository.findAccountNotificationsByMessage(1, "You have recieved a payment of $150 from John Doe.")).thenReturn(Collections.emptyList());

        assertThrows(NonEmptyListRequiredException.class, () -> {
            accountNotificationService.getAccountNotificationsByMessage(1, "You have recieved a payment of $150 from John Doe.");
        });
    }

    @Test
    public void testGetAccountNotificationsByPriority_InvalidPriorityLevel(){
        assertThrows(InvalidPriorityLevelFoundException.class, () -> {
            accountNotificationService.getAccountNotificationsByPriority(1, -1);
        });
    }

    @Test
    public void testGetAccountNotificationByPriority_InvalidPriorityLevelAndInvalidAcctID(){
        assertThrows(InvalidAccountNotificationException.class, () -> {
            accountNotificationService.getAccountNotificationsByPriority(-1, -1);
        });
    }

    @Test
    public void testGetAccountNotificationsByPriority_ValidParameters(){

        when(accountNotificationRepository.findAccountNotificationsByPriorityLevel(1, 1)).thenReturn(List.of(accountNotificationEntity));

        List<AccountNotificationEntity> actualNotifications = accountNotificationService.getAccountNotificationsByPriority(1, 1);

        assertNotNull(actualNotifications);
        assertFalse(actualNotifications.isEmpty());
        assertEquals(1, actualNotifications.size());
        assertEquals(1, actualNotifications.get(0).getPriority());
        assertEquals(1, actualNotifications.get(0).getAccount().getAcctID());
    }

    @Test
    public void testGetAccountNotificationsByPriority_ValidParameters_EmptyList(){

        when(accountNotificationRepository.findAccountNotificationsByPriorityLevel(1, 1)).thenReturn(Collections.emptyList());

        assertThrows(NonEmptyListRequiredException.class, () -> {
            accountNotificationService.getAccountNotificationsByPriority(1, 1);
        });
    }

    @Test
    public void testGetAccountNotificationByTitle_InvalidAccountID(){
        assertThrows(InvalidAccountIDException.class, () -> {
            accountNotificationService.getAccountNotificationByTitle(-1, "Payment Reminder");
        });
    }

    @Test
    public void testGetAccountNotificationByTitle_EmptyTitle_ThrowException(){
        when(accountNotificationRepository.findAccountNotificationByTitle(1, "")).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            accountNotificationService.getAccountNotificationByTitle(1, "");
        });
    }

    @Test
    public void testGetAccountNotificationByTitle_ValidTitle_ValidAcctID_ReturnNotification(){

        Optional<AccountNotificationEntity> accountNotification = Optional.of(accountNotificationEntity);

        when(accountNotificationRepository.findAccountNotificationByTitle(1, "Test")).thenReturn(accountNotification);

        Optional<AccountNotificationEntity> actual = accountNotificationService.getAccountNotificationByTitle(1, "Test");
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(1, actual.get().getAccount().getAcctID());
        assertEquals("Test", actual.get().getTitle());
    }

    @Test
    public void testGetAccountNotificationByTitle_ValidTitle_ValidAcctID_ReturnEmptyOptional(){

        when(accountNotificationRepository.findAccountNotificationByTitle(1, "Payment Reminder")).thenReturn(Optional.empty());

        Optional<AccountNotificationEntity> actual = accountNotificationService.getAccountNotificationByTitle(1, "Payment Reminder");

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testGetAccountNotificationsThatAreRead_InvalidAccountID_ThrowException(){
        assertThrows(InvalidAccountIDException.class, () -> {
            accountNotificationService.getAccountNotificationsThatAreRead(-1);
        });
    }

    @Test
    public void testGetAccountNotificationThatAreRead_ValidAccountID_ReturnNotification(){
        when(accountNotificationRepository.findReadAccountNotifications(1)).thenReturn(List.of(accountNotificationEntity));

        List<AccountNotificationEntity> actual = accountNotificationService.getAccountNotificationsThatAreRead(1);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertTrue(actual.get(0).isRead());
    }

    @Test
    public void testGetAccountNotificationsThatAreRead_ValidAccountID_ReturnEmptyList(){
        when(accountNotificationRepository.findReadAccountNotifications(1)).thenReturn(Collections.emptyList());

        assertThrows(NonEmptyListRequiredException.class, () -> {
            accountNotificationService.getAccountNotificationsThatAreRead(1);
        });
    }

    @Test
    public void testGetAccountNotificationsByCategory_NullCategory(){
        when(accountNotificationRepository.findAccountNotificationsByCategory(null)).thenThrow(NullAccountNotificationCategoryException.class);

        assertThrows(NullAccountNotificationCategoryException.class, () -> {
            accountNotificationService.getAccountNotificationsByCategory(null);
        });
    }


    @AfterEach
    void tearDown() {
    }

    private AccountNotificationEntity buildAccountNotification(int acctID, String title, String message, int priority, boolean isRead, boolean isSevere,
                                                               AccountNotificationCategory category){
        return AccountNotificationEntity.builder()
                .account(AccountEntity.builder().acctID(acctID).build())
                .title(title)
                .message(message)
                .isRead(isRead)
                .isSevere(isSevere)
                .priority(priority)
                .accountNotificationCategory(category)
                .build();
    }
}