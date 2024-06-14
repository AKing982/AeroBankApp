package com.example.aerobankapp.services;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.AccountIDNotFoundException;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.exceptions.InvalidUserStringException;
import com.example.aerobankapp.exceptions.NonEmptyListRequiredException;
import com.example.aerobankapp.repositories.AccountRepository;
import com.example.aerobankapp.repositories.AccountSecurityRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AccountDAOImplTest
{
    @InjectMocks
    private AccountServiceImpl accountDAO;

    @Mock
    private TypedQuery<Integer> typedQuery;

    @Mock
    private TypedQuery<AccountEntity> accountEntityTypedQuery;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountSecurityRepository accountSecurityRepository;

    private AccountEntity accountEntity;


    @BeforeEach
    void setUp()
    {
       // when(entityManager.createQuery(anyString(), eq(Integer.class))).thenReturn(typedQuery);
        accountEntity = AccountEntity.builder()
                .accountName("AKing Checking")
                .accountType("Checking")
                .isRentAccount(false)
                .acctID(1)
               // .accountCode("A1")
                .hasDividend(false)
                .balance(new BigDecimal("1205"))
                .interest(new BigDecimal("0.00"))
              //  .userID(1)
                .hasMortgage(false)
                .build();

        accountDAO = new AccountServiceImpl(entityManager,accountRepository, accountSecurityRepository);
    }

    @Test
    public void saveAccount()
    {
        accountDAO.delete(accountEntity);
        accountDAO.save(accountEntity);
        List<AccountEntity> expectedAccounts = new ArrayList<>();
        expectedAccounts.add(accountEntity);

        List<AccountEntity> accountEntityList = accountDAO.findAll();

        assertNotNull(accountEntity);
        assertEquals(1, accountEntityList.size());
        assertEquals(expectedAccounts.size(), accountEntityList.size());
        assertEquals(expectedAccounts.get(0).getAccountCode(), accountEntityList.get(0).getAccountCode());
    }

    @Test
    public void testGetValidUserName()
    {
        String username = "AKing94";
        List<AccountEntity> accountEntities = new ArrayList<>();
        accountEntities.add(accountEntity);

        List<AccountEntity> akingAccounts = accountDAO.findByUserName(username);
        System.out.println(akingAccounts.stream().findFirst().isPresent());
      //  int expectedUserID = Objects.requireNonNull(accountEntities.stream().findFirst().orElse(null)).getUserID();
       // int actualID = Objects.requireNonNull(akingAccounts.stream().findFirst().orElse(null)).getUserID();

      //  assertEquals(accountEntities.size(), akingAccounts.size());
        assertEquals(3, akingAccounts.size());
       // assertEquals(expectedUserID, actualID);

    }

    @Test
    public void testValidUserNameBSmith23()
    {
        String username = "BSmith23";
        List<AccountEntity> accountEntities = accountDAO.findByUserName(username);

        assertEquals(1, accountEntities.size());
    }

    @Test
    void getNumberOfAccounts_ValidUsername() {
        String username = "AKing94";
        int expectedCount = 5;

        when(typedQuery.setParameter("username", username)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(expectedCount);

        Long actualCount = accountDAO.getNumberOfAccounts(username);

        assertEquals(expectedCount, actualCount);
    }

    @Test
    void getNumberOfAccounts_NoUserFound() {
        String username = "NonExistentUser";

        when(typedQuery.setParameter("username", username)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(0); // Assuming 0 is returned when no user is found

        Long actualCount = accountDAO.getNumberOfAccounts(username);

        assertEquals(0, actualCount);
    }

    @Test
    public void testGetAccountTypeMapByAccountID()
    {
        String mockUser = "AKing94";

        Map<Integer, String> expectedAccountTypeMap = new HashMap<>();
        expectedAccountTypeMap.put(1, "Checking");
        expectedAccountTypeMap.put(2, "Savings");

        Map<Integer, String> accountTypeMap = accountDAO.getAccountTypeMapByAccountId(mockUser);

        assertNotNull(accountTypeMap);
        assertEquals(expectedAccountTypeMap.get(1), accountTypeMap.get(1));
        assertEquals(expectedAccountTypeMap.get(2), accountTypeMap.get(2));
    }

    private static Stream<Arguments> provideUserNamesForTest() {
        return Stream.of(
                Arguments.of("AKing94", new AccountEntity(1, "Checking")),
                Arguments.of("AKing94", new AccountEntity(2, "Savings")),
                Arguments.of("BSmith23", new AccountEntity(3, "Checking"))
                // Add more test cases as needed
        );
    }

    @ParameterizedTest
    @MethodSource("provideUserNamesForTest")
    public void testGetAccountTypeMapByAccountId(String userName, AccountEntity expectedAccount) {
        List<AccountEntity> mockResultList = Collections.singletonList(expectedAccount);

        when(entityManager.createQuery(anyString(), eq(AccountEntity.class))).thenReturn(accountEntityTypedQuery);
        when(accountEntityTypedQuery.setParameter(eq("userName"), eq(userName))).thenReturn(accountEntityTypedQuery);
        when(accountEntityTypedQuery.getResultList()).thenReturn(mockResultList);

        Map<Integer, String> result = accountDAO.getAccountTypeMapByAccountId(userName);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(expectedAccount.getAccountType(), result.get(expectedAccount.getAcctID()));

        verify(accountEntityTypedQuery).setParameter(eq("userName"), eq(userName));
        verify(accountEntityTypedQuery).getResultList();
    }
    @Test
    public void testGetAccountTypeMapByAccountId() {
        String userName = "testUser";
        List<AccountEntity> mockResultList = new ArrayList<>();
        mockResultList.add(new AccountEntity(1, "Checking"));
        mockResultList.add(new AccountEntity(2, "Savings"));

        when(entityManager.createQuery(anyString(), eq(AccountEntity.class))).thenReturn(accountEntityTypedQuery);
        when(accountEntityTypedQuery.setParameter(eq("userName"), eq(userName))).thenReturn(accountEntityTypedQuery);
        when(accountEntityTypedQuery.getResultList()).thenReturn(mockResultList);

        Map<Integer, String> result = accountDAO.getAccountTypeMapByAccountId(userName);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Checking", result.get(1));
        assertEquals("Savings", result.get(2));

        verify(accountEntityTypedQuery).setParameter(eq("userName"), eq(userName));
        verify(accountEntityTypedQuery).getResultList();
    }

    @Test
    @WithMockUser
    public void testGetAccountIDByAcctCodeAndUserID_ValidUser() throws AccountIDNotFoundException {
        int userID = 1;
        Long acctCode = 1L;
        TypedQuery<Integer> mockedQuery = mock(TypedQuery.class);

        when(entityManager.createQuery(anyString(), eq(Integer.class))).thenReturn(mockedQuery);
        when(mockedQuery.setParameter("userID", userID)).thenReturn(mockedQuery);
        when(mockedQuery.setParameter("acctCode", acctCode)).thenReturn(mockedQuery);
        when(mockedQuery.getSingleResult()).thenReturn(1);

        int actualAccountID = accountDAO.getAccountIDByAcctCodeAndUserID(userID, acctCode);

        assertEquals(1, actualAccountID);
    }

    @Test
    @WithMockUser
    public void testGetAccountIDByAcctCodeAndUserID_InvalidUserID()
    {
        int userID = 0;
        Long acctCode = 1L;
        TypedQuery<Integer> mockedQuery = mock(TypedQuery.class);
        TypedQuery<Long> longTypedQuery = mock(TypedQuery.class);

        when(entityManager.createQuery(eq("SELECT COUNT(a) FROM AccountEntity a WHERE a.accountCode =: acctCode"), eq(Long.class)))
                .thenReturn(longTypedQuery);
        when(longTypedQuery.setParameter(eq("acctCode"), anyString())).thenReturn(longTypedQuery);
        when(longTypedQuery.getSingleResult()).thenReturn(1L); // Assuming the account code exists

        lenient().when(entityManager.createQuery(anyString(), eq(Integer.class))).thenReturn(mockedQuery);
        lenient().when(mockedQuery.setParameter("userID", userID)).thenReturn(mockedQuery);
        lenient().when(mockedQuery.setParameter("acctCode", acctCode)).thenReturn(mockedQuery);
        lenient().when(mockedQuery.getSingleResult()).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            accountDAO.getAccountIDByAcctCodeAndUserID(userID, acctCode);
        });
    }

    @Test
    @WithMockUser
    public void testGetAccountIDByAcctCodeAndUserID_InvalidAccountCode()
    {
        int userID = 1;
        Long acctCode = -1L;
        TypedQuery<Integer> mockQuery = mock(TypedQuery.class);
        TypedQuery<Long> longTypedQuery = mock(TypedQuery.class);

        when(entityManager.createQuery(eq("SELECT COUNT(a) FROM AccountEntity a WHERE a.accountCode =: acctCode"), eq(Long.class)))
                .thenReturn(longTypedQuery);
        when(longTypedQuery.setParameter(eq("acctCode"), anyString())).thenReturn(longTypedQuery);
        when(longTypedQuery.getSingleResult()).thenReturn(1L); // Assuming the account code exists

        lenient().when(entityManager.createQuery(anyString(), eq(Integer.class))).thenReturn(mockQuery);
        lenient().when(mockQuery.setParameter("userID", userID)).thenReturn(mockQuery);
        lenient().when(mockQuery.setParameter("acctCode", acctCode)).thenReturn(mockQuery);
        lenient().when(mockQuery.getSingleResult()).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            accountDAO.getAccountIDByAcctCodeAndUserID(userID, acctCode);
        });

    }

    @Test
    public void testUpdateAccountBalanceByAcctID_Success()
    {
        BigDecimal balance = new BigDecimal("100.00");
        int acctID = 1;

        Query query = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        accountDAO.updateAccountBalanceByAcctID(balance, acctID);

        verify(query).setParameter("balance", balance);
        verify(query).setParameter("acctID", acctID);
        verify(query).executeUpdate();
    }

    @Test
    public void testUpdateAccountBalanceByAcctID_Exception() {
        BigDecimal balance = new BigDecimal("100.00");
        int acctID = 1;
        Query query = mock(Query.class);

        TypedQuery<Integer> longTypedQuery = mock(TypedQuery.class);

        lenient().when(entityManager.createQuery(eq("SELECT COUNT(a) FROM AccountEntity a WHERE a.acctID =: acctID"), eq(Integer.class)))
                .thenReturn(longTypedQuery);
        lenient().when(query.setParameter(eq("acctID"), eq(acctID))).thenReturn(query);
        lenient().when(longTypedQuery.getSingleResult()).thenReturn(1); // Assuming the account code exists

        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenThrow(new PersistenceException("Error updating"));

        assertThrows(PersistenceException.class, () -> {
            accountDAO.updateAccountBalanceByAcctID(balance, acctID);
        });
    }

    @Test
    public void testUpdateAccountBalanceByAcctID_InvalidParameters() {
        // Assuming acctID should be positive
        BigDecimal balance = new BigDecimal("100.00");
        int invalidAcctID = -1;

        // You can expect a specific exception or handle it accordingly
        assertThrows(IllegalArgumentException.class, () -> {
            accountDAO.updateAccountBalanceByAcctID(balance, invalidAcctID);
        });
    }

    @Test
    public void testGetBalanceByAcctID_ValidAcctID(){
        final int acctID = 1;

        BigDecimal expectedBalance = new BigDecimal("4500.000");
        BigDecimal actualBalance = accountDAO.getBalanceByAcctID(acctID);

        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    @DisplayName("Test getTotalBalances for acctID, return total balances")
    public void testGetTotalBalancesForAcctID(){
        int acctID = 1;
        BigDecimal expectedBalances = new BigDecimal("4500.000");
        BigDecimal actual = accountDAO.getTotalAccountBalances("AKing94");

        assertEquals(expectedBalances, actual);
    }

    @Test
    public void testGetAccountWithMostTransactionsByUserID_InvalidUserID(){
        final int userID = -1;

        assertThrows(InvalidUserIDException.class, () -> {
            accountDAO.getAccountWithMostTransactionsByUserID(userID);
        });
    }

    @Test
    public void testGetAccountWithMostTransactionsByUserID_ValidUserID(){
        final int userID = 1;

        int actualAccountID = accountDAO.getAccountWithMostTransactionsByUserID(userID);

        assertEquals(1, actualAccountID);
    }

    @Test
    public void testGetAccountIDByAcctCodeAndAcctNumber_EmptyParameters(){
        final Long acctCodeID = -1L;
        final String acctNumber = "";

        assertThrows(IllegalArgumentException.class, () -> {
            accountDAO.getAccountIDByAccountCodeAndAccountNumber(acctCodeID, acctNumber);
        });
    }

    @Test
    public void testGetAccountIDByAcctCodeAndAcctNumber_ValidParameters(){
        final Long acctCodeID = 1L;
        final String acctNumber = "89-42-48";

        int actual = accountDAO.getAccountIDByAccountCodeAndAccountNumber(acctCodeID, acctNumber);

        assertEquals(1, actual);
        assertEquals(2, accountDAO.getAccountIDByAccountCodeAndAccountNumber(acctCodeID, acctNumber));
    }

    @Test
    public void testGetAccountCodeShortSegmentByUser_EmptyUserString(){
        final String user = "";
        assertThrows(InvalidUserStringException.class, () -> {
            accountDAO.getAccountCodeShortSegmentByUser(user);
        });
    }

    @Test
    public void testGetAccountCodeShortSegmentByUser_ValidUser_DNE(){
        final String user = "AWest332";
        assertThrows(NonEmptyListRequiredException.class, () -> {
            accountDAO.getAccountCodeShortSegmentByUser(user);
        });
    }

    @Test
    public void testGetAccountCodeShortSegmentByUser_ValidUser_Exists(){
        final String user = "AKing94";

        List<String> rawAccountCodesSegments = List.of("A1", "A2", "A3");
        List<String> actualSegments = accountDAO.getAccountCodeShortSegmentByUser(user);

        assertNotNull(actualSegments);
        assertEquals(3, actualSegments.size());
        for(int i = 0; i < rawAccountCodesSegments.size(); i++){
            assertEquals(rawAccountCodesSegments.get(i), actualSegments.get(i));
        }

    }


    @AfterEach
    void tearDown() {
    }
}